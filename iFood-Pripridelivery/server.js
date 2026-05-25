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
import { initializeApp as initializeAdminApp, cert, getApps } from 'firebase-admin/app';
import { getFirestore } from 'firebase-admin/firestore';
import { readFileSync, existsSync } from 'fs';

dotenv.config();

const __filename = fileURLToPath(import.meta.url);
const __dirname = dirname(__filename);

// --- Firebase Admin Initialization ---
let adminDb = null;

try {
  const serviceAccountPath = path.join(__dirname, 'priprideli-firebase-adminsdk-fbsvc-f00fa3c42e.json');

  if (existsSync(serviceAccountPath)) {
    const serviceAccount = JSON.parse(readFileSync(serviceAccountPath, 'utf8'));

    if (getApps().length === 0) {
      initializeAdminApp({ credential: cert(serviceAccount) });
    }
    adminDb = getFirestore();
    console.log('✅ Firebase Admin conectado (via serviceAccountKey.json)');
  } else {
    // Fallback: usar variáveis de ambiente do Firebase (project ID)
    const projectId = process.env.VITE_FIREBASE_PROJECT_ID;
    if (projectId) {
      if (getApps().length === 0) {
        initializeAdminApp({ projectId });
      }
      adminDb = getFirestore();
      console.log('✅ Firebase Admin conectado (via projectId do .env)');
    } else {
      console.warn('⚠️ Firebase Admin não configurado. A Pri não terá acesso aos dados reais de restaurantes/produtos.');
    }
  }
} catch (error) {
  console.warn('⚠️ Erro ao inicializar Firebase Admin:', error.message);
  console.warn('   A Pri funcionará sem dados reais de restaurantes.');
}

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

// --- Integração com a Pri (OpenRouter + Firebase) ---

// Função auxiliar para buscar dados reais do Firebase
async function buscarCardapioReal() {
  if (!adminDb) return null;

  try {
    const restaurantesSnap = await adminDb.collection('restaurantes').get();
    const restaurantes = [];

    for (const restDoc of restaurantesSnap.docs) {
      const restData = restDoc.data();

      const produtosSnap = await adminDb.collection('produtos')
        .where('restauranteId', '==', restDoc.id)
        .get();

      // Fallback: tentar campo alternativo usado no seed (restaurante_id)
      let produtos = produtosSnap.docs.map(p => p.data());
      if (produtos.length === 0) {
        const produtosSnapAlt = await adminDb.collection('produtos')
          .where('restaurante_id', '==', restDoc.id)
          .get();
        produtos = produtosSnapAlt.docs.map(p => p.data());
      }

      restaurantes.push({
        nome: restData.nome,
        categoria: restData.categoria,
        descricao: restData.descricao,
        taxaEntregaNormal: restData.taxaEntregaNormal,
        tempoEntregaNormal: restData.tempoEntregaNormal,
        produtos: produtos.map(p => ({
          nome: p.nome,
          descricao: p.descricao,
          preco: p.preco,
          categoria: p.categoria,
          disponivel: p.disponivel !== false
        }))
      });
    }

    return restaurantes;
  } catch (error) {
    console.error('Erro ao buscar cardápio do Firebase:', error.message);
    return null;
  }
}

// Função auxiliar para chamar OpenRouter com fallback de modelos
async function chamarOpenRouter(messages) {
  const modelos = [
    'nvidia/nemotron-3-nano-omni-30b-a3b-reasoning:free',
    'deepseek/deepseek-v4-flash:free',
    'google/gemma-4-31b-it:free'
  ];

  for (const modelo of modelos) {
    try {
      console.log(`🤖 Tentando modelo: ${modelo}`);
      const response = await fetch('https://openrouter.ai/api/v1/chat/completions', {
        method: 'POST',
        headers: {
          'Authorization': `Bearer ${process.env.OPENROUTER_API_KEY}`,
          'Content-Type': 'application/json',
          'HTTP-Referer': 'http://localhost:5173',
          'X-Title': 'PriPriDelivery',
        },
        body: JSON.stringify({
          model: modelo,
          messages
        })
      });

      const data = await response.json();

      // Se a API retornou erro (rate limit, etc), tentar próximo modelo
      if (data.error) {
        console.warn(`⚠️ Modelo ${modelo} retornou erro: ${data.error.message || JSON.stringify(data.error)}`);
        continue;
      }

      if (data.choices && data.choices[0] && data.choices[0].message) {
        console.log(`✅ Resposta recebida do modelo: ${modelo}`);
        return data;
      }

      console.warn(`⚠️ Modelo ${modelo} não retornou resposta válida:`, JSON.stringify(data).slice(0, 200));
    } catch (error) {
      console.warn(`⚠️ Erro no modelo ${modelo}: ${error.message}`);
    }
  }

  return null;
}

app.post('/api/chat-pri', async (req, res) => {
  try {
    const { answers } = req.body;

    if (!process.env.OPENROUTER_API_KEY) {
      return res.status(500).json({ error: 'OPENROUTER_API_KEY não configurada no servidor.' });
    }

    if (!answers || answers.length !== 4) {
      return res.status(400).json({ error: 'Respostas incompletas para a Pri.' });
    }

    // Buscar dados reais dos restaurantes/produtos
    const cardapio = await buscarCardapioReal();

    let contextoCardapio = '';
    if (cardapio && cardapio.length > 0) {
      contextoCardapio = '\n\nAqui estão os restaurantes e produtos REAIS disponíveis no nosso app:\n\n';
      for (const rest of cardapio) {
        contextoCardapio += `🏪 ${rest.nome} (${rest.categoria}) — ${rest.descricao}\n`;
        contextoCardapio += `   Entrega: R$ ${rest.taxaEntregaNormal?.toFixed(2) || '0.00'} | Tempo: ~${rest.tempoEntregaNormal || '?'} min\n`;

        const produtosDisponiveis = rest.produtos.filter(p => p.disponivel);
        if (produtosDisponiveis.length > 0) {
          for (const prod of produtosDisponiveis) {
            contextoCardapio += `   • ${prod.nome} — R$ ${prod.preco?.toFixed(2)} — ${prod.descricao}\n`;
          }
        }
        contextoCardapio += '\n';
      }
    }

    const prompt = `O usuário respondeu as seguintes preferências para sua refeição:
1. Orçamento: ${answers[0]}
2. Preparo: ${answers[1]}
3. Tipo de refeição: ${answers[2]}
4. Restrições: ${answers[3]}

Por favor, recomende pratos e restaurantes de forma curta, amigável e direta, sugerindo opções que combinem perfeitamente com essas escolhas.${cardapio ? ' Use APENAS os restaurantes e produtos listados abaixo. Não invente opções que não existam no cardápio.' : ''}`;

    const systemPrompt = `Você é a Pri, uma assistente virtual simpática do aplicativo PriPriDelivery. Seja sempre breve, use emojis com moderação e foque em sugerir pratos ou restaurantes de acordo com os filtros do usuário.${contextoCardapio ? ' Você DEVE recomendar apenas itens que existem no cardápio real fornecido abaixo. Mencione o nome do restaurante e o preço dos pratos.' + contextoCardapio : ' Como não temos dados do cardápio disponíveis agora, faça sugestões genéricas de tipos de comida.'}`;

    const messages = [
      { role: 'system', content: systemPrompt },
      { role: 'user', content: prompt }
    ];

    const data = await chamarOpenRouter(messages);

    if (data) {
      res.json(data);
    } else {
      res.status(502).json({ error: 'Nenhum modelo de IA disponível no momento. Tente novamente mais tarde.' });
    }
  } catch (error) {
    console.error('Erro no OpenRouter:', error);
    res.status(500).json({ error: 'Erro ao se comunicar com a Pri.' });
  }
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