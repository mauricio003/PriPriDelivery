import dotenv from 'dotenv';
dotenv.config();

import express from 'express';
import twilio from 'twilio';
import cors from 'cors';

const app = express();
app.use(cors());
app.use(express.json());

const MOCK_MODE = !process.env.TWILIO_ACCOUNT_SID ||
                  !process.env.TWILIO_AUTH_TOKEN  ||
                  !process.env.TWILIO_VERIFY_SERVICE_SID;

if (MOCK_MODE) {
  console.warn('⚠️  [MOCK MODE] Credenciais do Twilio não encontradas. Usando mock — código fixo: 123456');
}

const client = MOCK_MODE
  ? null
  : twilio(process.env.TWILIO_ACCOUNT_SID, process.env.TWILIO_AUTH_TOKEN);

/* ── Enviar código de verificação ── */
app.post('/api/send-verification', async (req, res) => {
  try {
    const { phoneNumber } = req.body;

    if (!phoneNumber) {
      return res.status(400).json({ error: 'Número de telefone é obrigatório.' });
    }

    if (MOCK_MODE) {
      console.log(`[MOCK] Código 123456 "enviado" para ${phoneNumber}`);
      return res.json({ success: true, mock: true });
    }

    await client.verify.v2
      .services(process.env.TWILIO_VERIFY_SERVICE_SID)
      .verifications
      .create({ to: phoneNumber, channel: 'sms' });

    res.json({ success: true, mock: false });
  } catch (error) {
    console.error('Erro ao enviar código:', error.message);
    res.status(500).json({ error: error.message || 'Erro ao enviar código de verificação.' });
  }
});

/* ── Verificar código digitado ── */
app.post('/api/verify-code', async (req, res) => {
  try {
    const { phoneNumber, code } = req.body;

    if (!phoneNumber || !code) {
      return res.status(400).json({ error: 'Telefone e código são obrigatórios.' });
    }

    if (MOCK_MODE) {
      const valid = code === '123456';
      return res.json({ valid });
    }

    const check = await client.verify.v2
      .services(process.env.TWILIO_VERIFY_SERVICE_SID)
      .verificationChecks
      .create({ to: phoneNumber, code });

    res.json({ valid: check.status === 'approved' });
  } catch (error) {
    console.error('Erro ao verificar código:', error.message);
    res.status(500).json({ error: error.message || 'Erro ao verificar código.' });
  }
});

app.listen(3001, () => console.log('🚀 Servidor rodando na porta 3001'));