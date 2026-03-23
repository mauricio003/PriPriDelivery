import React, { createContext, useContext, useEffect, useState } from 'react';
import { onAuthStateChanged, signOut } from 'firebase/auth';
import { auth } from '../lib/firebase';

const ContextoAutenticacao = createContext({});

export function ProvedorAutenticacao({ children }) {
  const [usuario, setUsuario] = useState(null);
  const [carregando, setCarregando] = useState(true);

  useEffect(() => {
    const unsubscribe = onAuthStateChanged(auth, (user) => {
      setUsuario(user || null);
      setCarregando(false);
    });

    return () => unsubscribe();
  }, []);

  const logout = async () => {
    try {
      await signOut(auth);
      setUsuario(null);
    } catch (erro) {
      console.error('Erro ao fazer logout:', erro);
    }
  };

  const valor = {
    usuario,
    carregando,
    estaAutenticado: !!usuario,
    logout
  };

  return (
    <ContextoAutenticacao.Provider value={valor}>
      {!carregando && children}
    </ContextoAutenticacao.Provider>
  );
}

export function useAuth() {
  const contexto = useContext(ContextoAutenticacao);

  if (!contexto) {
    throw new Error('useAuth deve ser usado dentro de um ProvedorAutenticacao');
  }

  return contexto;
}