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

app.get('/api/health', (req, res) => {
  res.json({ status: 'ok' });
});

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