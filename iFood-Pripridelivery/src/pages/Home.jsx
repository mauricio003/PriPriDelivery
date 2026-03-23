import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import {
  collection,
  getDocs,
  query,
  where,
  doc,
  getDoc,
  updateDoc,
  deleteDoc
} from 'firebase/firestore';
import { db } from '../lib/firebase';
import { Search, MapPin, LogOut, Store, ShoppingCart } from 'lucide-react';
import { useAuth } from '../contexts/AuthContext';
import CarrinhoModal from '../components/CarrinhoModal';

function Home() {
  const navegacao = useNavigate();
  const { usuario: user, logout } = useAuth();
  const [restaurantes, setRestaurantes] = useState([]);
  const [termoPesquisa, setTermoPesquisa] = useState('');
  const [categoriaAtiva, setCategoriaAtiva] = useState('Todos');
  const [carregando, setCarregando] = useState(true);
  const [carrinhoAberto, setCarrinhoAberto] = useState(false);
  const [itensCarrinho, setItensCarrinho] = useState([]);
  const [totalCarrinho, setTotalCarrinho] = useState(0);

  const categorias = [
    'Todos',
    'Italiana',
    'Japonesa',
    'Brasileira',
    'Mexicana',
    'Vegetariana'
  ];

  useEffect(() => {
    carregarRestaurantes();
  }, []);

  useEffect(() => {
    if (user?.uid) {
      carregarCarrinho();
    }
  }, [user]);

  const carregarCarrinho = async () => {
    try {
      if (!user?.uid) return;

      const q = query(
        collection(db, 'carrinho'),
        where('userId', '==', user.uid)
      );

      const snapshot = await getDocs(q);

      const itensBase = snapshot.docs.map((item) => ({
        id: item.id,
        ...item.data()
      }));

      const itensComProduto = await Promise.all(
        itensBase.map(async (item) => {
          try {
            const produtoRef = doc(db, 'produtos', item.produtoId);
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

            let restaurante = null;

            if (produto.restauranteId) {
              const restauranteRef = doc(db, 'restaurantes', produto.restauranteId);
              const restauranteSnap = await getDoc(restauranteRef);

              if (restauranteSnap.exists()) {
                restaurante = {
                  id: restauranteSnap.id,
                  ...restauranteSnap.data()
                };
              }
            }

            return {
              ...item,
              produto: {
                ...produto,
                restaurante
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

  const carregarRestaurantes = async () => {
    try {
      const snapshot = await getDocs(collection(db, 'restaurantes'));

      const lista = snapshot.docs.map((item) => ({
        id: item.id,
        ...item.data()
      }));

      setRestaurantes(lista);
    } catch (erro) {
      console.error('Erro ao carregar restaurantes:', erro);
    } finally {
      setCarregando(false);
    }
  };

  const restaurantesFiltrados = restaurantes.filter((restaurante) => {
    const correspondeCategoria =
      categoriaAtiva === 'Todos' || restaurante.categoria === categoriaAtiva;

    const correspondePesquisa =
      restaurante.nome?.toLowerCase().includes(termoPesquisa.toLowerCase()) ||
      restaurante.categoria?.toLowerCase().includes(termoPesquisa.toLowerCase());

    return correspondeCategoria && correspondePesquisa;
  });

  const fazerLogout = async () => {
    await logout();
    navegacao('/', { replace: true });
  };

  const irParaComprar = (restauranteId) => {
    navegacao('/restaurante/${restauranteId}/comprar');
  };

  return (
    <div className="min-h-screen bg-gray-50">
      <header className="bg-white shadow-sm">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-4">
          <div className="flex justify-between items-center">
            <img
              src="https://logodownload.org/wp-content/uploads/2017/05/ifood-logo-0.png"
              alt="iFood"
              className="h-8"
            />

            <div className="flex items-center space-x-4">
              <button
                onClick={() => navegacao('/endereco')}
                className="flex items-center text-gray-600 hover:text-gray-900"
              >
                <MapPin className="w-5 h-5 mr-2" />
                <span>Endereços</span>
              </button>

              <button
                onClick={() => navegacao('/restaurante')}
                className="flex items-center text-gray-600 hover:text-gray-900"
              >
                <Store className="w-5 h-5 mr-2" />
                <span>Meus Restaurantes</span>
              </button>

              <button
                onClick={() => setCarrinhoAberto(true)}
                className="flex items-center text-gray-600 hover:text-gray-900"
              >
                <ShoppingCart className="w-5 h-5 mr-2" />
                <span>Carrinho ({itensCarrinho.length})</span>
              </button>

              <button
                onClick={fazerLogout}
                className="flex items-center text-gray-600 hover:text-gray-900"
              >
                <LogOut className="w-5 h-5 mr-2" />
                <span>Sair</span>
              </button>
            </div>
          </div>

          <div className="mt-4 relative">
            <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
              <Search className="h-5 w-5 text-gray-400" />
            </div>

            <input
              type="text"
              value={termoPesquisa}
              onChange={(e) => setTermoPesquisa(e.target.value)}
              placeholder="Busque por restaurantes ou culinária"
              className="block w-full pl-10 pr-3 py-2 border border-gray-300 rounded-md leading-5 bg-white placeholder-gray-500 focus:outline-none focus:placeholder-gray-400 focus:ring-1 focus:ring-ifood-red focus:border-ifood-red sm:text-sm"
            />
          </div>

          <div className="mt-4 flex space-x-4 overflow-x-auto pb-2">
            {categorias.map((categoria) => (
              <button
                key={categoria}
                onClick={() => setCategoriaAtiva(categoria)}
                className={`px-4 py-2 rounded-full text-sm font-medium whitespace-nowrap ${
                  categoriaAtiva === categoria
                    ? 'bg-ifood-red text-white'
                    : 'bg-gray-100 text-gray-800 hover:bg-gray-200'
                }`}
              >
                {categoria}
              </button>
            ))}
          </div>
        </div>
      </header>

      <main className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        {carregando ? (
          <div className="text-center">Carregando restaurantes...</div>
        ) : restaurantesFiltrados.length === 0 ? (
          <div className="text-center text-gray-500">
            Nenhum restaurante encontrado
          </div>
        ) : (
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
            {restaurantesFiltrados.map((restaurante) => (
              <div
                key={restaurante.id}
                onClick={() => irParaComprar(restaurante.id)}
                className="bg-white rounded-lg shadow-sm overflow-hidden hover:shadow-md transition-shadow duration-200 cursor-pointer"
              >
                {restaurante.imagemUrl ? (
                  <div className="relative h-48">
                    <img
                      src={restaurante.imagemUrl}
                      alt={restaurante.nome}
                      className="w-full h-full object-cover"
                    />
                  </div>
                ) : (
                  <div className="w-full h-48 bg-gray-200 flex items-center justify-center">
                    <Store className="w-12 h-12 text-gray-400" />
                  </div>
                )}

                <div className="p-4">
                  <h3 className="text-lg font-semibold text-gray-900">
                    {restaurante.nome}
                  </h3>

                  <p className="text-sm text-gray-500">{restaurante.categoria}</p>

                  <p className="text-sm text-gray-500 mt-1">
                    {restaurante.horario_abertura} - {restaurante.horario_fechamento}
                  </p>

                  {restaurante.descricao && (
                    <p className="text-sm text-gray-600 mt-2 line-clamp-2">
                      {restaurante.descricao}
                    </p>
                  )}
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
      />
    </div>
  );
}

export default Home;