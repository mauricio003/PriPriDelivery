import React from 'react';
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import { Toaster } from 'react-hot-toast';
import PaginaLogin from './pages/PaginaLogin';
import Cadastro from './pages/Cadastro';
import Endereco from './pages/Endereco';
import Restaurante from './pages/Restaurante';
import Produtos from './pages/Produtos';
import ProdutosParaCarrinho from './pages/ProdutosParaCarrinho';
import Pagamento from './pages/Pagamento';
import AcompanhamentoPedido from './pages/AcompanhamentoPedido';
import Home from './pages/Home';
import { ProvedorAutenticacao } from './contexts/AuthContext';
import { useAuth } from './contexts/AuthContext';

function RotaProtegida({ children }) {
  const { estaAutenticado, carregando } = useAuth();

  if (carregando) {
    return <div>Carregando...</div>;
  }

  if (!estaAutenticado) {
    return <Navigate to="/login" replace />;
  }

  return children;
}

function App() {
  return (
    <ProvedorAutenticacao>
      <Toaster position="top-center" reverseOrder={false} />
      <BrowserRouter
        future={{
          v7_startTransition: true,
          v7_relativeSplatPath: true,
        }}
      >
        <Routes>
          <Route path="/" element={<Cadastro />} />
          <Route path="/cadastro" element={<Cadastro />} />
          <Route path="/login" element={<PaginaLogin />} />
          <Route
            path="/home"
            element={
              <RotaProtegida>
                <Home />
              </RotaProtegida>
            }
          />
          <Route
            path="/endereco"
            element={
              <RotaProtegida>
                <Endereco />
              </RotaProtegida>
            }
          />
          <Route
            path="/restaurante"
            element={
              <RotaProtegida>
                <Restaurante />
              </RotaProtegida>
            }
          />
          <Route
            path="/restaurante/:restauranteId/produtos"
            element={
              <RotaProtegida>
                <Produtos />
              </RotaProtegida>
            }
          />
          <Route
            path="/restaurante/:restauranteId/comprar"
            element={
              <RotaProtegida>
                <ProdutosParaCarrinho />
              </RotaProtegida>
            }
          />
          <Route
            path="/pagamento"
            element={
              <RotaProtegida>
                <Pagamento />
              </RotaProtegida>
            }
          />
          <Route
            path="/acompanhamento"
            element={
              <RotaProtegida>
                <AcompanhamentoPedido />
              </RotaProtegida>
            }
          />
          <Route path="*" element={<Navigate to="/login" replace />} />
        </Routes>
      </BrowserRouter>
    </ProvedorAutenticacao>
  );
}

export default App;