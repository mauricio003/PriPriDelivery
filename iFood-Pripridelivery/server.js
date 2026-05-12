import nodemailer from 'nodemailer';
import PDFDocument from 'pdfkit';
import express from 'express';
import cors from 'cors';
import twilio from 'twilio';
import { fileURLToPath } from 'url';
import { dirname } from 'path';
import path from 'path';
import dotenv from 'dotenv';
import { MercadoPagoConfig, Preference } from 'mercadopago';

dotenv.config();

const __filename = fileURLToPath(import.meta.url);
const __dirname = dirname(__filename);

const app = express();

// Configure CORS to allow requests from the Vite dev server
app.use(cors({
  origin: ['http://localhost:5173', 'http://127.0.0.1:5173'],
  credentials: true
}));

app.use(express.json());

const accountSid = process.env.TWILIO_ACCOUNT_SID;
const authToken = process.env.TWILIO_AUTH_TOKEN;
const verifyServiceSid = process.env.TWILIO_VERIFY_SERVICE_SID;

let client = null;
const isTwilioConfigured = accountSid && authToken && verifyServiceSid;

if (!isTwilioConfigured) {
  console.warn('⚠️ ATENÇÃO: Variáveis do Twilio ausentes. Iniciando no MODO SIMULAÇÃO (Mock SMS). O código de teste será sempre 123456.');
} else {
  client = twilio(accountSid, authToken);
  console.log('✅ Twilio configurado e pronto para envio de SMS real.');
}

const mercadoPagoClient = new MercadoPagoConfig({
  accessToken: process.env.MERCADO_PAGO_ACCESS_TOKEN
});


const formatPhoneNumber = (phoneNumber) => {
  // Remove all non-numeric characters
  const cleaned = phoneNumber.replace(/\D/g, '');
  
  // If it already has the country code, return as is
  if (phoneNumber.startsWith('+')) {
    return phoneNumber;
  }
  
  // Add Brazilian country code if not present
  return `+55${cleaned}`;
};

app.post('/api/send-verification', async (req, res) => {
  try {
    let { phoneNumber } = req.body;
    phoneNumber = formatPhoneNumber(phoneNumber);
    
    console.log('Enviando verificação para:', phoneNumber);
    
    if (!isTwilioConfigured) {
      console.log(`[MOCK] Simulando envio de SMS para ${phoneNumber}`);
      return res.json({ success: true, sid: 'mock_sid_123', mock: true });
    }
    
    const verification = await client.verify.v2
      .services(verifyServiceSid)
      .verifications
      .create({
        to: phoneNumber,
        channel: 'sms'
      });
    
    console.log('Verificação enviada:', verification.sid);
    res.json({ success: true, sid: verification.sid, mock: false });
  } catch (error) {
    console.error('Erro Twilio:', error);
    res.status(500).json({ 
      success: false, 
      error: error.message || 'Falha ao enviar código de verificação'
    });
  }
});

app.post('/api/verify-code', async (req, res) => {
  try {
    let { phoneNumber, code } = req.body;
    phoneNumber = formatPhoneNumber(phoneNumber);
    
    console.log('Verificando código para:', phoneNumber);
    
    if (!phoneNumber || !code) {
      return res.status(400).json({
        success: false,
        error: 'Número de telefone e código são obrigatórios'
      });
    }

    if (!isTwilioConfigured) {
      const isCodeValid = code === '123456';
      console.log(`[MOCK] Verificação do código ${code}: ${isCodeValid ? 'Aprovado' : 'Recusado'}`);
      return res.json({
        success: true,
        valid: isCodeValid,
        status: isCodeValid ? 'approved' : 'pending',
        mock: true
      });
    }

    const verificationCheck = await client.verify.v2
      .services(verifyServiceSid)
      .verificationChecks
      .create({
        to: phoneNumber,
        code: code
      });
    
    console.log('Resultado da verificação:', verificationCheck.status);
    
    res.json({
      success: true,
      valid: verificationCheck.status === 'approved',
      status: verificationCheck.status,
      mock: false
    });
  } catch (error) {
    console.error('Erro de verificação:', error);
    res.status(500).json({
      success: false,
      error: error.message || 'Falha ao verificar código'
    });
  }
});

app.post('/criar-preferencia', async (req, res) => {
  try {
    const { itens, total } = req.body;

    const preference = new Preference(mercadoPagoClient);

    const response = await preference.create({
      body: {
        items: itens?.length
          ? itens.map((item) => ({
              title: item.nome || 'Produto PriPriDelivery',
              quantity: Number(item.quantidade || 1),
              unit_price: Number(item.preco || 0),
              currency_id: 'BRL'
            }))
          : [
              {
                title: 'Pedido PriPriDelivery',
                quantity: 1,
                unit_price: Number(total),
                currency_id: 'BRL'
              }
            ],
        back_urls: {
          success: 'http://localhost:5173/acompanhamento',
          failure: 'http://localhost:5173/pagamento',
          pending: 'http://localhost:5173/pagamento'
        },
      }
    });

    res.json({
      id: response.id,
      init_point: response.init_point
    });
  } catch (error) {
    console.error('Erro Mercado Pago:', error);
    res.status(500).json({
      erro: 'Erro ao criar preferência Mercado Pago',
      detalhes: error.message
    });
  }
});

app.post('/api/enviar-nfe-pdf', async (req, res) => {
  try {
    const { pedidoId, email, nomeCliente, itens, total, endereco } = req.body;

    if (!email) {
      return res.status(400).json({ success: false, error: 'E-mail do cliente não informado' });
    }

    const doc = new PDFDocument({ margin: 50 });
    const buffers = [];

    doc.on('data', buffers.push.bind(buffers));

    doc.fontSize(20).text('PriPriDelivery', { align: 'center' });
    doc.moveDown();
    doc.fontSize(16).text('Nota Fiscal / Comprovante do Pedido');
    doc.moveDown();

    doc.fontSize(12).text(`Pedido: ${pedidoId}`);
    doc.text(`Cliente: ${nomeCliente || 'Cliente'}`);
    doc.text(`E-mail: ${email}`);
    doc.text(`Data: ${new Date().toLocaleString('pt-BR')}`);

    if (endereco) {
      doc.moveDown();
      doc.text('Endereço de entrega:');
      doc.text(`${endereco.logradouro || ''}, ${endereco.numero || ''}`);
      doc.text(`${endereco.bairro || ''} - ${endereco.cidade || ''}/${endereco.estado || ''}`);
      doc.text(`CEP: ${endereco.cep || ''}`);
    }

    doc.moveDown();
    doc.text('Itens do pedido:');
    doc.moveDown();

    itens.forEach((item) => {
      const subtotal = Number(item.preco || 0) * Number(item.quantidade || 1);
      doc.text(`${item.quantidade}x ${item.nome} - R$ ${subtotal.toFixed(2)}`);
    });

    doc.moveDown();
    doc.fontSize(14).text(`Total: R$ ${Number(total || 0).toFixed(2)}`, { align: 'right' });

    doc.end();

    doc.on('end', async () => {
      const pdfBuffer = Buffer.concat(buffers);

      const transporter = nodemailer.createTransport({
        service: 'gmail',
        auth: {
          user: process.env.EMAIL_USER,
          pass: process.env.EMAIL_PASS
        }
      });

      await transporter.sendMail({
        from: `"PriPriDelivery" <${process.env.EMAIL_USER}>`,
        to: email,
        subject: `Nota Fiscal do Pedido ${pedidoId}`,
        text: `Olá, ${nomeCliente || 'Cliente'}! Segue em anexo a nota fiscal/comprovante do seu pedido.`,
        attachments: [
          {
            filename: `nota-fiscal-${pedidoId}.pdf`,
            content: pdfBuffer,
            contentType: 'application/pdf'
          }
        ]
      });

      res.json({ success: true, message: 'NF em PDF enviada por e-mail' });
    });
  } catch (error) {
    console.error('Erro ao enviar NF PDF:', error);
    res.status(500).json({
      success: false,
      error: 'Erro ao enviar NF em PDF',
      detalhes: error.message
    });
  }
});

app.get('/api/health', (req, res) => {
  res.json({ status: 'ok' });
});

// --- Funcionalidade de Acompanhamento de Pedido ---
const ordersStatus = new Map();
const statusSteps = [
  'O restaurante aceitou o pedido',
  'Pedido sendo preparado',
  'Encontrando motorista parceiro',
  'Seu motorista está indo até você',
  'Seu pedido chegou'
];

app.get('/api/order-status/:orderId', (req, res) => {
  const { orderId } = req.params;
  const transitionTime = parseInt(process.env.ORDER_STATUS_TRANSITION_TIME || '30', 10) * 1000;

  if (!ordersStatus.has(orderId)) {
    ordersStatus.set(orderId, {
      createdAt: Date.now(),
      id: orderId
    });
  }

  const order = ordersStatus.get(orderId);
  const elapsed = Date.now() - order.createdAt;
  
  // Calcula o index do status baseado no tempo decorrido
  let statusIndex = Math.floor(elapsed / transitionTime);
  
  if (statusIndex >= statusSteps.length) {
    statusIndex = statusSteps.length - 1;
  }

  res.json({
    orderId,
    status: statusSteps[statusIndex],
    statusIndex,
    isFinished: statusIndex === statusSteps.length - 1,
    elapsedSeconds: Math.floor(elapsed / 1000),
    nextStatusIn: statusIndex < statusSteps.length - 1 
      ? Math.ceil((transitionTime - (elapsed % transitionTime)) / 1000)
      : 0
  });
});
// --------------------------------------------------

// Serve static files from the dist directory
app.use(express.static(path.join(__dirname, 'dist')));

// Handle all other routes by serving the index.html
app.get('*', (req, res) => {
  res.sendFile(path.join(__dirname, 'dist', 'index.html'));
});

const PORT = process.env.PORT || 3001;
app.listen(PORT, () => {
  console.log(`Servidor rodando na porta ${PORT}`);
});