import React, { useState, useRef, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { MessageCircle, X, ChevronRight, Loader2, Bot, UtensilsCrossed } from 'lucide-react';

const questions = [
  {
    title: "Qual é o seu orçamento para esta refeição?",
    options: [
      { label: "Econômico (até R$ 25)", value: "Econômico" },
      { label: "Moderado (R$ 25-45)", value: "Moderado" },
      { label: "Premium (R$ 45-70)", value: "Premium" },
      { label: "Sem limite definido", value: "Sem limite" },
    ]
  },
  {
    title: "Como você prefere o preparo da comida?",
    options: [
      { label: "Fresco/cru (sushi, saladas)", value: "Fresco/cru" },
      { label: "Grelhado/assado (leve)", value: "Grelhado/assado" },
      { label: "Frito/empanado (crocante)", value: "Frito/empanado" },
      { label: "Cozido/ensopado (sopas)", value: "Cozido/ensopado" },
    ]
  },
  {
    title: "Que tipo de refeição você está buscando?",
    options: [
      { label: "Leve e saudável", value: "Leve e saudável" },
      { label: "Substancial e satisfatória", value: "Substancial" },
      { label: "Rápida e prática", value: "Rápida" },
      { label: "Indulgente (calórico)", value: "Indulgente" },
    ]
  },
  {
    title: "Alguma restrição ou preferência alimentar?",
    options: [
      { label: "Sem restrições", value: "Nenhuma" },
      { label: "Vegetariano/vegano", value: "Vegetariano/vegano" },
      { label: "Sem glúten/lactose", value: "Sem glúten/lactose" },
      { label: "Evitar ingredientes específicos", value: "Específica" },
    ]
  }
];

// Função para limpar markdown e formatar a resposta da IA
function formatarResposta(texto) {
  if (!texto) return '';

  let formatado = texto
    // Remove headers markdown (##, ###, etc)
    .replace(/#{1,6}\s*/g, '')
    // Remove bold markdown (**texto** ou __texto__)
    .replace(/\*\*(.*?)\*\*/g, '$1')
    .replace(/__(.*?)__/g, '$1')
    // Remove italic markdown (*texto* ou _texto_)
    .replace(/(?<!\*)\*(?!\*)(.*?)(?<!\*)\*(?!\*)/g, '$1')
    // Remove bullet points markdown (- ou *)
    .replace(/^[\s]*[-*]\s+/gm, '• ')
    // Remove linhas em branco excessivas
    .replace(/\n{3,}/g, '\n\n')
    // Remove espaços extras no início/fim
    .trim();

  return formatado;
}

export default function PriChat() {
  const [isOpen, setIsOpen] = useState(false);
  const [step, setStep] = useState(0);
  const [answers, setAnswers] = useState([]);
  const [recommendation, setRecommendation] = useState('');
  const [loading, setLoading] = useState(false);
  const chatEndRef = useRef(null);
  const navegacao = useNavigate();

  // Auto-scroll para o final do chat quando muda o conteúdo
  useEffect(() => {
    if (chatEndRef.current) {
      chatEndRef.current.scrollIntoView({ behavior: 'smooth' });
    }
  }, [step, recommendation, loading]);

  const handleOptionClick = async (value) => {
    const newAnswers = [...answers, value];
    setAnswers(newAnswers);

    if (step < questions.length - 1) {
      setStep(step + 1);
    } else {
      setStep(step + 1);
      setLoading(true);
      try {
        const response = await fetch('/api/chat-pri', {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify({ answers: newAnswers })
        });
        const data = await response.json();
        if (data.choices && data.choices[0] && data.choices[0].message) {
          const conteudo = data.choices[0].message.content;
          setRecommendation(formatarResposta(conteudo));
        } else if (data.error) {
          setRecommendation(`😅 ${data.error}`);
        } else {
          setRecommendation("Ops, tive um probleminha para pensar agora. Tente de novo!");
        }
      } catch (error) {
        setRecommendation("Parece que estou sem conexão no momento. Tente novamente mais tarde! 🔌");
      } finally {
        setLoading(false);
      }
    }
  };

  const resetChat = () => {
    setStep(0);
    setAnswers([]);
    setRecommendation('');
  };

  const verProdutosRecomendados = () => {
    // Navega para a tela de recomendações passando as respostas como state
    navegacao('/recomendacoes', { state: { answers, recommendation } });
    setIsOpen(false);
  };

  return (
    <div className="fixed bottom-6 right-6 z-50">
      {isOpen && (
        <div className="absolute bottom-16 right-0 w-80 bg-white rounded-2xl shadow-2xl border border-gray-100 overflow-hidden flex flex-col transition-all duration-300 transform origin-bottom-right">
          {/* Header */}
          <div className="bg-ifood-red text-white p-4 flex items-center justify-between">
            <div className="flex items-center gap-2">
              <div className="bg-white p-1.5 rounded-full">
                <Bot className="w-5 h-5 text-ifood-red" />
              </div>
              <div>
                <h3 className="font-bold text-sm">Pri - Assistente</h3>
                <p className="text-xs text-white opacity-90">Sua conselheira gastronômica</p>
              </div>
            </div>
            <button onClick={() => setIsOpen(false)} className="text-white hover:bg-white/20 p-1 rounded-full transition-colors">
              <X className="w-5 h-5" />
            </button>
          </div>

          {/* Chat Body */}
          <div className="p-4 h-96 overflow-y-auto bg-gray-50 flex flex-col gap-4">
            {/* Mensagem de boas-vindas */}
            <div className="flex gap-2">
              <div className="bg-ifood-red/10 text-gray-800 p-3 rounded-2xl rounded-tl-sm text-sm max-w-[85%]">
                Oi! Sou a Pri 👩🏻‍🍳 Vamos descobrir o que você quer comer hoje?
              </div>
            </div>

            {/* Respostas já dadas */}
            {answers.map((answer, idx) => (
              <div key={idx} className="flex flex-col gap-2">
                <div className="bg-ifood-red/10 text-gray-800 p-3 rounded-2xl rounded-tl-sm text-sm max-w-[85%] font-medium">
                  {questions[idx].title}
                </div>
                <div className="self-end bg-ifood-red text-white p-2.5 rounded-2xl rounded-tr-sm text-sm max-w-[75%]">
                  {answer}
                </div>
              </div>
            ))}

            {/* Pergunta atual */}
            {step < questions.length && (
              <div className="flex flex-col gap-2 transition-opacity duration-300">
                <div className="bg-ifood-red/10 text-gray-800 p-3 rounded-2xl rounded-tl-sm text-sm max-w-[85%] font-medium">
                  {questions[step].title}
                </div>
                <div className="flex flex-col gap-2 mt-2">
                  {questions[step].options.map((opt, idx) => (
                    <button
                      key={idx}
                      onClick={() => handleOptionClick(opt.value)}
                      className="bg-white border border-ifood-red/20 text-ifood-red text-sm p-2.5 rounded-xl text-left hover:bg-ifood-red hover:text-white transition-colors flex justify-between items-center group shadow-sm"
                    >
                      {opt.label}
                      <ChevronRight className="w-4 h-4 opacity-0 group-hover:opacity-100 transition-opacity" />
                    </button>
                  ))}
                </div>
              </div>
            )}

            {/* Loading */}
            {step === questions.length && loading && (
              <div className="flex gap-2">
                <div className="bg-ifood-red/10 text-gray-800 p-3 rounded-2xl rounded-tl-sm text-sm max-w-[85%] flex items-center gap-2">
                  <Loader2 className="w-4 h-4 animate-spin text-ifood-red" />
                  Pensando em algo delicioso para você...
                </div>
              </div>
            )}

            {/* Recomendação */}
            {step === questions.length && !loading && recommendation && (
              <div className="flex flex-col gap-3">
                <div className="bg-ifood-red/10 text-gray-800 p-3 rounded-2xl rounded-tl-sm text-sm leading-relaxed whitespace-pre-wrap">
                  {recommendation}
                </div>

                {/* Botão para ver produtos */}
                <button
                  onClick={verProdutosRecomendados}
                  className="flex items-center justify-center gap-2 bg-ifood-red text-white text-sm font-medium py-2.5 px-4 rounded-xl hover:bg-red-700 transition-colors shadow-sm"
                >
                  <UtensilsCrossed className="w-4 h-4" />
                  Ver produtos recomendados
                </button>

                <button
                  onClick={resetChat}
                  className="text-sm text-ifood-red font-medium hover:underline text-center"
                >
                  Fazer nova consulta
                </button>
              </div>
            )}

            <div ref={chatEndRef} />
          </div>
        </div>
      )}

      {/* Botão flutuante */}
      <button
        onClick={() => setIsOpen(!isOpen)}
        className={`bg-ifood-red text-white p-4 rounded-full shadow-xl hover:shadow-2xl hover:scale-105 transition-all duration-300 flex items-center justify-center ${isOpen ? 'hidden' : 'block'}`}
      >
        <MessageCircle className="w-6 h-6" />
      </button>
    </div>
  );
}
