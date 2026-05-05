import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { collection, query, where, getDocs, orderBy } from 'firebase/firestore';
import { db } from '../lib/firebase';
import { useAuth } from '../contexts/AuthContext';
import { ArrowLeft, Clock, ShoppingBag } from 'lucide-react';

function MeusPedidos() {
  const navegacao = useNavigate();
  const { usuario } = useAuth();
  const [pedidos, setPedidos] = useState([]);
  const [carregando, setCarregando] = useState(true);

  useEffect(() => {
    if (usuario?.uid) {
      carregarPedidos();
    }
  }, [usuario]);

  const carregarPedidos = async () => {
    try {
      const q = query(
        collection(db, 'pedidos'),
        where('user_id', '==', usuario.uid)
      );

      const snapshot = await getDocs(q);
      const lista = snapshot.docs.map(doc => ({
        id: doc.id,
        ...doc.data()
      })).sort((a, b) => new Date(b.createdAt) - new Date(a.createdAt));

      setPedidos(lista);
    } catch (error) {
      console.error('Erro ao carregar pedidos:', error);
    } finally {
      setCarregando(false);
    }
  };

  return (
    <div className="min-h-screen bg-gray-50">
      <nav className="bg-white shadow-sm sticky top-0 z-10">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex justify-between h-16">
            <div className="flex items-center">
              <button
                onClick={() => navegacao('/home')}
                className="mr-4 flex items-center text-gray-500 hover:text-gray-700"
              >
                <ArrowLeft className="w-5 h-5" />
              </button>
              <h1 className="text-xl font-semibold text-gray-900">Meus Pedidos</h1>
            </div>
          </div>
        </div>
      </nav>

      <main className="max-w-3xl mx-auto py-8 px-4">
        {carregando ? (
          <div className="text-center py-10">Carregando seus pedidos...</div>
        ) : pedidos.length === 0 ? (
          <div className="text-center py-10">
            <ShoppingBag className="w-16 h-16 mx-auto text-gray-300 mb-4" />
            <p className="text-gray-500">Você ainda não fez nenhum pedido.</p>
            <button
              onClick={() => navegacao('/home')}
              className="mt-4 text-ifood-red font-medium"
            >
              Ir para a página inicial
            </button>
          </div>
        ) : (
          <div className="space-y-4">
            {pedidos.map((pedido) => (
              <div
                key={pedido.id}
                className="bg-white rounded-lg shadow-sm p-6 hover:shadow-md transition-shadow cursor-pointer"
                onClick={() => navegacao('/acompanhamento', { state: { pedidoId: pedido.pedidoId } })}
              >
                <div className="flex justify-between items-start mb-4">
                  <div>
                    <p className="font-semibold text-lg text-gray-900">
                      Pedido #{pedido.pedidoId}
                    </p>
                    <div className="flex items-center text-sm text-gray-500 mt-1">
                      <Clock className="w-4 h-4 mr-1" />
                      {new Date(pedido.createdAt).toLocaleDateString()} às {new Date(pedido.createdAt).toLocaleTimeString()}
                    </div>
                  </div>
                  <span className="bg-green-100 text-green-700 px-3 py-1 rounded-full text-xs font-medium">
                    {pedido.status || 'Em andamento'}
                  </span>
                </div>

                <div className="border-t border-b border-gray-100 py-3 mb-4">
                  {pedido.itens?.map((item, idx) => (
                    <p key={idx} className="text-sm text-gray-600">
                      {item.quantidade}x {item.nome}
                    </p>
                  ))}
                </div>

                <div className="flex justify-between items-center">
                  <span className="font-bold text-lg text-gray-900">
                    Total: {new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' }).format(pedido.total)}
                  </span>
                  <button className="text-ifood-red text-sm font-semibold hover:underline">
                    Ver Acompanhamento
                  </button>
                </div>
              </div>
            ))}
          </div>
        )}
      </main>
    </div>
  );
}

export default MeusPedidos;
