import React, { useState, useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import {
  collection,
  getDocs,
  query,
  where,
  doc,
  getDoc,
  addDoc,
  updateDoc,
  deleteDoc,
} from 'firebase/firestore';
import { db } from '../lib/firebase';
import { useAuth } from '../contexts/AuthContext';
import {
  Store,
  ArrowLeft,
  ShoppingCart,
  Plus,
  Minus,
  CreditCard,
  QrCode as Qr
} from 'lucide-react';
import CarrinhoModal from '../components/CarrinhoModal';

function ProdutosParaCarrinho() {
  const { restauranteId } = useParams();
  const navegacao = useNavigate();
  const { usuario: user } = useAuth();

  const [produtos, setProdutos] = useState([]);
  const [restaurante, setRestaurante] = useState(null);
  const [carregando, setCarregando] = useState(true);
  const [erro, setErro] = useState(null);
  const [carrinhoAberto, setCarrinhoAberto] = useState(false);
  const [itensCarrinho, setItensCarrinho] = useState([]);
  const [totalCarrinho, setTotalCarrinho] = useState(0);
  const [quantidades, setQuantidades] = useState({});
  const [modalPagamentoAberto, setModalPagamentoAberto] = useState(false);
  const [formaPagamento, setFormaPagamento] = useState('');
  const [dadosPagamento, setDadosPagamento] = useState({
    numero: '',
    nome: '',
    validade: '',
    cvv: ''
  });

  useEffect(() => {
    carregarRestaurante();
    carregarProdutos();
  }, [restauranteId]);

  useEffect(() => {
    if (user?.uid) {
      carregarCarrinho();
    }
  }, [user, restauranteId]);

  const carregarCarrinho = async () => {
    try {
      if (!user?.uid) return;

      const q = query(
        collection(db, 'carrinho'),
        where('user_id', '==', user.uid)
      );

      const snapshot = await getDocs(q);

      const itensBase = snapshot.docs.map((item) => ({
        id: item.id,
        ...item.data()
      }));

      const itensComProduto = await Promise.all(
        itensBase.map(async (item) => {
          try {
            const produtoRef = doc(db, 'produtos', item.produto_id);
            const produtoSnap = await getDoc(produtoRef);

            if (!produtoSnap.exists()) {
              return {
                ...item,
                produto: null
              };
            }

            const produto = {
              id: produtoSnap.id,
              ...produtoSnap.data()
            };

            let restauranteProduto = null;

            if (produto.restaurante_id) {
              const restauranteRef = doc(db, 'restaurantes', produto.restaurante_id);
              const restauranteSnap = await getDoc(restauranteRef);

              if (restauranteSnap.exists()) {
                restauranteProduto = {
                  id: restauranteSnap.id,
                  ...restauranteSnap.data()
                };
              }
            }

            return {
              ...item,
              produto: {
                ...produto,
                restaurante: restauranteProduto
              }
            };
          } catch (erro) {
            console.error('Erro ao montar item do carrinho:', erro);
            return {
              ...item,
              produto: null
            };
          }
        })
      );

      const itensValidos = itensComProduto.filter((item) => item.produto);
      setItensCarrinho(itensValidos);
      calcularTotal(itensValidos);
    } catch (erro) {
      console.error('Erro ao carregar carrinho:', erro);
    }
  };

  const calcularTotal = (itens) => {
    const total = itens.reduce((acc, item) => {
      return acc + item.produto.preco * item.quantidade;
    }, 0);

    setTotalCarrinho(total);
  };

  const aumentarQuantidade = async (itemId) => {
    try {
      const item = itensCarrinho.find((i) => i.id === itemId);
      if (!item) return;

      const novaQuantidade = item.quantidade + 1;

      await updateDoc(doc(db, 'carrinho', itemId), {
        quantidade: novaQuantidade
      });

      await carregarCarrinho();
    } catch (erro) {
      console.error('Erro ao aumentar quantidade:', erro);
    }
  };

  const diminuirQuantidade = async (itemId) => {
    try {
      const item = itensCarrinho.find((i) => i.id === itemId);
      if (!item) return;

      if (item.quantidade <= 1) {
        await removerItem(itemId);
        return;
      }

      const novaQuantidade = item.quantidade - 1;

      await updateDoc(doc(db, 'carrinho', itemId), {
        quantidade: novaQuantidade
      });

      await carregarCarrinho();
    } catch (erro) {
      console.error('Erro ao diminuir quantidade:', erro);
    }
  };

  const removerItem = async (itemId) => {
    try {
      await deleteDoc(doc(db, 'carrinho', itemId));
      await carregarCarrinho();
    } catch (erro) {
      console.error('Erro ao remover item:', erro);
    }
  };

const carregarRestaurante = async () => {
  try {
    console.log('🔥 restauranteId recebido:', restauranteId)

    const restauranteRef = doc(db, 'restaurantes', restauranteId);

    console.log('🔍 buscando restaurante com ID:', restauranteId);

    const restauranteSnap = await getDoc(restauranteRef);

    if (!restauranteSnap.exists()) {
      throw new Error('Restaurante não encontrado');
    }

    setRestaurante({
      id: restauranteSnap.id,
      ...restauranteSnap.data()
    });
  } catch (erro) {
    console.error('Erro ao carregar restaurante:', erro);

    console.error('🚨 ID que deu erro:', restauranteId);

    setErro('Não foi possível carregar as informações do restaurante');
  }
};

  const carregarProdutos = async () => {
  try {
    setErro(null);
    setCarregando(true);

    const q = query(
      collection(db, 'produtos'),
      where('restaurante_id', '==', restauranteId)
    );

    const snapshot = await getDocs(q);

    const lista = snapshot.docs
      .map((item) => ({
        id: item.id,
        ...item.data(),
      }))
      .filter((p) => p.disponivel !== false)
      .sort((a, b) => {
        const da = a.created_at?.toDate ? a.created_at.toDate() : new Date(a.created_at || 0);
        const db = b.created_at?.toDate ? b.created_at.toDate() : new Date(b.created_at || 0);
        return db - da;
      });

    setProdutos(lista);

    const quantidadesIniciais = {};
    lista.forEach((produto) => {
      quantidadesIniciais[produto.id] = 1;
    });

    setQuantidades(quantidadesIniciais);

  } catch (erro) {
    console.error('Erro ao carregar produtos:', erro);
    setErro('Não foi possível carregar os produtos');
  } finally {
    setCarregando(false);
  }
};

  const ajustarQuantidade = (produtoId, delta) => {
    setQuantidades((prev) => ({
      ...prev,
      [produtoId]: Math.max(1, (prev[produtoId] || 1) + delta)
    }));
  };

  const adicionarAoCarrinho = async (produto) => {
    try {
      if (!user?.uid) {
        setErro('Usuário não autenticado');
        return;
      }

      if (itensCarrinho.length > 0) {
        const primeiroItem = itensCarrinho[0];
        if (primeiroItem.produto?.restaurante?.id !== restauranteId) {
          alert(
            'Você já possui itens de outro restaurante no carrinho. Finalize ou remova esses itens antes de adicionar produtos de um novo restaurante.'
          );
          return;
        }
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

      setQuantidades((prev) => ({
        ...prev,
        [produto.id]: 1
      }));

      await carregarCarrinho();
      alert('Produto adicionado ao carrinho!');
    } catch (erro) {
      console.error('Erro ao adicionar ao carrinho:', erro);
      setErro('Erro ao adicionar produto ao carrinho');
    }
  };

  const finalizarPedido = () => {
    setCarrinhoAberto(false);
    setModalPagamentoAberto(true);
  };

  const processarPagamento = async () => {
    try {
      if (!user?.uid) {
        alert('Usuário não autenticado.');
        return;
      }

      for (const item of itensCarrinho) {
        await deleteDoc(doc(db, 'carrinho', item.id));
      }

      alert('Pedido realizado com sucesso!');
      setModalPagamentoAberto(false);
      setFormaPagamento('');
      setDadosPagamento({
        numero: '',
        nome: '',
        validade: '',
        cvv: ''
      });

      await carregarCarrinho();
      navegacao('/home');
    } catch (erro) {
      console.error('Erro ao processar pagamento:', erro);
      alert('Erro ao processar pagamento. Tente novamente.');
    }
  };

  const categorias = [...new Set(produtos.map((p) => p.categoria))];

  return (
    <div className="min-h-screen bg-gray-50">
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
              <span className="ml-4 text-xl font-semibold text-gray-900">
                {restaurante?.nome}
              </span>
            </div>

            <div className="flex items-center">
              <button
                onClick={() => setCarrinhoAberto(true)}
                className="flex items-center text-gray-600 hover:text-gray-900"
              >
                <ShoppingCart className="w-5 h-5 mr-2" />
                <span>Carrinho ({itensCarrinho.length})</span>
              </button>
            </div>
          </div>
        </div>
      </nav>

      <main className="max-w-7xl mx-auto py-6 sm:px-6 lg:px-8">
        {erro && (
          <div className="mb-4 p-4 bg-red-100 border border-red-400 text-red-700 rounded-md">
            {erro}
          </div>
        )}

        {carregando ? (
          <div className="text-center py-4">Carregando produtos...</div>
        ) : produtos.length === 0 ? (
          <div className="text-center py-4 text-gray-500">
            Nenhum produto disponível
          </div>
        ) : (
          <div className="space-y-8">
            {categorias.map((categoria) => (
              <div key={categoria}>
                <h2 className="text-xl font-semibold text-gray-900 mb-4">
                  {categoria}
                </h2>

                <div className="grid gap-6 grid-cols-1 md:grid-cols-2 lg:grid-cols-3">
                  {produtos
                    .filter((produto) => produto.categoria === categoria)
                    .map((produto) => (
                      <div
                        key={produto.id}
                        className="bg-white rounded-lg shadow-sm overflow-hidden"
                      >
                        {produto.imagem_url ? (
                          <img
                            src={produto.imagem_url}
                            alt={produto.nome}
                            className="w-full h-48 object-cover"
                          />
                        ) : (
                          <div className="w-full h-48 bg-gray-200 flex items-center justify-center">
                            <Store className="w-12 h-12 text-gray-400" />
                          </div>
                        )}

                        <div className="p-4">
                          <h3 className="text-lg font-medium text-gray-900">
                            {produto.nome}
                          </h3>

                          {produto.descricao && (
                            <p className="mt-1 text-sm text-gray-500">
                              {produto.descricao}
                            </p>
                          )}

                          <p className="mt-2 text-lg font-semibold text-ifood-red">
                            {new Intl.NumberFormat('pt-BR', {
                              style: 'currency',
                              currency: 'BRL'
                            }).format(produto.preco)}
                          </p>

                          <div className="mt-4 flex items-center justify-between">
                            <div className="flex items-center space-x-2">
                              <button
                                onClick={() => ajustarQuantidade(produto.id, -1)}
                                className="p-1 text-gray-400 hover:text-gray-500"
                              >
                                <Minus className="w-4 h-4" />
                              </button>

                              <span className="text-gray-600">
                                {quantidades[produto.id] || 1}
                              </span>

                              <button
                                onClick={() => ajustarQuantidade(produto.id, 1)}
                                className="p-1 text-gray-400 hover:text-gray-500"
                              >
                                <Plus className="w-4 h-4" />
                              </button>
                            </div>

                            <button
                              onClick={() => adicionarAoCarrinho(produto)}
                              className="flex items-center px-4 py-2 bg-ifood-red text-white rounded-md hover:bg-red-700"
                            >
                              <ShoppingCart className="w-4 h-4 mr-2" />
                              Adicionar
                            </button>
                          </div>
                        </div>
                      </div>
                    ))}
                </div>
              </div>
            ))}
          </div>
        )}
      </main>

      <CarrinhoModal
        aberto={carrinhoAberto}
        fechar={() => setCarrinhoAberto(false)}
        itens={itensCarrinho}
        aumentarQuantidade={aumentarQuantidade}
        diminuirQuantidade={diminuirQuantidade}
        removerItem={removerItem}
        total={totalCarrinho}
        finalizarPedido={finalizarPedido}
      />

      {modalPagamentoAberto && (
        <div className="fixed inset-0 bg-gray-500 bg-opacity-75 flex items-center justify-center p-4 z-50">
          <div className="bg-white rounded-lg max-w-md w-full p-6">
            <h3 className="text-lg font-medium text-gray-900 mb-4">Pagamento</h3>

            <div className="space-y-4">
              <div className="flex space-x-4">
                <button
                  onClick={() => setFormaPagamento('cartao')}
                  className={`flex-1 flex items-center justify-center p-4 rounded-lg border ${
                    formaPagamento === 'cartao'
                      ? 'border-ifood-red bg-red-50'
                      : 'border-gray-300 hover:border-gray-400'
                  }`}
                >
                  <CreditCard className="w-6 h-6 mr-2" />
                  <span>Cartão</span>
                </button>

                <button
                  onClick={() => setFormaPagamento('pix')}
                  className={`flex-1 flex items-center justify-center p-4 rounded-lg border ${
                    formaPagamento === 'pix'
                      ? 'border-ifood-red bg-red-50'
                      : 'border-gray-300 hover:border-gray-400'
                  }`}
                >
                  <Qr className="w-6 h-6 mr-2" />
                  <span>PIX</span>
                </button>
              </div>

              {formaPagamento === 'cartao' && (
                <div className="space-y-4">
                  <div>
                    <label className="block text-sm font-medium text-gray-700">
                      Número do Cartão
                    </label>
                    <input
                      type="text"
                      value={dadosPagamento.numero}
                      onChange={(e) =>
                        setDadosPagamento({
                          ...dadosPagamento,
                          numero: e.target.value
                        })
                      }
                      className="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-ifood-red focus:ring-ifood-red sm:text-sm"
                      placeholder="1234 5678 9012 3456"
                    />
                  </div>

                  <div>
                    <label className="block text-sm font-medium text-gray-700">
                      Nome no Cartão
                    </label>
                    <input
                      type="text"
                      value={dadosPagamento.nome}
                      onChange={(e) =>
                        setDadosPagamento({
                          ...dadosPagamento,
                          nome: e.target.value
                        })
                      }
                      className="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-ifood-red focus:ring-ifood-red sm:text-sm"
                      placeholder="NOME COMO ESTÁ NO CARTÃO"
                    />
                  </div>

                  <div className="grid grid-cols-2 gap-4">
                    <div>
                      <label className="block text-sm font-medium text-gray-700">
                        Validade
                      </label>
                      <input
                        type="text"
                        value={dadosPagamento.validade}
                        onChange={(e) =>
                          setDadosPagamento({
                            ...dadosPagamento,
                            validade: e.target.value
                          })
                        }
                        className="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-ifood-red focus:ring-ifood-red sm:text-sm"
                        placeholder="MM/AA"
                      />
                    </div>

                    <div>
                      <label className="block text-sm font-medium text-gray-700">
                        CVV
                      </label>
                      <input
                        type="text"
                        value={dadosPagamento.cvv}
                        onChange={(e) =>
                          setDadosPagamento({
                            ...dadosPagamento,
                            cvv: e.target.value
                          })
                        }
                        className="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-ifood-red focus:ring-ifood-red sm:text-sm"
                        placeholder="123"
                      />
                    </div>
                  </div>
                </div>
              )}

              {formaPagamento === 'pix' && (
                <div className="text-center py-4">
                  <Qr className="w-32 h-32 mx-auto mb-4" />
                  <p className="text-sm text-gray-600">
                    Escaneie o código QR com seu aplicativo de pagamento
                  </p>
                </div>
              )}

              <div className="flex justify-between items-center text-lg font-medium mb-4">
                <span>Total</span>
                <span className="text-ifood-red">
                  {new Intl.NumberFormat('pt-BR', {
                    style: 'currency',
                    currency: 'BRL'
                  }).format(totalCarrinho)}
                </span>
              </div>

              <div className="flex justify-end space-x-3">
                <button
                  onClick={() => setModalPagamentoAberto(false)}
                  className="px-4 py-2 border border-gray-300 rounded-md shadow-sm text-sm font-medium text-gray-700 bg-white hover:bg-gray-50"
                >
                  Cancelar
                </button>

                <button
                  onClick={processarPagamento}
                  disabled={
                    !formaPagamento ||
                    (formaPagamento === 'cartao' &&
                      (!dadosPagamento.numero ||
                        !dadosPagamento.nome ||
                        !dadosPagamento.validade ||
                        !dadosPagamento.cvv))
                  }
                  className="px-4 py-2 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-ifood-red hover:bg-red-700 disabled:opacity-50 disabled:cursor-not-allowed"
                >
                  Finalizar Pedido
                </button>
              </div>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}

export default ProdutosParaCarrinho;