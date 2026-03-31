import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { createUserWithEmailAndPassword, signOut } from 'firebase/auth';
import { auth, db } from '../lib/firebase';
import { doc, setDoc, getDocs, collection, query, where } from 'firebase/firestore';

function Cadastro() {
  const navegacao = useNavigate();
  const [nome, setNome] = useState('');
  const [email, setEmail] = useState('');
  const [cpf, setCpf] = useState('');
  const [telefone, setTelefone] = useState(''); 
  const [senha, setSenha] = useState('');
  const [confirmarSenha, setConfirmarSenha] = useState('');
  const [erro, setErro] = useState(null);
  const [carregando, setCarregando] = useState(false);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setErro(null);
    setCarregando(true);

    try {
      const cpfLimpo = cpf.replace(/\D/g, '');
      const telefoneLimpo = telefone.replace(/\D/g, '');

      if (cpfLimpo.length !== 11) {
        throw new Error('CPF inválido. Digite os 11 números do CPF.');
      }
      
      if (telefoneLimpo.length < 10 || telefoneLimpo.length > 11) {
        throw new Error('Telefone inválido. Digite DDD + número.');
      }

      if (senha.length < 6) {
        throw new Error('A senha deve ter pelo menos 6 caracteres.');
      }

      if (senha !== confirmarSenha) {
        throw new Error('As senhas não coincidem.');
      }

      const usuariosRef = collection(db, 'usuarios');

      const consultaEmail = query(usuariosRef, where('email', '==', email));
      const emailSnapshot = await getDocs(consultaEmail);

      if (!emailSnapshot.empty) {
        throw new Error('Este email já está cadastrado');
      }

      const consultaCpf = query(usuariosRef, where('cpf', '==', cpfLimpo));
      const cpfSnapshot = await getDocs(consultaCpf);

      if (!cpfSnapshot.empty) {
        throw new Error('Este CPF já está cadastrado');
      }

      const credencial = await createUserWithEmailAndPassword(auth, email, senha);
      const usuario = credencial.user;

      await setDoc(doc(db, 'usuarios', usuario.uid), {
        uid: usuario.uid,
        nome,
        email,
        cpf: cpfLimpo,
        telefone: telefoneLimpo,
        created_at: new Date()
      });

      await signOut(auth);

      alert('Cadastro realizado com sucesso!');
      navegacao('/login', { replace: true });
    } catch (erro) {
      console.error('Erro ao cadastrar:', erro);

      switch (erro.code) {
        case 'auth/email-already-in-use':
          setErro('Este email já está cadastrado');
          break;
        case 'auth/invalid-email':
          setErro('E-mail inválido');
          break;
        case 'auth/weak-password':
          setErro('A senha deve ter pelo menos 6 caracteres');
          break;
        default:
          setErro(erro.message || 'Erro ao realizar cadastro. Por favor, tente novamente.');
      }
    } finally {
      setCarregando(false);
    }
  };

  return (
    <div className="min-h-screen bg-gray-50 flex flex-col justify-center py-12 sm:px-6 lg:px-8">
      <div className="sm:mx-auto sm:w-full sm:max-w-md">
        <img
          className="mx-auto h-12 w-auto"
          src="https://logodownload.org/wp-content/uploads/2017/05/ifood-logo-0.png"
          alt="iFood"
        />
        <h2 className="mt-6 text-center text-3xl font-extrabold text-gray-900">
          Criar sua conta
        </h2>
      </div>

      <div className="mt-8 sm:mx-auto sm:w-full sm:max-w-md">
        <div className="bg-white py-8 px-4 shadow sm:rounded-lg sm:px-10">
          {erro && (
            <div className="mb-4 p-3 bg-red-100 border border-red-400 text-red-700 rounded">
              {erro}
            </div>
          )}

          <form className="space-y-6" onSubmit={handleSubmit}>
            <div>
              <label htmlFor="nome" className="block text-sm font-medium text-gray-700">
                Nome completo
              </label>
              <div className="mt-1">
                <input
                  id="nome"
                  name="nome"
                  type="text"
                  required
                  value={nome}
                  onChange={(e) => setNome(e.target.value)}
                  className="appearance-none block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm placeholder-gray-400 focus:outline-none focus:ring-ifood-red focus:border-ifood-red sm:text-sm"
                />
              </div>
            </div>

            <div>
              <label htmlFor="email" className="block text-sm font-medium text-gray-700">
                E-mail
              </label>
              <div className="mt-1">
                <input
                  id="email"
                  name="email"
                  type="email"
                  required
                  value={email}
                  onChange={(e) => setEmail(e.target.value)}
                  className="appearance-none block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm placeholder-gray-400 focus:outline-none focus:ring-ifood-red focus:border-ifood-red sm:text-sm"
                />
              </div>
            </div>

            <div>
              <label htmlFor="cpf" className="block text-sm font-medium text-gray-700">
                CPF
              </label>
              <div className="mt-1">
                <input
                  id="cpf"
                  name="cpf"
                  type="text"
                  required
                  value={cpf}
                  onChange={(e) => setCpf(e.target.value.replace(/\D/g, ''))}
                  maxLength={11}
                  className="appearance-none block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm placeholder-gray-400 focus:outline-none focus:ring-ifood-red focus:border-ifood-red sm:text-sm"
                />
              </div>
            </div>

          <div>
            <label htmlFor="telefone" className="block text-sm font-medium text-gray-700">
              Telefone
            </label>
            <div className="mt-1">
              <input
                id="telefone"
                name="telefone"
                type="text"
                required
                value={telefone}
                onChange={(e) => setTelefone(e.target.value.replace(/\D/g, ''))}
                maxLength={11}
                placeholder="11999999999"
                className="appearance-none block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-ifood-red focus:border-ifood-red sm:text-sm"
              />
            </div>
          </div>

            <div>
              <label htmlFor="senha" className="block text-sm font-medium text-gray-700">
                Senha
              </label>
              <div className="mt-1">
                <input
                  id="senha"
                  name="senha"
                  type="password"
                  required
                  value={senha}
                  onChange={(e) => setSenha(e.target.value)}
                  className="appearance-none block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm placeholder-gray-400 focus:outline-none focus:ring-ifood-red focus:border-ifood-red sm:text-sm"
                />
              </div>
            </div>

            <div>
              <label htmlFor="confirmarSenha" className="block text-sm font-medium text-gray-700">
                Confirmar senha
              </label>
              <div className="mt-1">
                <input
                  id="confirmarSenha"
                  name="confirmarSenha"
                  type="password"
                  required
                  value={confirmarSenha}
                  onChange={(e) => setConfirmarSenha(e.target.value)}
                  className="appearance-none block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm placeholder-gray-400 focus:outline-none focus:ring-ifood-red focus:border-ifood-red sm:text-sm"
                />
              </div>
            </div>

            <div>
              <button
                type="submit"
                disabled={carregando}
                className="w-full flex justify-center py-2 px-4 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-ifood-red hover:bg-red-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-ifood-red disabled:opacity-50"
              >
                {carregando ? 'Cadastrando...' : 'Cadastrar'}
              </button>
            </div>
          </form>

          <div className="mt-6">
            <div className="relative">
              <p className="text-center text-sm text-gray-500">
                Já tem uma conta?{' '}
                <button
                  onClick={() => navegacao('/login')}
                  className="font-medium text-ifood-red hover:text-red-700"
                >
                  Fazer login
                </button>
              </p>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}

export default Cadastro;