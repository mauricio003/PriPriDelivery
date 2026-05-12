import React, { useState, useEffect } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import { Truck, Check, ChevronRight, MessageSquare, HelpCircle } from 'lucide-react';
import { useAuth } from '../contexts/AuthContext';

function AcompanhamentoPedido() {
  const { usuario: user } = useAuth();
  const navegacao = useNavigate();
  const location = useLocation();
  const [statusAtual, setStatusAtual] = useState(0);
  const [pedidoConcluido, setPedidoConcluido] = useState(false);
  const [mostrarModalVerificacao, setMostrarModalVerificacao] = useState(false);
  const [codigoInserido, setCodigoInserido] = useState('');
  const [erroVerificacao, setErroVerificacao] = useState('');

  const restauranteNome = location.state?.restauranteNome || 'Burger House';
  const pedidoId = location.state?.pedidoId || '12345';

  const statusPedido = [
    {
      titulo: "Pedido confirmado",
      subtitulo: "O restaurante aceitou seu pedido",
    },
    {
      titulo: "Em preparo",
      subtitulo: "Restaurante preparando seu pedido",
    },
    {
      titulo: "Encontrando motorista",
      subtitulo: "Aguardando retirada pelo entregador",
    },
    {
      titulo: "Saiu para entrega",
      subtitulo: "Pedido a caminho do seu endereço",
    },
    {
      titulo: "Entregue",
      subtitulo: "Pedido finalizado com sucesso",
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
            setStatusAtual(data.statusIndex);

            // Se o pedido chegou (index 4), abre o modal de verificação
            if (data.statusIndex === 4 && !pedidoConcluido) {
              setMostrarModalVerificacao(true);
            }
          }
        }
      } catch (error) {
        console.error('Erro ao buscar status do pedido:', error);
      }
    };

    const intervalo = setInterval(buscarStatus, 3000);
    buscarStatus();

    return () => clearInterval(intervalo);
  }, [location.state?.pedidoId, statusAtual, pedidoConcluido, navegacao]);

  const verificarCodigo = () => {
    const codigoCorreto = location.state?.codigoVerificacao;
    
    if (codigoInserido === codigoCorreto) {
      setPedidoConcluido(true);
      setMostrarModalVerificacao(false);
      
      setTimeout(() => {
        navegacao('/home');
      }, 5000);
    } else {
      setErroVerificacao('Código incorreto. Verifique seu e-mail.');
    }
  };

  return (
    <div className="bg-[#f7f7f7] min-h-screen font-['Inter',-apple-system,BlinkMacSystemFont,'Segoe_UI',Roboto,sans-serif]">
      <div className="max-w-[520px] mx-auto bg-white min-h-screen pb-[110px] shadow-sm">
        
        {/* Header */}
        <div className="p-5 px-4 border-b border-gray-100">
          <h1 className="text-xl font-bold text-[#1f1f1f]">Acompanhar pedido</h1>
          <p className="text-sm text-[#757575] mt-1">
            {pedidoConcluido ? 'Pedido finalizado' : 'Seu pedido chegará em breve'}
          </p>
        </div>

        {/* Info Restaurante */}
        <div className="p-4 border-b-[8px] border-[#f7f7f7]">
          <div className="text-[15px] font-semibold text-[#1f1f1f]">
            {restauranteNome}
          </div>
          <div className="text-xs text-[#8e8e8e] mt-1">
            Pedido #{pedidoId}
          </div>
        </div>

        {/* Timeline de Status */}
        <div className="p-5 px-4">
          {statusPedido.map((step, index) => {
            const isCompleted = index < statusAtual;
            const isCurrent = index === statusAtual;
            const isPending = index > statusAtual;

            return (
              <div key={index} className={`flex gap-3 ${index !== statusPedido.length - 1 ? 'mb-[18px]' : ''}`}>
                {/* Dot */}
                <div className="flex flex-col items-center">
                  <div 
                    className={`rounded-full mt-1.5 transition-all duration-300 ${
                      isCurrent 
                        ? 'w-3 h-3 bg-[#ea1d2c] shadow-[0_0_0_4px_rgba(234,29,44,0.15)]' 
                        : isCompleted 
                          ? 'w-2.5 h-2.5 bg-[#ea1d2c]' 
                          : 'w-2.5 h-2.5 bg-[#ddd]'
                    }`}
                  />
                  {index !== statusPedido.length - 1 && (
                    <div className={`w-[2px] h-full mt-1 ${isCompleted ? 'bg-[#ea1d2c]' : 'bg-[#eee]'}`} />
                  )}
                </div>

                {/* Content */}
                <div>
                  <div className={`text-sm font-semibold transition-colors duration-300 ${
                    isPending ? 'text-[#bdbdbd]' : 'text-[#1f1f1f]'
                  }`}>
                    {step.titulo}
                  </div>
                  <div className={`text-xs mt-0.5 transition-colors duration-300 ${
                    isPending ? 'text-[#c7c7c7]' : 'text-[#757575]'
                  }`}>
                    {step.subtitulo}
                  </div>
                </div>
              </div>
            );
          })}
        </div>

        {/* Links de Suporte */}
        <div className="border-t-[8px] border-[#f7f7f7]">
          <button className="w-full p-4 flex justify-between items-center border-b border-gray-100 hover:bg-gray-50 transition-colors">
            <div className="flex items-center gap-3">
              <MessageSquare className="w-5 h-5 text-gray-400" />
              <span className="text-sm text-[#1f1f1f]">Falar com a loja</span>
            </div>
            <span className="text-[#ea1d2c] text-sm font-semibold">Abrir</span>
          </button>

          <button className="w-full p-4 flex justify-between items-center hover:bg-gray-50 transition-colors">
            <div className="flex items-center gap-3">
              <HelpCircle className="w-5 h-5 text-gray-400" />
              <span className="text-sm text-[#1f1f1f]">Ajuda com o pedido</span>
            </div>
            <span className="text-[#ea1d2c] text-sm font-semibold">Ver opções</span>
          </button>
        </div>

        {/* Feedback Sucesso */}
        {pedidoConcluido && (
          <div className="mt-8 px-4 text-center animate-in fade-in zoom-in duration-500">
            <div className="w-16 h-16 bg-green-100 text-green-600 rounded-full flex items-center justify-center mx-auto mb-4">
              <Check className="w-10 h-10" />
            </div>
            <p className="text-green-600 text-xl font-bold">Pedido entregue!</p>
            <p className="text-sm text-gray-500 mt-2">Obrigado por pedir na PriPriDelivery!</p>
          </div>
        )}
      </div>

      {/* Botão Fixo Suporte */}
      <div className="fixed bottom-0 w-full max-w-[520px] left-1/2 -translate-x-1/2 bg-white border-t border-gray-100 p-3 px-4 z-40">
        <button className="w-full bg-[#ea1d2c] text-white p-4 rounded-xl text-[15px] font-semibold hover:bg-red-700 transition-colors shadow-lg active:scale-[0.98]">
          Falar com suporte
        </button>
      </div>

      {/* Modal de Verificação (OTP) */}
      {mostrarModalVerificacao && (
        <div className="fixed inset-0 bg-black/60 flex items-center justify-center z-50 p-4 backdrop-blur-sm">
          <div className="bg-white rounded-2xl shadow-2xl max-w-sm w-full p-8 animate-in zoom-in duration-300">
            <div className="text-center">
              <div className="w-16 h-16 bg-red-50 text-[#ea1d2c] rounded-full flex items-center justify-center mx-auto mb-4">
                <Truck className="w-8 h-8" />
              </div>
              <h2 className="text-2xl font-bold text-[#1f1f1f] mb-2">Seu pedido chegou!</h2>
              <p className="text-[#757575] mb-6 text-sm">
                Por favor, insira o código enviado para o seu e-mail para finalizar a entrega.
              </p>
              
              <input
                type="text"
                maxLength={6}
                value={codigoInserido}
                onChange={(e) => setCodigoInserido(e.target.value.replace(/\D/g, ''))}
                placeholder="000000"
                className="w-full text-center text-3xl tracking-widest font-bold py-4 border-2 border-gray-100 rounded-xl focus:border-[#ea1d2c] focus:outline-none mb-4 transition-colors"
              />

              {erroVerificacao && (
                <p className="text-red-500 text-xs mb-4 font-medium">{erroVerificacao}</p>
              )}

              <button
                onClick={verificarCodigo}
                disabled={codigoInserido.length !== 6}
                className="w-full py-4 bg-[#ea1d2c] text-white font-bold rounded-xl hover:bg-red-700 transition-colors disabled:opacity-50 disabled:cursor-not-allowed shadow-md"
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