import React, { useState, useEffect } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import {
  collection,
  getDocs,
  query,
  where,
  deleteDoc,
  doc,
  addDoc
} from 'firebase/firestore';
import { db } from '../lib/firebase';
import { CreditCard, QrCode, ArrowLeft } from 'lucide-react';
import { useAuth } from '../contexts/AuthContext';
import emailjs from 'emailjs-com';

function Pagamento() {
  const navegacao = useNavigate();
  const location = useLocation();
  const { usuario } = useAuth();

  const [formaPagamento, setFormaPagamento] = useState('');
  const [carregando, setCarregando] = useState(false);
  const [erro, setErro] = useState(null);
  const [sugestoesEndereco, setSugestoesEndereco] = useState([]);
  const [dadosPagamento, setDadosPagamento] = useState({
    numero: '',
    nome: '',
    validade: '',
    cvv: ''
  });
  const [enderecoEntrega, setEnderecoEntrega] = useState(null);
  const [enderecos, setEnderecos] = useState([]);
  const [mostrarNovoEndereco, setMostrarNovoEndereco] = useState(false);
  const [novoEndereco, setNovoEndereco] = useState({
    cep: '',
    logradouro: '',
    numero: '',
    complemento: '',
    bairro: '',
    cidade: '',
    estado: '',
    principal: false
  });
  const [total, setTotal] = useState(0);
  const [itensCarrinho, setItensCarrinho] = useState([]);

  useEffect(() => {
    if (!location.state?.total || !location.state?.itens) {
      navegacao('/home');
      return;
    }

    setTotal(location.state.total);
    setItensCarrinho(location.state.itens);

    if (usuario?.uid) {
      carregarEnderecoEntrega();
    }
  }, [location.state, usuario]);

    const carregarEnderecoEntrega = async () => {
      try {
        if (!usuario?.uid) return;

        const q = query(
          collection(db, 'enderecos'),
          where('user_id', '==', usuario.uid)
        );    

        const snapshot = await getDocs(q);

        const lista = snapshot.docs.map((item) => ({
          id: item.id,
          ...item.data()
        }));

        setEnderecos(lista);

        if (lista.length === 0) {
          setEnderecoEntrega(null);
          return;
        }

        const principal = lista.find((endereco) => endereco.principal === true);

        setEnderecoEntrega(principal || lista[0]);
      } catch (erro) {
        console.error('Erro ao carregar endereço:', erro);
        setErro('Erro ao carregar endereço de entrega');
      }
    };  

        const salvarNovoEndereco = async (e) => {
          e.preventDefault();

          try {
            if (!usuario?.uid) return;

            const dados = {
              ...novoEndereco,
              user_id: usuario.uid
            };      

            await addDoc(collection(db, 'enderecos'), dados);

            setNovoEndereco({
              cep: '',
              logradouro: '',
              numero: '',
              complemento: '',
              bairro: '',
              cidade: '',
              estado: '',
              principal: false
            });

            setMostrarNovoEndereco(false);

            await carregarEnderecoEntrega();
          } catch (erro) {
            console.error('Erro ao salvar novo endereço:', erro);
            setErro('Erro ao salvar novo endereço');
          }
        };

  const buscarCep = async (cep) => {
    try {
      const cepLimpo = cep.replace(/\D/g, '');

      if (cepLimpo.length !== 8) return;

      const response = await fetch(`https://viacep.com.br/ws/${cepLimpo}/json/`);
      const data = await response.json();

      if (data.erro) {
        setErro('CEP não encontrado');
        return;
      }

      setNovoEndereco((prev) => ({
        ...prev,
        cep: cepLimpo,
        logradouro: data.logradouro || '',
        bairro: data.bairro || '',
        cidade: data.localidade || '',
        estado: data.uf || ''
      }));
    } catch (erro) {
      console.error('Erro ao buscar CEP:', erro);
      setErro('Erro ao buscar CEP');
    }
  };

  const buscarSugestoesEndereco = async (texto) => {
    try {
      if (!window.google || !texto || texto.length < 3) {
        setSugestoesEndereco([]);
        return;
      }

      const service = new window.google.maps.places.AutocompleteService();

      service.getPlacePredictions(
        {
          input: texto,
          componentRestrictions: { country: 'br' }
        },
        (predictions, status) => {
          if (
            status !== window.google.maps.places.PlacesServiceStatus.OK ||
            !predictions
          ) {
            setSugestoesEndereco([]);
            return;
          }

          setSugestoesEndereco(predictions);
        }
      );
    } catch (erro) {
      console.error('Erro ao buscar sugestões:', erro);
      setSugestoesEndereco([]);
    }
  };

  const enviarNFe = async () => {
    const pedidoId =
      location.state?.pedidoId || Math.random().toString(36).substring(2, 11);

const orders = itensCarrinho.map((item) => ({
  name: `${item.quantidade}x ${item.produto.nome} (${item.produto.restaurante?.nome || 'Restaurante'})`,
  units: item.quantidade,
  price: (item.produto.preco * item.quantidade).toFixed(2),
  image_url: item.produto.imagemUrl || 'https://via.placeholder.com/64'
}));

    const shipping = 0.0;
    const tax = 0.0;

    await emailjs.send(
      'service_odnkte2',
      'template_en9q65a',
      {
        status_message: 'teste',
        order_id: pedidoId,
        orders,
        cost_shipping: shipping.toFixed(2),
        cost_tax: tax.toFixed(2),
        cost_total: (total + shipping + tax).toFixed(2),
        to_email: usuario?.email
      },
      'trUMBMErkFdAQ-Hfv'
    );
  };

  const processarPagamento = async () => {
    if (!enderecoEntrega) {
      setErro('Selecione ou cadastre um endereço de entrega');
      return;
    }  

    if (!formaPagamento) {
      setErro('Selecione uma forma de pagamento');
      return;
    }

    if (formaPagamento === 'cartao') {
      if (
        !dadosPagamento.numero ||
        !dadosPagamento.nome ||
        !dadosPagamento.validade ||
        !dadosPagamento.cvv
      ) {
        setErro('Preencha todos os dados do cartão');
        return;
      }
    }

    setCarregando(true);
    setErro(null);

    try {
      await new Promise((resolve) => setTimeout(resolve, 2000));

      await enviarNFe();

      for (const item of itensCarrinho) {
        await deleteDoc(doc(db, 'carrinho', item.id));
      }

      navegacao('/acompanhamento', {
        state: {
          pedidoId: Math.random().toString(36).substring(2, 11)
        }
      });
    } catch (erro) {
      console.error('Erro ao processar pagamento:', erro);
      setErro('Erro ao processar pagamento. Tente novamente.');
    } finally {
      setCarregando(false);
    }
  };

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
                onClick={() => navegacao(-1)}
                className="ml-4 flex items-center text-gray-500 hover:text-gray-700"
              >
                <ArrowLeft className="w-5 h-5 mr-2" />
                Voltar
              </button>
              <span className="ml-4 text-xl font-semibold text-gray-900">
                Pagamento
              </span>
            </div>
          </div>
        </div>
      </nav>

      <main className="max-w-4xl mx-auto py-8 px-4">
        <div className="bg-white shadow rounded-lg overflow-hidden">
          <div className="p-6 border-b border-gray-200">
            <h2 className="text-lg font-semibold text-gray-900 mb-4">
              Resumo do Pedido
            </h2>
            <div className="space-y-4">
              {itensCarrinho.map((item) => (
                <div key={item.id} className="flex justify-between items-center">
                  <div>
                    <p className="font-medium text-gray-900">
                      {item.quantidade}x {item.produto.nome}
                    </p>
                    <p className="text-sm text-gray-500">
                      {item.produto.restaurante?.nome || 'Restaurante'}
                    </p>
                  </div>
                  <p className="text-gray-900">
                    {new Intl.NumberFormat('pt-BR', {
                      style: 'currency',
                      currency: 'BRL'
                    }).format(item.produto.preco * item.quantidade)}
                  </p>
                </div>
              ))}
              <div className="pt-4 border-t border-gray-200">
                <div className="flex justify-between items-center font-semibold text-lg">
                  <span>Total</span>
                  <span className="text-ifood-red">
                    {new Intl.NumberFormat('pt-BR', {
                      style: 'currency',
                      currency: 'BRL'
                    }).format(total)}
                  </span>
                </div>
              </div>
            </div>
          </div>

          <div className="p-6 border-b border-gray-200">
          <h2 className="text-lg font-semibold text-gray-900 mb-4">
            Endereço de Entrega
          </h2>

          <div className="space-y-3">
            {enderecos.length > 0 ? (
              enderecos.map((endereco) => (
                <label
                  key={endereco.id}
                  className={`block border rounded-lg p-4 cursor-pointer transition ${
                    enderecoEntrega?.id === endereco.id
                      ? 'border-ifood-red bg-red-50'
                      : 'border-gray-300 hover:border-gray-400'
                  }`}
                >
                  <div className="flex items-start">
                    <input
                      type="radio"
                      name="enderecoEntrega"
                      checked={enderecoEntrega?.id === endereco.id}
                      onChange={() => setEnderecoEntrega(endereco)}
                      className="mt-1 mr-3"
                    />

                    <div>
                      <p className="text-gray-900 font-medium">
                        {endereco.logradouro}, {endereco.numero}
                      </p>

                      {endereco.complemento && (
                        <p className="text-gray-700">{endereco.complemento}</p>
                      )}

                      <p className="text-gray-700">
                        {endereco.bairro}, {endereco.cidade} - {endereco.estado}
                      </p>

                      <p className="text-gray-700">CEP: {endereco.cep}</p>

                      {endereco.principal && (
                        <span className="inline-block mt-2 text-xs px-2 py-1 rounded bg-green-100 text-green-700">
                          Principal
                        </span>
                      )}
                    </div>
                  </div>
                </label>
              ))
            ) : (
              <p className="text-gray-500">Nenhum endereço cadastrado</p>
            )}

            <button
              type="button"
              onClick={() => setMostrarNovoEndereco(!mostrarNovoEndereco)}
              className="text-ifood-red font-medium hover:text-red-700"
            >
              {mostrarNovoEndereco ? 'Cancelar' : '+ Cadastrar novo endereço'}
            </button>

            {mostrarNovoEndereco && (
              <form onSubmit={salvarNovoEndereco} className="mt-4 space-y-4 border rounded-lg p-4 bg-gray-50">
                <div>
                  <label className="block text-sm font-medium text-gray-700">CEP</label>
                <input
                  type="text"
                  value={novoEndereco.cep}
                  onChange={(e) =>
                    setNovoEndereco({ ...novoEndereco, cep: e.target.value })
                  }
                  onBlur={(e) => buscarCep(e.target.value)}
                  className="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-ifood-red focus:ring-ifood-red sm:text-sm"
                  required
                />
                </div>

                <div>
                  <label className="block text-sm font-medium text-gray-700">Logradouro</label>
                  <div className="relative">
                    <input
                      type="text"
                      value={novoEndereco.logradouro}
                      onChange={(e) => {
                        const valor = e.target.value;
                        setNovoEndereco({ ...novoEndereco, logradouro: valor });
                        buscarSugestoesEndereco(valor);
                      }}
                      className="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-ifood-red focus:ring-ifood-red sm:text-sm"
                      required
                    />

                    {sugestoesEndereco.length > 0 && (
                      <div className="absolute z-10 mt-1 w-full bg-white border border-gray-300 rounded-md shadow-lg max-h-48 overflow-y-auto">
                        {sugestoesEndereco.map((sugestao) => (
                          <button
                            key={sugestao.place_id}
                            type="button"
                            onClick={() => {
                              setNovoEndereco({
                                ...novoEndereco,
                                logradouro: sugestao.description
                              });
                              setSugestoesEndereco([]);
                            }}
                            className="w-full text-left px-3 py-2 hover:bg-gray-100 text-sm"
                          >
                            {sugestao.description}
                          </button>
                        ))}
                      </div>
                    )}
                  </div>
                </div>

                <div className="grid grid-cols-2 gap-4">
                  <div>
                    <label className="block text-sm font-medium text-gray-700">Número</label>
                    <input
                      type="text"
                      value={novoEndereco.numero}
                      onChange={(e) =>
                        setNovoEndereco({ ...novoEndereco, numero: e.target.value })
                      }
                      className="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-ifood-red focus:ring-ifood-red sm:text-sm"
                      required
                    />
                  </div>

                  <div>
                    <label className="block text-sm font-medium text-gray-700">Complemento</label>
                    <input
                      type="text"
                      value={novoEndereco.complemento}
                      onChange={(e) =>
                        setNovoEndereco({ ...novoEndereco, complemento: e.target.value })
                      }
                      className="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-ifood-red focus:ring-ifood-red sm:text-sm"
                    />
                  </div>

                <div>
                  <label className="block text-sm font-medium text-gray-700">Bairro</label>
                  <input
                    type="text"
                    value={novoEndereco.bairro}
                    onChange={(e) =>
                      setNovoEndereco({ ...novoEndereco, bairro: e.target.value })
                    }
                    className="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-ifood-red focus:ring-ifood-red sm:text-sm"
                    required
                  />
                </div>

                </div>

                <div className="grid grid-cols-2 gap-4">
                  <div>
                    <label className="block text-sm font-medium text-gray-700">Cidade</label>
                    <input
                      type="text"
                      value={novoEndereco.cidade}
                      onChange={(e) =>
                        setNovoEndereco({ ...novoEndereco, cidade: e.target.value })
                      }
                      className="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-ifood-red focus:ring-ifood-red sm:text-sm"
                      required
                    />
                  </div>

                  <div>
                    <label className="block text-sm font-medium text-gray-700">Estado</label>
                    <input
                      type="text"
                      value={novoEndereco.estado}
                      onChange={(e) =>
                        setNovoEndereco({ ...novoEndereco, estado: e.target.value })
                      }
                      className="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-ifood-red focus:ring-ifood-red sm:text-sm"
                      required
                    />
                  </div>
                </div>

                <div className="flex items-center">
                  <input
                    id="principal"
                    type="checkbox"
                    checked={novoEndereco.principal}
                    onChange={(e) =>
                      setNovoEndereco({ ...novoEndereco, principal: e.target.checked })
                    }
                    className="mr-2"
                  />
                  <label htmlFor="principal" className="text-sm text-gray-700">
                    Definir como endereço principal
                  </label>
                </div>

                <button
                  type="submit"
                  className="w-full py-2 px-4 rounded-md text-white bg-ifood-red hover:bg-red-700"
                >
                  Salvar novo endereço
                </button>
              </form>
            )}
          </div>
        </div>
          <div className="p-6">
            <h2 className="text-lg font-semibold text-gray-900 mb-4">
              Forma de Pagamento
            </h2>

            {erro && (
              <div className="mb-4 p-4 bg-red-100 border border-red-400 text-red-700 rounded">
                {erro}
              </div>
            )}

            <div className="grid grid-cols-2 gap-4 mb-6">
              <button
                onClick={() => setFormaPagamento('cartao')}
                className={`flex items-center justify-center p-4 rounded-lg border ${
                  formaPagamento === 'cartao'
                    ? 'border-ifood-red bg-red-50'
                    : 'border-gray-300 hover:border-gray-400'
                }`}
              >
                <CreditCard className="w-6 h-6 mr-2" />
                <span>Cartão de Crédito</span>
              </button>

              <button
                onClick={() => setFormaPagamento('pix')}
                className={`flex items-center justify-center p-4 rounded-lg border ${
                  formaPagamento === 'pix'
                    ? 'border-ifood-red bg-red-50'
                    : 'border-gray-300 hover:border-gray-400'
                }`}
              >
                <QrCode className="w-6 h-6 mr-2" />
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
                    />
                  </div>
                </div>
              </div>
            )}

            {formaPagamento === 'pix' && (
              <div className="text-center py-8">
                <QrCode className="w-48 h-48 mx-auto mb-4" />
                <p className="text-sm text-gray-600 mb-2">
                  Escaneie o QR Code com seu aplicativo de pagamento
                </p>
                <p className="text-xs text-gray-500">
                  O pedido será confirmado automaticamente após o pagamento
                </p>
              </div>
            )}

            <div className="mt-6">
              <button
                onClick={processarPagamento}
                disabled={
                  carregando ||
                  !enderecoEntrega ||
                  !formaPagamento ||
                  (formaPagamento === 'cartao' &&
                    (!dadosPagamento.numero ||
                      !dadosPagamento.nome ||
                      !dadosPagamento.validade ||
                      !dadosPagamento.cvv))
                }
                className="w-full flex justify-center py-3 px-4 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-ifood-red hover:bg-red-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-ifood-red disabled:opacity-50 disabled:cursor-not-allowed"
              >
                {carregando ? 'Processando...' : 'Finalizar Pedido'}
              </button>
            </div>
          </div>
        </div>
      </main>
    </div>
  );
}

export default Pagamento;