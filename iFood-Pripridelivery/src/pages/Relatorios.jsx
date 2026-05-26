import React, { useState, useEffect } from 'react';
import DashboardLayout from '../components/DashboardLayout';
import TopBar from '../components/TopBar';
import KpiCard from '../components/KpiCard';
import { useNavigate } from 'react-router-dom';
import { collection, getDocs } from 'firebase/firestore';
import { db } from '../lib/firebase';
import { 
  ArrowLeft, 
  TrendingUp, 
  DollarSign, 
  ShoppingBag, 
  Users, 
  Store, 
  Award, 
  Calendar, 
  Loader2 
} from 'lucide-react';

function Relatorios() {
  const navegacao = useNavigate();
  const [pedidos, setPedidos] = useState([]);
  const [usuarios, setUsuarios] = useState([]);
  const [carregando, setCarregando] = useState(true);
  const [abaAtiva, setAbaAtiva] = useState('faturamento'); // 'faturamento' | 'restaurantes' | 'clientes'
  const [filtroDias, setFiltroDias] = useState('30'); // '7' | '30' | 'total'

  useEffect(() => {
    async function carregarDados() {
      try {
        setCarregando(true);
        // Busca todos os pedidos e usuários cadastrados
        const [pedidosSnap, usuariosSnap] = await Promise.all([
          getDocs(collection(db, 'pedidos')),
          getDocs(collection(db, 'usuarios'))
        ]);

        const listaPedidos = pedidosSnap.docs.map(doc => ({
          id: doc.id,
          ...doc.data()
        }));

        const listaUsuarios = usuariosSnap.docs.map(doc => ({
          id: doc.id,
          ...doc.data()
        }));

        setPedidos(listaPedidos);
        setUsuarios(listaUsuarios);
      } catch (error) {
        console.error("Erro ao carregar dados dos relatórios:", error);
      } finally {
        setCarregando(false);
      }
    }
    carregarDados();
  }, []);

  // Filtra os pedidos com base no período selecionado
  const filtrarPedidos = () => {
    if (filtroDias === 'total') return pedidos;

    const limiteData = new Date();
    limiteData.setDate(limiteData.getDate() - parseInt(filtroDias));

    return pedidos.filter(pedido => {
      if (!pedido.createdAt) return false;
      const dataPedido = new Date(pedido.createdAt);
      return dataPedido >= limiteData;
    });
  };

  const pedidosFiltrados = filtrarPedidos();

  // --- CÁLCULO DE MÉTRICAS GERAIS (KPIs) ---
  const faturamentoTotal = pedidosFiltrados.reduce((acc, p) => acc + (p.total || 0), 0);
  const totalPedidos = pedidosFiltrados.length;
  const ticketMedio = totalPedidos > 0 ? faturamentoTotal / totalPedidos : 0;
  const clientesAtivos = new Set(pedidosFiltrados.map(p => p.user_id)).size;

  // --- REPORT 1: FATURAMENTO DIÁRIO E MEIOS DE PAGAMENTO ---
  const obterFaturamentoDiario = () => {
    const faturamento = {};
    pedidosFiltrados.forEach(p => {
      if (!p.createdAt) return;
      const dataStr = new Date(p.createdAt).toLocaleDateString('pt-BR');
      faturamento[dataStr] = (faturamento[dataStr] || 0) + (p.total || 0);
    });

    return Object.entries(faturamento)
      .map(([data, valor]) => ({ data, valor }))
      .sort((a, b) => {
        const [diaA, mesA, anoA] = a.data.split('/');
        const [diaB, mesB, anoB] = b.data.split('/');
        return new Date(anoB, mesB - 1, diaB) - new Date(anoA, mesA - 1, diaA);
      });
  };

  const obterMeiosPagamento = () => {
    const faturamento = {};
    pedidosFiltrados.forEach(p => {
      const forma = p.forma_pagamento || 'Não Definido';
      // Tradução simples para exibição amigável
      const formaFormatada = forma === 'cartao' ? 'Cartão de Crédito' : forma.toUpperCase();
      faturamento[formaFormatada] = (faturamento[formaFormatada] || 0) + (p.total || 0);
    });

    return Object.entries(faturamento)
      .map(([nome, valor]) => ({
        nome,
        valor,
        porcentagem: faturamentoTotal > 0 ? (valor / faturamentoTotal) * 100 : 0
      }))
      .sort((a, b) => b.valor - a.valor);
  };

  // --- REPORT 2: PERFORMANCE POR RESTAURANTE ---
  const obterPerformanceRestaurantes = () => {
    const dados = {};
    pedidosFiltrados.forEach(p => {
      const restId = p.restaurante_id || 'desconhecido';
      const restNome = p.restaurante_nome || 'Outros / Legado';

      if (!dados[restId]) {
        dados[restId] = { nome: restNome, faturamento: 0, totalPedidos: 0 };
      }
      dados[restId].faturamento += p.total || 0;
      dados[restId].totalPedidos += 1;
    });

    return Object.values(dados)
      .map(r => ({
        ...r,
        ticketMedio: r.totalPedidos > 0 ? r.faturamento / r.totalPedidos : 0,
        participacao: faturamentoTotal > 0 ? (r.faturamento / faturamentoTotal) * 100 : 0
      }))
      .sort((a, b) => b.faturamento - a.faturamento);
  };

  // --- REPORT 3: RANKING DE CLIENTES ---
  const obterRankingClientes = () => {
    const dados = {};
    pedidosFiltrados.forEach(p => {
      const uid = p.user_id;
      if (!uid) return;

      if (!dados[uid]) {
        const cadastro = usuarios.find(u => u.id === uid);
        dados[uid] = {
          nome: cadastro?.nome || 'Cliente não cadastrado',
          email: cadastro?.email || 'Sem e-mail',
          totalGasto: 0,
          totalPedidos: 0
        };
      }
      dados[uid].totalGasto += p.total || 0;
      dados[uid].totalPedidos += 1;
    });

    return Object.values(dados)
      .map(c => ({
        ...c,
        ticketMedio: c.totalPedidos > 0 ? c.totalGasto / c.totalPedidos : 0
      }))
      .sort((a, b) => b.totalGasto - a.totalGasto);
  };

  const formatarMoeda = (valor) => {
    return new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' }).format(valor);
  };

  return (
    <DashboardLayout>
  <TopBar />
  {carregando ? (
    <div className="flex items-center justify-center min-h-[calc(100vh-4rem)]">
      <div className="text-center">
        <Loader2 className="w-10 h-10 animate-spin text-red-600 mx-auto mb-4" />
        <p className="text-gray-500">Processando e gerando relatórios...</p>
      </div>
    </div>
  ) : (
    <main className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
      {/* Navbar */}
      <nav className="bg-white shadow-sm sticky top-0 z-10">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex justify-between h-16">
            <div className="flex items-center">
              <button
                onClick={() => navegacao('/home')}
                className="mr-4 flex items-center text-gray-500 hover:text-gray-700 transition-colors"
              >
                <ArrowLeft className="w-5 h-5" />
              </button>
              <h1 className="text-xl font-bold text-gray-900 flex items-center">
                <TrendingUp className="w-6 h-6 mr-2 text-red-600" />
                Painel de Relatórios
              </h1>
            </div>
            
            {/* Seletor de Período */}
            <div className="flex items-center space-x-2">
              <Calendar className="w-4 h-4 text-gray-400" />
              <select
                value={filtroDias}
                onChange={(e) => setFiltroDias(e.target.value)}
                className="text-sm border-gray-300 rounded-md focus:ring-red-500 focus:border-red-500 bg-white"
              >
                <option value="7">Últimos 7 dias</option>
                <option value="30">Últimos 30 dias</option>
                <option value="total">Todo o período</option>
              </select>
            </div>
          </div>
        </div>
      </nav>

      {carregando ? (
        <div className="flex items-center justify-center min-h-[calc(100vh-4rem)]">
          <div className="text-center">
            <Loader2 className="w-10 h-10 animate-spin text-red-600 mx-auto mb-4" />
            <p className="text-gray-500">Processando e gerando relatórios...</p>
          </div>
        </div>
      ) : (
        <main className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
          
          {/* Métricas Principais (Cards) */}
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6 mb-8">
            <div className="bg-white p-6 rounded-2xl shadow-sm border border-gray-100 flex items-center">
              <div className="p-3 rounded-xl bg-red-50 text-red-600 mr-4">
                <DollarSign className="w-6 h-6" />
              </div>
              <div>
                <p className="text-sm font-medium text-gray-500">Faturamento Total</p>
                <p className="text-2xl font-bold text-gray-900">{formatarMoeda(faturamentoTotal)}</p>
              </div>
            </div>

            <div className="bg-white p-6 rounded-2xl shadow-sm border border-gray-100 flex items-center">
              <div className="p-3 rounded-xl bg-blue-50 text-blue-600 mr-4">
                <ShoppingBag className="w-6 h-6" />
              </div>
              <div>
                <p className="text-sm font-medium text-gray-500">Total de Pedidos</p>
                <p className="text-2xl font-bold text-gray-900">{totalPedidos}</p>
              </div>
            </div>

            <div className="bg-white p-6 rounded-2xl shadow-sm border border-gray-100 flex items-center">
              <div className="p-3 rounded-xl bg-green-50 text-green-600 mr-4">
                <TrendingUp className="w-6 h-6" />
              </div>
              <div>
                <p className="text-sm font-medium text-gray-500">Ticket Médio</p>
                <p className="text-2xl font-bold text-gray-900">{formatarMoeda(ticketMedio)}</p>
              </div>
            </div>

            <div className="bg-white p-6 rounded-2xl shadow-sm border border-gray-100 flex items-center">
              <div className="p-3 rounded-xl bg-purple-50 text-purple-600 mr-4">
                <Users className="w-6 h-6" />
              </div>
              <div>
                <p className="text-sm font-medium text-gray-500">Clientes Ativos</p>
                <p className="text-2xl font-bold text-gray-900">{clientesAtivos}</p>
              </div>
            </div>
          </div>

          {/* Abas de Navegação interna dos Relatórios */}
          <div className="border-b border-gray-200 mb-6">
            <nav className="-mb-px flex space-x-8">
              <button
                onClick={() => setAbaAtiva('faturamento')}
                className={`pb-4 px-1 border-b-2 font-medium text-sm transition-all ${
                  abaAtiva === 'faturamento'
                    ? 'border-red-500 text-red-600 font-semibold'
                    : 'border-transparent text-gray-500 hover:text-gray-700 hover:border-gray-300'
                }`}
              >
                📊 Faturamento & Pagamentos
              </button>
              <button
                onClick={() => setAbaAtiva('restaurantes')}
                className={`pb-4 px-1 border-b-2 font-medium text-sm transition-all ${
                  abaAtiva === 'restaurantes'
                    ? 'border-red-500 text-red-600 font-semibold'
                    : 'border-transparent text-gray-500 hover:text-gray-700 hover:border-gray-300'
                }`}
              >
                🏪 Desempenho por Restaurante
              </button>
              <button
                onClick={() => setAbaAtiva('clientes')}
                className={`pb-4 px-1 border-b-2 font-medium text-sm transition-all ${
                  abaAtiva === 'clientes'
                    ? 'border-red-500 text-red-600 font-semibold'
                    : 'border-transparent text-gray-500 hover:text-gray-700 hover:border-gray-300'
                }`}
              >
                👥 Ranking de Clientes
              </button>
            </nav>
          </div>

          {/* Conteúdo das Abas */}
          <div className="transition-all duration-300">
            {/* ABA 1: FATURAMENTO & PAGAMENTOS */}
            {abaAtiva === 'faturamento' && (
              <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
                {/* Tabela de Vendas Diárias */}
                <div className="bg-white p-6 rounded-2xl shadow-sm border border-gray-100 lg:col-span-2">
                  <h3 className="text-lg font-bold text-gray-900 mb-4">Vendas por Dia</h3>
                  {obterFaturamentoDiario().length === 0 ? (
                    <p className="text-gray-500 text-center py-8">Nenhum dado encontrado no período.</p>
                  ) : (
                    <div className="overflow-x-auto max-h-96 overflow-y-auto">
                      <table className="min-w-full divide-y divide-gray-200">
                        <thead className="bg-gray-50">
                          <tr>
                            <th className="px-6 py-3 text-left text-xs font-semibold text-gray-500 uppercase tracking-wider">Data</th>
                            <th className="px-6 py-3 text-right text-xs font-semibold text-gray-500 uppercase tracking-wider">Valor Faturado</th>
                          </tr>
                        </thead>
                        <tbody className="bg-white divide-y divide-gray-200">
                          {obterFaturamentoDiario().map((dia, index) => (
                            <tr key={index} className="hover:bg-gray-50 transition-colors">
                              <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">{dia.data}</td>
                              <td className="px-6 py-4 whitespace-nowrap text-sm text-right font-bold text-gray-900">{formatarMoeda(dia.valor)}</td>
                            </tr>
                          ))}
                        </tbody>
                      </table>
                    </div>
                  )}
                </div>

                {/* Meios de Pagamento (Visual Bars) */}
                <div className="bg-white p-6 rounded-2xl shadow-sm border border-gray-100">
                  <h3 className="text-lg font-bold text-gray-900 mb-4">Meios de Pagamento</h3>
                  {obterMeiosPagamento().length === 0 ? (
                    <p className="text-gray-500 text-center py-8">Sem dados registrados.</p>
                  ) : (
                    <div className="space-y-6">
                      {obterMeiosPagamento().map((pgto, index) => (
                        <div key={index}>
                          <div className="flex justify-between text-sm font-medium mb-1">
                            <span className="text-gray-600">{pgto.nome}</span>
                            <span className="font-bold">{formatarMoeda(pgto.valor)} ({pgto.porcentagem.toFixed(1)}%)</span>
                          </div>
                          <div className="w-full bg-gray-100 rounded-full h-2">
                            <div 
                              className="bg-red-600 h-2 rounded-full transition-all duration-500" 
                              style={{ width: `${pgto.porcentagem}%` }}
                            />
                          </div>
                        </div>
                      ))}
                    </div>
                  )}
                </div>
              </div>
            )}

            {/* ABA 2: DESEMPENHO POR RESTAURANTE */}
            {abaAtiva === 'restaurantes' && (
              <div className="bg-white p-6 rounded-2xl shadow-sm border border-gray-100">
                <div className="flex items-center justify-between mb-4">
                  <h3 className="text-lg font-bold text-gray-900">Rank de Vendas de Restaurantes</h3>
                  <Store className="w-5 h-5 text-gray-400" />
                </div>
                {obterPerformanceRestaurantes().length === 0 ? (
                  <p className="text-gray-500 text-center py-8">Nenhum restaurante faturou no período.</p>
                ) : (
                  <div className="overflow-x-auto">
                    <table className="min-w-full divide-y divide-gray-200">
                      <thead className="bg-gray-50">
                        <tr>
                          <th className="px-6 py-3 text-left text-xs font-semibold text-gray-500 uppercase tracking-wider">Restaurante</th>
                          <th className="px-6 py-3 text-center text-xs font-semibold text-gray-500 uppercase tracking-wider">Pedidos Solicitados</th>
                          <th className="px-6 py-3 text-right text-xs font-semibold text-gray-500 uppercase tracking-wider">Ticket Médio</th>
                          <th className="px-6 py-3 text-right text-xs font-semibold text-gray-500 uppercase tracking-wider">Faturamento</th>
                          <th className="px-6 py-3 text-right text-xs font-semibold text-gray-500 uppercase tracking-wider">Quota do Faturamento</th>
                        </tr>
                      </thead>
                      <tbody className="bg-white divide-y divide-gray-200">
                        {obterPerformanceRestaurantes().map((rest, index) => (
                          <tr key={index} className="hover:bg-gray-50 transition-colors">
                            <td className="px-6 py-4 whitespace-nowrap text-sm font-semibold text-gray-900">{rest.nome}</td>
                            <td className="px-6 py-4 whitespace-nowrap text-sm text-center text-gray-600">{rest.totalPedidos}</td>
                            <td className="px-6 py-4 whitespace-nowrap text-sm text-right text-gray-600">{formatarMoeda(rest.ticketMedio)}</td>
                            <td className="px-6 py-4 whitespace-nowrap text-sm text-right font-bold text-gray-900">{formatarMoeda(rest.faturamento)}</td>
                            <td className="px-6 py-4 whitespace-nowrap text-right text-sm">
                              <span className="inline-block px-2.5 py-0.5 rounded-full text-xs font-semibold bg-red-100 text-red-800">
                                {rest.participacao.toFixed(1)}% do total
                              </span>
                            </td>
                          </tr>
                        ))}
                      </tbody>
                    </table>
                  </div>
                )}
              </div>
            )}

            {/* ABA 3: RANKING DE CLIENTES */}
            {abaAtiva === 'clientes' && (
              <div className="bg-white p-6 rounded-2xl shadow-sm border border-gray-100">
                <div className="flex items-center justify-between mb-4">
                  <h3 className="text-lg font-bold text-gray-900">Leaderboard de Clientes</h3>
                  <Award className="w-5 h-5 text-gray-400" />
                </div>
                {obterRankingClientes().length === 0 ? (
                  <p className="text-gray-500 text-center py-8">Nenhum pedido efetuado no período.</p>
                ) : (
                  <div className="overflow-x-auto">
                    <table className="min-w-full divide-y divide-gray-200">
                      <thead className="bg-gray-50">
                        <tr>
                          <th className="px-6 py-3 text-left text-xs font-semibold text-gray-500 uppercase tracking-wider">Rank</th>
                          <th className="px-6 py-3 text-left text-xs font-semibold text-gray-500 uppercase tracking-wider">Nome</th>
                          <th className="px-6 py-3 text-left text-xs font-semibold text-gray-500 uppercase tracking-wider">E-mail</th>
                          <th className="px-6 py-3 text-center text-xs font-semibold text-gray-500 uppercase tracking-wider">Total de Pedidos</th>
                          <th className="px-6 py-3 text-right text-xs font-semibold text-gray-500 uppercase tracking-wider">Valor Gasto</th>
                          <th className="px-6 py-3 text-right text-xs font-semibold text-gray-500 uppercase tracking-wider">Média p/ Pedido</th>
                        </tr>
                      </thead>
                      <tbody className="bg-white divide-y divide-gray-200">
                        {obterRankingClientes().map((cli, index) => (
                          <tr key={index} className="hover:bg-gray-50 transition-colors">
                            <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500 font-semibold">
                              {index === 0 ? '🥇 1º' : index === 1 ? '🥈 2º' : index === 2 ? '🥉 3º' : `${index + 1}º`}
                            </td>
                            <td className="px-6 py-4 whitespace-nowrap text-sm font-semibold text-gray-900">{cli.nome}</td>
                            <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-600">{cli.email}</td>
                            <td className="px-6 py-4 whitespace-nowrap text-sm text-center text-gray-600">{cli.totalPedidos}</td>
                            <td className="px-6 py-4 whitespace-nowrap text-sm text-right font-bold text-red-600">{formatarMoeda(cli.totalGasto)}</td>
                            <td className="px-6 py-4 whitespace-nowrap text-sm text-right text-gray-600">{formatarMoeda(cli.ticketMedio)}</td>
                          </tr>
                        ))}
                      </tbody>
                    </table>
                  </div>
                )}
              </div>
            )}
          </div>
        </main>
      )}
    </div>
  );
}

export default Relatorios;
