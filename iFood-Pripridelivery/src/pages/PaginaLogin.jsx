import React, { useState, useEffect } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import {
  GoogleAuthProvider,
  FacebookAuthProvider,
  signInWithPopup,
  RecaptchaVerifier,
  signInWithPhoneNumber,
  sendSignInLinkToEmail,
  isSignInWithEmailLink,
  signInWithEmailLink
} from 'firebase/auth';
import { auth } from '../lib/firebase';
import { useAuth } from '../contexts/AuthContext';
import Cabecalho from '../components/Cabecalho';
import InputMask from 'react-input-mask';

function PaginaLogin() {
  const navegacao = useNavigate();
  const { estaAutenticado } = useAuth();

  const [metodoLogin, setMetodoLogin] = useState('email');
  const [email, setEmail] = useState('');
  const [telefone, setTelefone] = useState('');
  const [codigo, setCodigo] = useState('');
  const [mostrarCodigo, setMostrarCodigo] = useState(false);
  const [carregando, setCarregando] = useState(false);
  const [erro, setErro] = useState(null);
  const [ultimoEnvio, setUltimoEnvio] = useState(0);
  const [confirmationResult, setConfirmationResult] = useState(null);

  useEffect(() => {
    if (estaAutenticado) {
      navegacao('/home', { replace: true });
    }
  }, [estaAutenticado, navegacao]);

  useEffect(() => {
    if (isSignInWithEmailLink(auth, window.location.href)) {
      let emailSalvo = window.localStorage.getItem('emailParaLogin');

      if (!emailSalvo) {
        emailSalvo = window.prompt('Por favor, confirme seu e-mail');
      }

      if (!emailSalvo) {
        setErro('Não foi possível confirmar o e-mail para login.');
        return;
      }

      signInWithEmailLink(auth, emailSalvo, window.location.href)
        .then(() => {
          window.localStorage.removeItem('emailParaLogin');
          navegacao('/home', { replace: true });
        })
        .catch((error) => {
          console.error('Erro ao fazer login com email:', error);
          setErro(traduzirErroFirebase(error));
        });
    }
  }, [navegacao]);

  useEffect(() => {
    return () => {
      if (window.recaptchaVerifier) {
        try {
          window.recaptchaVerifier.clear();
        } catch (e) {}
        window.recaptchaVerifier = null;
      }
    };
  }, []);

  const traduzirErroFirebase = (error) => {
    switch (error?.code) {
      case 'auth/invalid-phone-number':
        return 'Número de telefone inválido.';
      case 'auth/invalid-verification-code':
        return 'Código inválido. Por favor, tente novamente.';
      case 'auth/missing-verification-code':
        return 'Informe o código de verificação.';
      case 'auth/code-expired':
        return 'O código expirou. Solicite um novo código.';
      case 'auth/too-many-requests':
        return 'Muitas tentativas. Tente novamente mais tarde.';
      case 'auth/popup-closed-by-user':
        return 'A janela de login foi fechada antes da conclusão.';
      case 'auth/cancelled-popup-request':
        return 'Solicitação de login cancelada.';
      case 'auth/account-exists-with-different-credential':
        return 'Já existe uma conta com outro método de login para este e-mail.';
      case 'auth/unauthorized-domain':
        return 'Este domínio não está autorizado no Firebase Authentication.';
      case 'auth/invalid-email':
        return 'E-mail inválido.';
      case 'auth/invalid-action-code':
        return 'O link de login é inválido ou expirou.';
      default:
        return error?.message || 'Ocorreu um erro. Por favor, tente novamente.';
    }
  };

  const formatarTelefoneFirebase = (telefone) => {
    const apenasNumeros = telefone.replace(/\D/g, '');

    if (apenasNumeros.length === 10 || apenasNumeros.length === 11) {
      return `+55${apenasNumeros}`;
    }

    if (
      apenasNumeros.startsWith('55') &&
      (apenasNumeros.length === 12 || apenasNumeros.length === 13)
    ) {
      return `+${apenasNumeros}`;
    }

    return null;
  };

  const criarRecaptcha = async () => {
    if (window.recaptchaVerifier) {
      try {
        window.recaptchaVerifier.clear();
      } catch (e) {}
      window.recaptchaVerifier = null;
    }

    window.recaptchaVerifier = new RecaptchaVerifier(auth, 'recaptcha-container', {
      size: 'invisible'
    });

    await window.recaptchaVerifier.render();
  };

  const enviarCodigo = async (e) => {
    e.preventDefault();
    setErro(null);

    const agora = Date.now();
    if (agora - ultimoEnvio < 60000) {
      const segundosRestantes = Math.ceil((60000 - (agora - ultimoEnvio)) / 1000);
      setErro(`Por favor, aguarde ${segundosRestantes} segundos antes de solicitar um novo código.`);
      return;
    }

    setCarregando(true);

    try {
      if (metodoLogin === 'email') {
        const actionCodeSettings = {
          url: `${window.location.origin}/login`,
          handleCodeInApp: true
        };

        await sendSignInLinkToEmail(auth, email, actionCodeSettings);
        window.localStorage.setItem('emailParaLogin', email);
        alert('Link de login enviado para seu e-mail.');
      } else {
        const numeroFormatado = formatarTelefoneFirebase(telefone);

        if (!numeroFormatado) {
          setErro('Digite um telefone válido com DDD.');
          setCarregando(false);
          return;
        }

        await criarRecaptcha();

        const result = await signInWithPhoneNumber(
          auth,
          numeroFormatado,
          window.recaptchaVerifier
        );

        setConfirmationResult(result);
        setMostrarCodigo(true);
        setCodigo('');
        alert('Código de verificação enviado para seu telefone.');
      }

      setUltimoEnvio(Date.now());
    } catch (erro) {
      console.error('Erro ao enviar código:', erro);
      setErro(traduzirErroFirebase(erro));

      try {
        if (window.recaptchaVerifier) {
          const widgetId = await window.recaptchaVerifier.render();
          window.grecaptcha?.reset(widgetId);
        }
      } catch (e) {}
    } finally {
      setCarregando(false);
    }
  };

  const verificarCodigo = async (e) => {
    e.preventDefault();
    setErro(null);
    setCarregando(true);

    try {
      if (!confirmationResult) {
        throw new Error('Nenhuma verificação foi iniciada. Solicite um novo código.');
      }

      await confirmationResult.confirm(codigo);
      navegacao('/home', { replace: true });
    } catch (erro) {
      console.error('Erro ao verificar código:', erro);
      setErro(traduzirErroFirebase(erro));
    } finally {
      setCarregando(false);
    }
  };

  const aoLoginSocial = async (provedor) => {
    setCarregando(true);
    setErro(null);

    try {
      let provider;

      if (provedor === 'Google') {
        provider = new GoogleAuthProvider();
      } else if (provedor === 'Facebook') {
        provider = new FacebookAuthProvider();
      } else {
        throw new Error('Provedor inválido.');
      }

      await signInWithPopup(auth, provider);
      navegacao('/home', { replace: true });
    } catch (erro) {
      console.error('Erro de login social:', erro);
      setErro(`Erro ao conectar com ${provedor}. ${traduzirErroFirebase(erro)}`);
    } finally {
      setCarregando(false);
    }
  };

  const selecionarMetodo = (metodo) => {
    setMetodoLogin(metodo);
    setMostrarCodigo(false);
    setCodigo('');
    setErro(null);
    setConfirmationResult(null);
  };

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
              <button
                type="button"
                onClick={() => aoLoginSocial('Google')}
                disabled={carregando}
                className="flex-1 flex justify-center items-center px-4 py-3 border border-gray-300 rounded-md shadow-sm text-sm font-medium text-gray-700 bg-white hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-ifood-red disabled:opacity-50"
              >
                <svg className="w-5 h-5 mr-2" viewBox="0 0 24 24">
                  <path
                    fill="currentColor"
                    d="M12.545,10.239v3.821h5.445c-0.712,2.315-2.647,3.972-5.445,3.972c-3.332,0-6.033-2.701-6.033-6.032s2.701-6.032,6.033-6.032c1.498,0,2.866,0.549,3.921,1.453l2.814-2.814C17.503,2.988,15.139,2,12.545,2C7.021,2,2.543,6.477,2.543,12s4.478,10,10.002,10c8.396,0,10.249-7.85,9.426-11.748L12.545,10.239z"
                  />
                </svg>
                Google
              </button>

              <button
                type="button"
                onClick={() => aoLoginSocial('Facebook')}
                disabled={carregando}
                className="flex-1 flex justify-center items-center px-4 py-3 border border-[#1877F2] rounded-md shadow-sm text-sm font-medium text-[#1877F2] bg-white hover:bg-blue-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-[#1877F2] disabled:opacity-50"
              >
                <svg className="w-5 h-5 mr-2" viewBox="0 0 24 24">
                  <path
                    fill="currentColor"
                    d="M24 12.073c0-6.627-5.373-12-12-12s-12 5.373-12 12c0 5.99 4.388 10.954 10.125 11.854v-8.385H7.078v-3.47h3.047V9.43c0-3.007 1.792-4.669 4.533-4.669 1.312 0 2.686.235 2.686.235v2.953H15.83c-1.491 0-1.956.925-1.956 1.874v2.25h3.328l-.532 3.47h-2.796v8.385C19.612 23.027 24 18.062 24 12.073z"
                  />
                </svg>
                Facebook
              </button>
            </div>

            <div className="relative">
              <div className="absolute inset-0 flex items-center">
                <div className="w-full border-t border-gray-300"></div>
              </div>
              <div className="relative flex justify-center text-sm">
                <span className="px-2 bg-white text-gray-500">ou continue com</span>
              </div>
            </div>

            <div className="flex justify-center space-x-4">
              <button
                type="button"
                onClick={() => selecionarMetodo('email')}
                className={`px-4 py-2 text-sm font-medium rounded-md ${
                  metodoLogin === 'email'
                    ? 'bg-ifood-red text-white'
                    : 'text-gray-700 bg-gray-100 hover:bg-gray-200'
                }`}
              >
                E-mail
              </button>

              <button
                type="button"
                onClick={() => selecionarMetodo('telefone')}
                className={`px-4 py-2 text-sm font-medium rounded-md ${
                  metodoLogin === 'telefone'
                    ? 'bg-ifood-red text-white'
                    : 'text-gray-700 bg-gray-100 hover:bg-gray-200'
                }`}
              >
                Telefone
              </button>
            </div>

            {!mostrarCodigo ? (
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
                        placeholder="Seu e-mail"
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
                    {carregando
                      ? 'Enviando...'
                      : metodoLogin === 'email'
                      ? 'Enviar link'
                      : 'Enviar código'}
                  </button>
                </div>
              </form>
            ) : (
              <form onSubmit={verificarCodigo}>
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

                <div className="mt-6 space-y-2">
                  <button
                    type="submit"
                    disabled={carregando || codigo.length !== 6}
                    className="w-full flex justify-center py-2 px-4 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-ifood-red hover:bg-red-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-ifood-red disabled:opacity-50"
                  >
                    {carregando ? 'Verificando...' : 'Verificar código'}
                  </button>

                  <button
                    type="button"
                    onClick={() => {
                      setMostrarCodigo(false);
                      setCodigo('');
                      setConfirmationResult(null);
                      setErro(null);
                    }}
                    disabled={carregando}
                    className="w-full flex justify-center py-2 px-4 border border-gray-300 rounded-md shadow-sm text-sm font-medium text-gray-700 bg-white hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-ifood-red disabled:opacity-50"
                  >
                    Voltar
                  </button>
                </div>
              </form>
            )}
          </div>

          <p className="text-center mt-4 text-sm text-gray-600">
            Você não tem cadastro?{' '}
            <Link to="/cadastro" className="text-ifood-red font-semibold hover:underline">
              Faça agora mesmo!
            </Link>
          </p>
        </div>
      </div>

      <div id="recaptcha-container"></div>
    </div>
  );
}

export default PaginaLogin;