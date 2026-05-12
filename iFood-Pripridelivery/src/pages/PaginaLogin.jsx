import React, { useState, useEffect, useRef } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import {
  GoogleAuthProvider,
  FacebookAuthProvider,
  signInWithPopup,
  signInAnonymously
} from 'firebase/auth';
import emailjs from 'emailjs-com';
import { auth, db } from '../lib/firebase';
import { doc, setDoc } from 'firebase/firestore';
import { useAuth } from '../contexts/AuthContext';
import Cabecalho from '../components/Cabecalho';
import BotaoSocial from '../components/BotaoSocial';
import Divisor from '../components/Divisor';
import InputMask from 'react-input-mask';
import { toast } from 'react-hot-toast';
import { sendVerificationCode, verifyCode as apiVerifyCode } from '../services/twilioService';

const EMAILJS_SERVICE_ID  = import.meta.env.VITE_EMAILJS_SERVICE_ID_OTP;
const EMAILJS_TEMPLATE_ID = import.meta.env.VITE_EMAILJS_TEMPLATE_ID_OTP;
const EMAILJS_PUBLIC_KEY  = import.meta.env.VITE_EMAILJS_PUBLIC_KEY;
const OTP_EXPIRY_MS = 5 * 60 * 1000; // 5 minutos

/* ─────────────────────────────────────────────
   Componente de input OTP com caixas individuais
───────────────────────────────────────────── */
function OtpInput({ onChange, disabled }) {
  const [digits, setDigits] = useState(['', '', '', '', '', '']);
  const refs = useRef([]);

  const update = (newDigits) => {
    setDigits(newDigits);
    onChange(newDigits.join(''));
  };

  const handleChange = (idx, val) => {
    if (!/^\d*$/.test(val)) return;
    const d = [...digits];
    d[idx] = val.slice(-1);
    update(d);
    if (val && idx < 5) refs.current[idx + 1]?.focus();
  };

  const handleKeyDown = (idx, e) => {
    if (e.key === 'Backspace') {
      if (digits[idx]) {
        const d = [...digits];
        d[idx] = '';
        update(d);
      } else if (idx > 0) {
        refs.current[idx - 1]?.focus();
      }
    }
  };

  const handlePaste = (e) => {
    e.preventDefault();
    const pasted = e.clipboardData.getData('text').replace(/\D/g, '').slice(0, 6);
    const d = pasted.split('').concat(Array(6).fill('')).slice(0, 6);
    update(d);
    refs.current[Math.min(pasted.length, 5)]?.focus();
  };

  return (
    <div style={{ display: 'flex', gap: '10px', justifyContent: 'center' }}>
      {digits.map((digit, idx) => (
        <input
          key={idx}
          ref={el => (refs.current[idx] = el)}
          type="text"
          inputMode="numeric"
          maxLength={1}
          value={digit}
          onChange={e => handleChange(idx, e.target.value)}
          onKeyDown={e => handleKeyDown(idx, e)}
          onPaste={handlePaste}
          disabled={disabled}
          style={{
            width: '48px',
            height: '58px',
            textAlign: 'center',
            fontSize: '26px',
            fontWeight: '700',
            border: digit ? '2px solid #e01e37' : '2px solid #d1d5db',
            borderRadius: '12px',
            outline: 'none',
            transition: 'border-color 0.2s, background 0.2s, transform 0.1s',
            background: digit ? '#fff1f2' : '#f9fafb',
            color: '#1f2937',
            cursor: disabled ? 'not-allowed' : 'text',
            transform: digit ? 'scale(1.05)' : 'scale(1)',
            boxShadow: digit ? '0 0 0 3px rgba(224,30,55,0.15)' : 'none',
          }}
        />
      ))}
    </div>
  );
}

/* ─────────────────────────────────────────────
   Página de Login
───────────────────────────────────────────── */
function PaginaLogin() {
  const navegacao = useNavigate();
  const { estaAutenticado } = useAuth();

  const [metodoLogin, setMetodoLogin]         = useState('email');
  const [email, setEmail]                     = useState('');
  const [telefone, setTelefone]               = useState('');
  const [codigo, setCodigo]                   = useState('');
  const [mostrarCodigo, setMostrarCodigo]     = useState(false);
  const [carregando, setCarregando]           = useState(false);
  const [erro, setErro]                       = useState(null);
  const [ultimoEnvio, setUltimoEnvio]         = useState(0);
  const [otpGerado, setOtpGerado]             = useState('');
  const [otpExpiry, setOtpExpiry]             = useState(null);
  const [tempoRestante, setTempoRestante]     = useState(0);
  const [reenvioDisponivel, setReenvioDisponivel] = useState(false);

  useEffect(() => {
    if (estaAutenticado) navegacao('/home', { replace: true });
  }, [estaAutenticado, navegacao]);

  // Timer de expiração do OTP
  useEffect(() => {
    if (!otpExpiry) return;
    const interval = setInterval(() => {
      const restante = Math.max(0, otpExpiry - Date.now());
      setTempoRestante(Math.ceil(restante / 1000));
      if (restante === 0) {
        clearInterval(interval);
        setReenvioDisponivel(true);
      }
    }, 500);
    return () => clearInterval(interval);
  }, [otpExpiry]);

  const gerarOtp = () => Math.floor(100000 + Math.random() * 900000).toString();

  const formatarTempo = (segundos) => {
    const m = Math.floor(segundos / 60);
    const s = segundos % 60;
    return `${m}:${s.toString().padStart(2, '0')}`;
  };

  const traduzirErroFirebase = (error) => {
    switch (error?.code) {
      case 'auth/invalid-phone-number':   return 'Número de telefone inválido.';
      case 'auth/invalid-verification-code': return 'Código inválido. Por favor, tente novamente.';
      case 'auth/code-expired':           return 'O código expirou. Solicite um novo.';
      case 'auth/too-many-requests':      return 'Muitas tentativas. Tente mais tarde.';
      case 'auth/popup-closed-by-user':   return 'A janela de login foi fechada.';
      case 'auth/account-exists-with-different-credential': return 'Já existe conta com outro método de login.';
      case 'auth/unauthorized-domain':    return 'Domínio não autorizado no Firebase.';
      default: return error?.message || 'Ocorreu um erro. Tente novamente.';
    }
  };

  const formatarTelefoneFirebase = (tel) => {
    const nums = tel.replace(/\D/g, '');
    if (nums.length === 10 || nums.length === 11) return `+55${nums}`;
    if (nums.startsWith('55') && (nums.length === 12 || nums.length === 13)) return `+${nums}`;
    return null;
  };

  const enviarOtpEmail = async (destino) => {
    const otp = gerarOtp();
    await emailjs.send(
      EMAILJS_SERVICE_ID,
      EMAILJS_TEMPLATE_ID,
      { to_email: destino, otp_code: otp },
      EMAILJS_PUBLIC_KEY
    );
    return otp;
  };

  const enviarCodigo = async (e) => {
    e.preventDefault();
    setErro(null);

    const agora = Date.now();
    if (agora - ultimoEnvio < 60000) {
      const seg = Math.ceil((60000 - (agora - ultimoEnvio)) / 1000);
      setErro(`Aguarde ${seg}s antes de solicitar novo código.`);
      return;
    }

    setCarregando(true);
    try {
      if (metodoLogin === 'email') {
        const otp = await enviarOtpEmail(email);
        setOtpGerado(otp);
        setOtpExpiry(Date.now() + OTP_EXPIRY_MS);
        setReenvioDisponivel(false);
        setCodigo('');
        setMostrarCodigo(true);
        toast.success('Código enviado para seu e-mail!');
      } else {
        const numeroFormatado = formatarTelefoneFirebase(telefone);
        if (!numeroFormatado) {
          setErro('Digite um telefone válido com DDD.');
          setCarregando(false);
          return;
        }
        const response = await sendVerificationCode(numeroFormatado);
        if (response.mock) {
          toast.success('[MOCK] Código padrão: 123456', { duration: 5000 });
        } else {
          toast.success('Código enviado para seu telefone!');
        }
        setMostrarCodigo(true);
        setCodigo('');
      }
      setUltimoEnvio(Date.now());
    } catch (err) {
      console.error('Erro ao enviar código:', err);
      setErro('Falha ao enviar código. Verifique o e-mail e tente novamente.');
    } finally {
      setCarregando(false);
    }
  };

  const verificarCodigo = async (e) => {
    e.preventDefault();
    setErro(null);
    setCarregando(true);

    try {
      if (metodoLogin === 'email') {
        if (Date.now() > otpExpiry) {
          setErro('Código expirado. Solicite um novo código.');
          setCarregando(false);
          return;
        }
        if (codigo.trim() !== otpGerado) {
          setErro('Código incorreto. Verifique e tente novamente.');
          setCarregando(false);
          return;
        }
        await signInAnonymously(auth);
        const user = auth.currentUser;
        if (user) {
          await setDoc(doc(db, 'usuarios', user.uid), {
            uid: user.uid,
            email: email,
            metodo: 'otp_email',
            updatedAt: new Date().toISOString()
          }, { merge: true });
        }
        toast.success('Login realizado com sucesso!');
        navegacao('/home', { replace: true });
      } else {
        const numeroFormatado = formatarTelefoneFirebase(telefone);
        const data = await apiVerifyCode(numeroFormatado, codigo);
        if (data.valid) {
          toast.success('Autenticação aprovada!');
          await signInAnonymously(auth);
          const user = auth.currentUser;
          if (user) {
            await setDoc(doc(db, 'usuarios', user.uid), {
              uid: user.uid,
              telefone: numeroFormatado,
              metodo: 'otp_telefone',
              updatedAt: new Date().toISOString()
            }, { merge: true });
          }
          navegacao('/home', { replace: true });
        } else {
          setErro('Código inválido ou expirado.');
        }
      }
    } catch (err) {
      console.error('Erro ao verificar código:', err);
      setErro(err.message || 'Falha ao verificar código.');
    } finally {
      setCarregando(false);
    }
  };

  const reenviarCodigo = async () => {
    setErro(null);
    setCarregando(true);
    try {
      const otp = await enviarOtpEmail(email);
      setOtpGerado(otp);
      setOtpExpiry(Date.now() + OTP_EXPIRY_MS);
      setReenvioDisponivel(false);
      setCodigo('');
      setUltimoEnvio(Date.now());
      toast.success('Novo código enviado!');
    } catch (err) {
      setErro('Falha ao reenviar código.');
    } finally {
      setCarregando(false);
    }
  };

  const aoLoginSocial = async (provedor) => {
    setCarregando(true);
    setErro(null);
    try {
      const provider = provedor === 'Google' ? new GoogleAuthProvider() : new FacebookAuthProvider();
      await signInWithPopup(auth, provider);
      navegacao('/home', { replace: true });
    } catch (err) {
      console.error('Erro de login social:', err);
      setErro(`Erro ao conectar com ${provedor}. ${traduzirErroFirebase(err)}`);
    } finally {
      setCarregando(false);
    }
  };

  const selecionarMetodo = (metodo) => {
    setMetodoLogin(metodo);
    setMostrarCodigo(false);
    setCodigo('');
    setErro(null);
    setOtpGerado('');
    setOtpExpiry(null);
  };

  /* ── Tela de verificação do código (e-mail) ── */
  if (mostrarCodigo && metodoLogin === 'email') {
    return (
      <div className="min-h-screen bg-gray-50 flex flex-col justify-center py-12 sm:px-6 lg:px-8">
        <Cabecalho />
        <div className="mt-8 sm:mx-auto sm:w-full sm:max-w-md">
          <div
            className="bg-white py-10 px-6 shadow-lg sm:rounded-2xl sm:px-10"
            style={{ borderTop: '4px solid #e01e37' }}
          >
            {/* Ícone */}
            <div style={{ textAlign: 'center', marginBottom: '20px' }}>
              <div style={{
                display: 'inline-flex',
                alignItems: 'center',
                justifyContent: 'center',
                width: '72px',
                height: '72px',
                borderRadius: '50%',
                background: 'linear-gradient(135deg, #fff1f2 0%, #ffe4e6 100%)',
                marginBottom: '12px',
              }}>
                <span style={{ fontSize: '32px' }}>📧</span>
              </div>
              <h2 style={{ fontSize: '22px', fontWeight: '700', color: '#111827', margin: 0 }}>
                Verifique seu e-mail
              </h2>
              <p style={{ fontSize: '14px', color: '#6b7280', marginTop: '8px' }}>
                Enviamos um código de 6 dígitos para
              </p>
              <p style={{
                fontSize: '14px',
                fontWeight: '600',
                color: '#e01e37',
                marginTop: '4px',
                wordBreak: 'break-all',
              }}>
                {email}
              </p>
            </div>

            {/* Erro */}
            {erro && (
              <div style={{
                marginBottom: '16px',
                padding: '12px 16px',
                background: '#fef2f2',
                border: '1px solid #fca5a5',
                borderRadius: '10px',
                color: '#dc2626',
                fontSize: '14px',
                textAlign: 'center',
              }}>
                {erro}
              </div>
            )}

            <form onSubmit={verificarCodigo}>
              {/* Inputs OTP */}
              <div style={{ marginBottom: '24px' }}>
                <OtpInput onChange={setCodigo} disabled={carregando} />
              </div>

              {/* Timer */}
              <div style={{ textAlign: 'center', marginBottom: '20px' }}>
                {!reenvioDisponivel ? (
                  <p style={{ fontSize: '13px', color: '#9ca3af' }}>
                    Código expira em{' '}
                    <span style={{ color: tempoRestante < 60 ? '#e01e37' : '#4b5563', fontWeight: '600' }}>
                      {formatarTempo(tempoRestante)}
                    </span>
                  </p>
                ) : (
                  <p style={{ fontSize: '13px', color: '#9ca3af' }}>
                    Código expirado.{' '}
                    <button
                      type="button"
                      onClick={reenviarCodigo}
                      disabled={carregando}
                      style={{
                        background: 'none',
                        border: 'none',
                        color: '#e01e37',
                        fontWeight: '600',
                        cursor: 'pointer',
                        fontSize: '13px',
                        textDecoration: 'underline',
                      }}
                    >
                      Reenviar código
                    </button>
                  </p>
                )}
              </div>

              {/* Botão verificar */}
              <button
                type="submit"
                disabled={carregando || codigo.length !== 6}
                style={{
                  width: '100%',
                  padding: '13px',
                  borderRadius: '12px',
                  border: 'none',
                  background: codigo.length === 6 && !carregando
                    ? 'linear-gradient(135deg, #e01e37 0%, #c0132a 100%)'
                    : '#e5e7eb',
                  color: codigo.length === 6 && !carregando ? '#fff' : '#9ca3af',
                  fontSize: '15px',
                  fontWeight: '600',
                  cursor: codigo.length === 6 && !carregando ? 'pointer' : 'not-allowed',
                  transition: 'all 0.2s',
                  marginBottom: '10px',
                }}
              >
                {carregando ? 'Verificando...' : 'Confirmar código'}
              </button>

              {/* Botão voltar */}
              <button
                type="button"
                onClick={() => {
                  setMostrarCodigo(false);
                  setCodigo('');
                  setErro(null);
                  setOtpGerado('');
                  setOtpExpiry(null);
                }}
                disabled={carregando}
                style={{
                  width: '100%',
                  padding: '12px',
                  borderRadius: '12px',
                  border: '1.5px solid #e5e7eb',
                  background: 'white',
                  color: '#6b7280',
                  fontSize: '14px',
                  fontWeight: '500',
                  cursor: 'pointer',
                  transition: 'all 0.2s',
                }}
              >
                ← Usar outro e-mail
              </button>
            </form>

            {/* Dica */}
            <p style={{ textAlign: 'center', fontSize: '12px', color: '#9ca3af', marginTop: '20px' }}>
              Não encontrou? Verifique sua pasta de spam.
            </p>
          </div>
        </div>
      </div>
    );
  }

  /* ── Tela de verificação por TELEFONE ── */
  if (mostrarCodigo && metodoLogin === 'telefone') {
    return (
      <div className="min-h-screen bg-gray-50 flex flex-col justify-center py-12 sm:px-6 lg:px-8">
        <Cabecalho />
        <div className="mt-8 sm:mx-auto sm:w-full sm:max-w-md">
          <div className="bg-white py-8 px-4 shadow sm:rounded-lg sm:px-10">
            <h2 className="text-center text-xl font-bold text-gray-800 mb-6">
              Digite o código recebido
            </h2>
            {erro && (
              <div className="mb-4 p-3 bg-red-100 border border-red-400 text-red-700 rounded">
                {erro}
              </div>
            )}
            <form onSubmit={verificarCodigo} className="space-y-4">
              <div>
                <label htmlFor="codigo" className="block text-sm font-medium text-gray-700">
                  Código de verificação
                </label>
                <div className="mt-1">
                  <input
                    id="codigo"
                    name="codigo"
                    type="text"
                    required
                    value={codigo}
                    onChange={(e) => setCodigo(e.target.value.replace(/\D/g, ''))}
                    disabled={carregando}
                    maxLength={6}
                    className="appearance-none block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm placeholder-gray-400 focus:outline-none focus:ring-ifood-red focus:border-ifood-red sm:text-sm disabled:opacity-50"
                    placeholder="Digite o código de 6 dígitos"
                  />
                </div>
              </div>
              <button
                type="submit"
                disabled={carregando || codigo.length !== 6}
                className="w-full flex justify-center py-2 px-4 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-ifood-red hover:bg-red-700 focus:outline-none disabled:opacity-50"
              >
                {carregando ? 'Verificando...' : 'Verificar código'}
              </button>
              <button
                type="button"
                onClick={() => { setMostrarCodigo(false); setCodigo(''); setErro(null); }}
                disabled={carregando}
                className="w-full flex justify-center py-2 px-4 border border-gray-300 rounded-md shadow-sm text-sm font-medium text-gray-700 bg-white hover:bg-gray-50 focus:outline-none disabled:opacity-50"
              >
                Voltar
              </button>
            </form>
          </div>
        </div>
      </div>
    );
  }

  /* ── Tela principal de login ── */
  return (
    <div className="min-h-screen bg-gray-50 flex flex-col justify-center py-12 sm:px-6 lg:px-8">
      <Cabecalho />

      <div className="mt-8 sm:mx-auto sm:w-full sm:max-w-md">
        <div className="bg-white py-8 px-4 shadow sm:rounded-lg sm:px-10">
          {erro && (
            <div className="mb-4 p-3 bg-red-100 border border-red-400 text-red-700 rounded">
              {erro}
            </div>
          )}

          <div className="space-y-6">
            <div className="flex justify-center space-x-4">
              <BotaoSocial provedor="Google"   aoClicar={() => aoLoginSocial('Google')}   desabilitado={carregando} />
              <BotaoSocial provedor="Facebook" aoClicar={() => aoLoginSocial('Facebook')} desabilitado={carregando} />
            </div>

            <Divisor texto="ou continue com" />

            <div className="flex justify-center space-x-4">
              <button
                type="button"
                onClick={() => selecionarMetodo('email')}
                className={`px-4 py-2 text-sm font-medium rounded-md ${
                  metodoLogin === 'email' ? 'bg-ifood-red text-white' : 'text-gray-700 bg-gray-100 hover:bg-gray-200'
                }`}
              >
                E-mail
              </button>
              <button
                type="button"
                onClick={() => selecionarMetodo('telefone')}
                className={`px-4 py-2 text-sm font-medium rounded-md ${
                  metodoLogin === 'telefone' ? 'bg-ifood-red text-white' : 'text-gray-700 bg-gray-100 hover:bg-gray-200'
                }`}
              >
                Telefone
              </button>
            </div>

            <form onSubmit={enviarCodigo}>
              {metodoLogin === 'email' ? (
                <div>
                  <label htmlFor="email" className="block text-sm font-medium text-gray-700">
                    E-mail
                  </label>
                  <div className="mt-1">
                    <input
                      id="email"
                      name="email"
                      type="email"
                      autoComplete="email"
                      required
                      value={email}
                      onChange={(e) => setEmail(e.target.value)}
                      disabled={carregando}
                      className="appearance-none block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm placeholder-gray-400 focus:outline-none focus:ring-ifood-red focus:border-ifood-red sm:text-sm disabled:opacity-50"
                      placeholder="seu@email.com"
                    />
                  </div>
                </div>
              ) : (
                <div>
                  <label htmlFor="telefone" className="block text-sm font-medium text-gray-700">
                    Telefone
                  </label>
                  <div className="mt-1">
                    <InputMask
                      mask="(99) 99999-9999"
                      id="telefone"
                      name="telefone"
                      type="tel"
                      autoComplete="tel"
                      required
                      value={telefone}
                      onChange={(e) => setTelefone(e.target.value)}
                      disabled={carregando}
                      className="appearance-none block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm placeholder-gray-400 focus:outline-none focus:ring-ifood-red focus:border-ifood-red sm:text-sm disabled:opacity-50"
                      placeholder="(11) 99999-9999"
                    />
                  </div>
                </div>
              )}

              <div className="mt-6">
                <button
                  type="submit"
                  disabled={carregando}
                  className="w-full flex justify-center py-2 px-4 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-ifood-red hover:bg-red-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-ifood-red disabled:opacity-50"
                >
                  {carregando ? 'Enviando...' : metodoLogin === 'email' ? 'Enviar código' : 'Enviar código'}
                </button>
              </div>
            </form>
          </div>

          <p className="text-center mt-4 text-sm text-gray-600">
            Você não tem cadastro?{' '}
            <Link to="/cadastro" className="text-ifood-red font-semibold hover:underline">
              Faça agora mesmo!
            </Link>
          </p>
        </div>
      </div>
    </div>
  );
}

export default PaginaLogin;