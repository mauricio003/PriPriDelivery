import React, { useState, useEffect } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import { collection, getDocs, query, where, doc, getDoc, addDoc, updateDoc } from 'firebase/firestore';
import { db } from '../lib/firebase';
import { useAuth } from '../contexts/AuthContext';
import {
  ArrowLeft,
  Store,
  ShoppingCart,
  Plus,
  Minus,
  Bot,
  Sparkles,
  Loader2
} from 'lucide-react';
import { toast } from 'react-hot-toast';

function Recomendacoes() {
  const navegacao = useNavigate();
  const location = useLocation();
  const { usuario: user } = useAuth();

  const { answers, recommendation } = location.state || {};

  const [restaurantes, setRestaurantes] = useState([]);
  const [produtos, setProdutos] = useState([]);
  const [carregando, setCarregando] = useState(true);
  const [quantidades, setQuantidades] = useState({});

  useEffect(() => {
    if (!answers) {
      navegacao('/home', { replace: true });
      return;
    }
    carregarProdutosFiltrados();
  }, [answers]);

  const carregarProdutosFiltrados = async () => {
    try {
      setCarregando(true);

      // Carregar todos os restaurantes
      const restSnap = await getDocs(collection(db, 'restaurantes'));
      const todosRestaurantes = restSnap.docs.map(d => ({ id: d.id, ...d.data() }));
      setRestaurantes(todosRestaurantes);

      // Carregar todos os produtos
      const prodSnap = await getDocs(collection(db, 'produtos'));
      const todosProdutos = prodSnap.docs
        .map(d => ({ id: d.id, ...d.data() }))
        .filter(p => p.disponivel !== false);

      // Filtrar por orçamento
      const orcamento = answers[0];
      let produtosFiltrados = todosProdutos;

      if (orcamento === 'Econômico') {
        produtosFiltrados = produtosFiltrados.filter(p => p.preco <= 25);
      } else if (orcamento === 'Moderado') {
        produtosFiltrados = produtosFiltrados.filter(p => p.preco > 25 && p.preco <= 45);
      } else if (orcamento === 'Premium') {
        produtosFiltrados = produtosFiltrados.filter(p => p.preco > 45 && p.preco <= 70);
      }
      // "Sem limite" não filtra

      // Se o filtro de orçamento não retornou nada, mostrar todos
      if (produtosFiltrados.length === 0) {
        produtosFiltrados = todosProdutos;
      }

      // Associar restaurante a cada produto
      const produtosComRestaurante = produtosFiltrados.map(prod => {
        const restId = prod.restauranteId || prod.restaurante_id;
        const restaurante = todosRestaurantes.find(r => r.id === restId);
        return { ...prod, restaurante };
      });

      setProdutos(produtosComRestaurante);

      // Inicializar quantidades
      const qtds = {};
      produtosComRestaurante.forEach(p => { qtds[p.id] = 1; });
      setQuantidades(qtds);

    } catch (erro) {
      console.error('Erro ao carregar produtos:', erro);
      toast.error('Erro ao carregar produtos recomendados');
    } finally {
      setCarregando(false);
    }
  };

  const ajustarQuantidade = (produtoId, delta) => {
    setQuantidades(prev => ({
      ...prev,
      [produtoId]: Math.max(1, (prev[produtoId] || 1) + delta)
    }));
  };

  const adicionarAoCarrinho = async (produto) => {
    try {
      if (!user?.uid) {
        toast.error('Faça login para adicionar ao carrinho');
        return;
      }

      const quantidade = quantidades[produto.id] || 1;

      const q = query(
        collection(db, 'carrinho'),
        where('user_id', '==', user.uid),
        where('produto_id', '==', produto.id)
      );

      const snapshot = await getDocs(q);

      if (!snapshot.empty) {
        const itemExistente = snapshot.docs[0];
        const dadosItem = itemExistente.data();

        await updateDoc(doc(db, 'carrinho', itemExistente.id), {
          quantidade: (dadosItem.quantidade || 1) + quantidade
        });
      } else {
        await addDoc(collection(db, 'carrinho'), {
          user_id: user.uid,
          produto_id: produto.id,
          quantidade,
          created_at: new Date()
        });
      }

      setQuantidades(prev => ({ ...prev, [produto.id]: 1 }));
      toast.success('Adicionado ao carrinho!');
    } catch (erro) {
      console.error('Erro ao adicionar ao carrinho:', erro);
      toast.error('Erro ao adicionar ao carrinho');
    }
  };

  const irParaRestaurante = (restauranteId) => {
    navegacao(`/restaurante/${restauranteId}/comprar`);
  };

  const formatarMoeda = (valor) => {
    return new Intl.NumberFormat('pt-BR', {
      style: 'currency',
      currency: 'BRL'
    }).format(Number(valor || 0));
  };

  return (
    <div className="min-h-screen bg-gray-50">
      {/* Header */}
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
              <button
                onClick={() => navegacao('/home')}
                className="ml-4 flex items-center text-gray-500 hover:text-gray-700"
              >
                <ArrowLeft className="w-5 h-5 mr-2" />
                Voltar
              </button>
            </div>
          </div>
        </div>
      </nav>

      <main className="max-w-7xl mx-auto py-6 px-4 sm:px-6 lg:px-8">
        {/* Card da recomendação da Pri */}
        {recommendation && (
          <div className="bg-white rounded-2xl shadow-sm border border-gray-100 p-6 mb-8">
            <div className="flex items-center gap-3 mb-4">
              <div className="bg-ifood-red/10 p-2.5 rounded-full">
                <Bot className="w-6 h-6 text-ifood-red" />
              </div>
              <div>
                <h2 className="text-lg font-bold text-gray-900 flex items-center gap-2">
                  Recomendação da Pri
                  <Sparkles className="w-4 h-4 text-yellow-500" />
                </h2>
                <p className="text-sm text-gray-500">Baseada nas suas preferências</p>
              </div>
            </div>

            <div className="bg-gray-50 rounded-xl p-4 text-sm text-gray-700 leading-relaxed whitespace-pre-wrap">
              {recommendation}
            </div>

            <div className="mt-3 flex flex-wrap gap-2">
              {answers && answers.map((answer, idx) => (
                <span
                  key={idx}
                  className="inline-flex items-center px-3 py-1 rounded-full text-xs font-medium bg-ifood-red/10 text-ifood-red"
                >
                  {answer}
                </span>
              ))}
            </div>
          </div>
        )}

        {/* Título da seção de produtos */}
        <div className="flex items-center gap-2 mb-6">
          <h2 className="text-xl font-bold text-gray-900">
            Produtos para você
          </h2>
          <span className="text-sm text-gray-500">
            ({produtos.length} {produtos.length === 1 ? 'item' : 'itens'})
          </span>
        </div>

        {/* Loading */}
        {carregando ? (
          <div className="flex flex-col items-center justify-center py-16">
            <Loader2 className="w-8 h-8 animate-spin text-ifood-red mb-3" />
            <p className="text-gray-500">Buscando produtos para você...</p>
          </div>
        ) : produtos.length === 0 ? (
          <div className="text-center py-16">
            <Store className="w-12 h-12 text-gray-300 mx-auto mb-3" />
            <p className="text-gray-500">Nenhum produto encontrado com esses filtros.</p>
            <button
              onClick={() => navegacao('/home')}
              className="mt-4 text-ifood-red font-medium hover:underline"
            >
              Ver todos os restaurantes
            </button>
          </div>
        ) : (
          <>
            {/* Agrupar por restaurante */}
            {restaurantes
              .filter(rest => produtos.some(p => (p.restauranteId || p.restaurante_id) === rest.id))
              .map(restaurante => (
                <div key={restaurante.id} className="mb-8">
                  {/* Header do restaurante */}
                  <div
                    onClick={() => irParaRestaurante(restaurante.id)}
                    className="flex items-center gap-3 mb-4 cursor-pointer hover:opacity-80 transition-opacity"
                  >
                    <div className="w-12 h-12 rounded-full overflow-hidden bg-gray-200 flex-shrink-0">
                      {restaurante.imagemUrl ? (
                        <img
                          src={restaurante.imagemUrl}
                          alt={restaurante.nome}
                          className="w-full h-full object-cover"
                        />
                      ) : (
                        <div className="w-full h-full flex items-center justify-center">
                          <Store className="w-6 h-6 text-gray-400" />
                        </div>
                      )}
                    </div>
                    <div>
                      <h3 className="font-semibold text-gray-900">{restaurante.nome}</h3>
                      <p className="text-xs text-gray-500">
                        {restaurante.categoria} • Entrega {formatarMoeda(restaurante.taxaEntregaNormal || 0)} • ~{restaurante.tempoEntregaNormal || '?'} min
                      </p>
                    </div>
                  </div>

                  {/* Produtos do restaurante */}
                  <div className="grid gap-4 grid-cols-1 md:grid-cols-2 lg:grid-cols-3">
                    {produtos
                      .filter(p => (p.restauranteId || p.restaurante_id) === restaurante.id)
                      .map(produto => (
                        <div
                          key={produto.id}
                          className="bg-white rounded-xl shadow-sm border border-gray-100 overflow-hidden hover:shadow-md transition-shadow"
                        >
                          {(produto.imagem_url || produto.imagemUrl) ? (
                            <img
                              src={produto.imagem_url || produto.imagemUrl}
                              alt={produto.nome}
                              className="w-full h-40 object-cover"
                            />
                          ) : (
                            <div className="w-full h-40 bg-gray-100 flex items-center justify-center">
                              <Store className="w-10 h-10 text-gray-300" />
                            </div>
                          )}

                          <div className="p-4">
                            <h4 className="font-medium text-gray-900">{produto.nome}</h4>

                            {produto.descricao && (
                              <p className="mt-1 text-sm text-gray-500 line-clamp-2">
                                {produto.descricao}
                              </p>
                            )}

                            <p className="mt-2 text-lg font-bold text-ifood-red">
                              {formatarMoeda(produto.preco)}
                            </p>

                            <div className="mt-3 flex items-center justify-between">
                              <div className="flex items-center gap-2">
                                <button
                                  onClick={() => ajustarQuantidade(produto.id, -1)}
                                  className="p-1.5 rounded-full bg-gray-100 hover:bg-gray-200 text-gray-600 transition-colors"
                                >
                                  <Minus className="w-3.5 h-3.5" />
                                </button>
                                <span className="text-sm font-medium w-6 text-center">
                                  {quantidades[produto.id] || 1}
                                </span>
                                <button
                                  onClick={() => ajustarQuantidade(produto.id, 1)}
                                  className="p-1.5 rounded-full bg-gray-100 hover:bg-gray-200 text-gray-600 transition-colors"
                                >
                                  <Plus className="w-3.5 h-3.5" />
                                </button>
                              </div>

                              <button
                                onClick={() => adicionarAoCarrinho(produto)}
                                className="flex items-center gap-1.5 px-3 py-2 bg-ifood-red text-white text-sm font-medium rounded-lg hover:bg-red-700 transition-colors"
                              >
                                <ShoppingCart className="w-4 h-4" />
                                Adicionar
                              </button>
                            </div>
                          </div>
                        </div>
                      ))}
                  </div>
                </div>
              ))}
          </>
        )}
      </main>
    </div>
  );
}

export default Recomendacoes;
