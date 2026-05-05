import React, { useState, useEffect } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import { Check, Truck, ChefHat, Search, Store } from 'lucide-react';
import emailjs from 'emailjs-com';
import { useAuth } from '../contexts/AuthContext';

function AcompanhamentoPedido() {
  const { usuario: user, estaAutenticado, logout } = useAuth();
  const navegacao = useNavigate();
  const location = useLocation();
  const [statusAtual, setStatusAtual] = useState(0);
  const [pedidoConcluido, setPedidoConcluido] = useState(false);
  const [mostrarModalVerificacao, setMostrarModalVerificacao] = useState(false);
  const [codigoInserido, setCodigoInserido] = useState('');
  const [erroVerificacao, setErroVerificacao] = useState('');

  const statusPedido = [
    {
      texto: "O restaurante aceitou o pedido",
      icone: <Store className="w-6 h-6" />,
      cor: "text-blue-500"
    },
    {
      texto: "Pedido sendo preparado",
      icone: <ChefHat className="w-6 h-6" />,
      cor: "text-yellow-500"
    },
    {
      texto: "Encontrando motorista parceiro",
      icone: <Search className="w-6 h-6" />,
      cor: "text-purple-500"
    },
    {
      texto: "Seu motorista está indo até você",
      icone: <Truck className="w-6 h-6" />,
      cor: "text-orange-500"
    },
    {
      texto: "Seu pedido chegou",
      icone: <Check className="w-6 h-6" />,
      cor: "text-green-500"
    }
  ];

  useEffect(() => {
    if (!location.state?.pedidoId) {
      navegacao('/home');
      return;
    }

    const buscarStatus = async () => {
      try {
        const response = await fetch(`http://localhost:3001/api/order-status/${location.state.pedidoId}`);
        const data = await response.json();
        
        if (data.statusIndex !== undefined) {
          if (data.statusIndex !== statusAtual) {
            // Enviar e-mail apenas quando o status mudar
            const novoStatus = statusPedido[data.statusIndex].texto;
            
            /* 
            try {
              emailjs.send(
                import.meta.env.VITE_EMAILJS_SERVICE_ID_ORDER,
                import.meta.env.VITE_EMAILJS_TEMPLATE_ID_ORDER,
                {
                  name: user?.displayName || 'Cliente',
                  message: `Status: ${novoStatus}`,
                  time: new Date().toLocaleString(),
                  pedido_id: location.state?.pedidoId,
                  to_email: user?.email
                },
                import.meta.env.VITE_EMAILJS_PUBLIC_KEY
              )
              .then(() => console.log('E-mail enviado com sucesso!', user?.email, novoStatus))
              .catch((err) => console.error('Erro ao enviar e-mail (EmailJS):', err));
            } catch (err) {
              console.error('Erro ao chamar EmailJS:', err);
            }
            */
            
            setStatusAtual(data.statusIndex);

            // Se o pedido chegou (index 4), abre o modal de verificação
            if (data.statusIndex === 4 && !pedidoConcluido) {
              setMostrarModalVerificacao(true);
            }
          }

          // Nota: não marcamos pedidoConcluido = true automaticamente aqui mais, 
          // agora depende da verificação do código.
        }
      } catch (error) {
        console.error('Erro ao buscar status do pedido:', error);
      }
    };

    const intervalo = setInterval(buscarStatus, 3000); // Polling a cada 3 segundos
    buscarStatus(); // Chamada inicial

    return () => clearInterval(intervalo);
  }, [location.state?.pedidoId, statusAtual, user?.email, pedidoConcluido]);

  const verificarCodigo = () => {
    const codigoCorreto = location.state?.codigoVerificacao;
    
    if (codigoInserido === codigoCorreto) {
      setPedidoConcluido(true);
      setMostrarModalVerificacao(false);
      
      // Opcional: Redirecionar após sucesso
      setTimeout(() => {
        navegacao('/home');
      }, 5000);
    } else {
      setErroVerificacao('Código incorreto. Verifique seu e-mail.');
    }
  };

  return (
    <div className="min-h-screen bg-gray-50 flex flex-col">
      <nav className="bg-white shadow-sm">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex justify-between h-16">
            <div className="flex items-center">
              <img
                className="h-8 w-auto cursor-pointer"
                src="https://logodownload.org/wp-content/uploads/2017/05/ifood-logo-0.png"
                alt="iFood"
                onClick={() => navegacao('/home')}
              />
              <span className="ml-4 text-xl font-semibold text-gray-900">
                Acompanhamento do Pedido
              </span>
            </div>
          </div>
        </div>
      </nav>

      <main className="flex-1 max-w-2xl mx-auto w-full px-4 py-8">
        <div className="bg-white shadow rounded-lg p-6">
          <div className="space-y-8">
            {statusPedido.map((status, index) => (
              <div key={index} className={`flex items-start space-x-4 ${
                index > statusAtual ? 'opacity-50' : ''
              }`}>
                <div className={`flex-shrink-0 w-10 h-10 rounded-full flex items-center justify-center ${
                  index <= statusAtual ? `${status.cor} bg-opacity-20` : 'bg-gray-200'
                }`}>
                  {status.icone}
                </div>
                <div className="flex-1">
                  <p className={`text-lg font-medium ${
                    index <= statusAtual ? status.cor : 'text-gray-500'
                  }`}>
                    {status.texto}
                  </p>
                  {index <= statusAtual && (
                    <p className="text-sm text-gray-500">
                      {new Date().toLocaleTimeString()}
                    </p>
                  )}
                </div>
                {index <= statusAtual && (
                  <div className="flex-shrink-0">
                    <Check className={`w-6 h-6 ${status.cor}`} />
                  </div>
                )}
              </div>
            ))}
          </div>

          {pedidoConcluido && (
            <div className="mt-8 text-center animate-bounce">
              <div className="w-16 h-16 bg-green-100 text-green-600 rounded-full flex items-center justify-center mx-auto mb-4">
                <Check className="w-10 h-10" />
              </div>
              <p className="text-green-600 text-xl font-bold">
                Pedido entregue com sucesso!
              </p>
              <p className="text-sm text-gray-500 mt-2">
                Obrigado por comprar com a PriPriDelivery!
              </p>
            </div>
          )}
        </div>
      </main>

      {/* Modal de Verificação */}
      {mostrarModalVerificacao && (
        <div className="fixed inset-0 bg-black bg-opacity-60 flex items-center justify-center z-50 p-4">
          <div className="bg-white rounded-2xl shadow-2xl max-w-sm w-full p-8 transform transition-all scale-100">
            <div className="text-center">
              <div className="w-16 h-16 bg-ifood-red bg-opacity-10 text-ifood-red rounded-full flex items-center justify-center mx-auto mb-4">
                <Truck className="w-8 h-8" />
              </div>
              <h2 className="text-2xl font-bold text-gray-900 mb-2">Seu pedido chegou!</h2>
              <p className="text-gray-600 mb-6">
                Por favor, insira o código de 6 dígitos enviado para o seu e-mail para finalizar a entrega.
              </p>
              
              <input
                type="text"
                maxLength={6}
                value={codigoInserido}
                onChange={(e) => setCodigoInserido(e.target.value.replace(/\D/g, ''))}
                placeholder="000000"
                className="w-full text-center text-3xl tracking-widest font-bold py-3 border-2 border-gray-200 rounded-xl focus:border-ifood-red focus:outline-none mb-4"
              />

              {erroVerificacao && (
                <p className="text-red-500 text-sm mb-4">{erroVerificacao}</p>
              )}

              <button
                onClick={verificarCodigo}
                disabled={codigoInserido.length !== 6}
                className="w-full py-4 bg-ifood-red text-white font-bold rounded-xl hover:bg-red-700 transition-colors disabled:opacity-50 disabled:cursor-not-allowed"
              >
                Confirmar Entrega
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}

export default AcompanhamentoPedido;