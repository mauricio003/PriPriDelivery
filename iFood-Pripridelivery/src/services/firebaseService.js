import {
  collection,
  getDocs,
  getDoc,
  query,
  where,
  orderBy,
  addDoc,
  updateDoc,
  deleteDoc,
  doc,
  setDoc
} from 'firebase/firestore';
import { db } from '../lib/firebase';

/* =========================
   USUÁRIOS
========================= */

export const salvarUsuario = async (usuario, dadosExtras = {}) => {
  if (!usuario?.uid) {
    throw new Error('Usuário inválido');
  }

  await setDoc(doc(db, 'usuarios', usuario.uid), {
    uid: usuario.uid,
    email: usuario.email || '',
    ...dadosExtras,
    createdAt: new Date()
  });
};

export const carregarUsuario = async (uid) => {
  if (!uid) return null;

  const snapshot = await getDoc(doc(db, 'usuarios', uid));

  if (!snapshot.exists()) return null;

  return {
    id: snapshot.id,
    ...snapshot.data()
  };
};

/* =========================
   ENDEREÇOS
========================= */

export const carregarEnderecos = async (user) => {
  if (!user?.uid) return [];

  const q = query(
    collection(db, 'enderecos'),
    where('userId', '==', user.uid),
    orderBy('principal', 'desc'),
    orderBy('createdAt', 'desc')
  );

  const snapshot = await getDocs(q);

  return snapshot.docs.map((docItem) => ({
    id: docItem.id,
    ...docItem.data()
  }));
};

export const carregarEnderecoPrincipal = async (user) => {
  if (!user?.uid) return null;

  const q = query(
    collection(db, 'enderecos'),
    where('userId', '==', user.uid),
    where('principal', '==', true)
  );

  const snapshot = await getDocs(q);

  if (snapshot.empty) return null;

  return {
    id: snapshot.docs[0].id,
    ...snapshot.docs[0].data()
  };
};

export const salvarEndereco = async (user, enderecoAtual) => {
  if (!user?.uid) {
    throw new Error('Usuário não autenticado');
  }

  if (enderecoAtual.principal) {
    const q = query(
      collection(db, 'enderecos'),
      where('userId', '==', user.uid)
    );

    const snapshot = await getDocs(q);

    for (const item of snapshot.docs) {
      await updateDoc(doc(db, 'enderecos', item.id), {
        principal: false
      });
    }
  }

  const dadosEndereco = {
    cep: enderecoAtual.cep || '',
    logradouro: enderecoAtual.logradouro || '',
    numero: enderecoAtual.numero || '',
    complemento: enderecoAtual.complemento || '',
    bairro: enderecoAtual.bairro || '',
    cidade: enderecoAtual.cidade || '',
    estado: enderecoAtual.estado || '',
    principal: Boolean(enderecoAtual.principal),
    userId: user.uid
  };

  if (enderecoAtual.id) {
    await updateDoc(doc(db, 'enderecos', enderecoAtual.id), dadosEndereco);
  } else {
    await addDoc(collection(db, 'enderecos'), {
      ...dadosEndereco,
      createdAt: new Date()
    });
  }
};

export const excluirEndereco = async (id) => {
  if (!id) return;
  await deleteDoc(doc(db, 'enderecos', id));
};

/* =========================
   RESTAURANTES
========================= */

export const carregarRestaurantes = async (user) => {
  if (!user?.uid) return [];

  const q = query(
    collection(db, 'restaurantes'),
    where('userId', '==', user.uid),
    orderBy('createdAt', 'desc')
  );

  const snapshot = await getDocs(q);

  return snapshot.docs.map((docItem) => ({
    id: docItem.id,
    ...docItem.data()
  }));
};

export const carregarTodosRestaurantes = async () => {
  const snapshot = await getDocs(collection(db, 'restaurantes'));

  return snapshot.docs.map((docItem) => ({
    id: docItem.id,
    ...docItem.data()
  }));
};

export const carregarRestaurantePorId = async (restauranteId) => {
  if (!restauranteId) {
    throw new Error('ID do restaurante não informado');
  }

  const snapshot = await getDoc(doc(db, 'restaurantes', restauranteId));

  if (!snapshot.exists()) {
    throw new Error('Restaurante não encontrado');
  }

  return {
    id: snapshot.id,
    ...snapshot.data()
  };
};

export const salvarRestaurante = async (user, restauranteAtual) => {
  if (!user?.uid) {
    throw new Error('Usuário não autenticado');
  }

  const dadosRestaurante = {
    nome: restauranteAtual.nome || '',
    descricao: restauranteAtual.descricao || '',
    categoria: restauranteAtual.categoria || '',
    horario_abertura: restauranteAtual.horario_abertura || '',
    horario_fechamento: restauranteAtual.horario_fechamento || '',
    imagemUrl: restauranteAtual.imagemUrl || '',
    userId: user.uid,
    taxaEntregaNormal: parseFloat(restauranteAtual.taxaEntregaNormal || 0),
    taxaEntregaRapida: parseFloat(restauranteAtual.taxaEntregaRapida || 0),
    tempoEntregaNormal: parseInt(restauranteAtual.tempoEntregaNormal || 0),
    tempoEntregaRapida: parseInt(restauranteAtual.tempoEntregaRapida || 0)
  };

  if (restauranteAtual.id) {
    await updateDoc(doc(db, 'restaurantes', restauranteAtual.id), dadosRestaurante);
  } else {
    await addDoc(collection(db, 'restaurantes'), {
      ...dadosRestaurante,
      createdAt: new Date()
    });
  }
};

export const excluirRestaurante = async (id) => {
  if (!id) return;
  await deleteDoc(doc(db, 'restaurantes', id));
};

/* =========================
   PRODUTOS
========================= */

export const carregarProdutos = async (restauranteId) => {
  if (!restauranteId) return [];

  const q = query(
    collection(db, 'produtos'),
    where('restauranteId', '==', restauranteId),
    orderBy('createdAt', 'desc')
  );

  const snapshot = await getDocs(q);

  return snapshot.docs.map((docItem) => ({
    id: docItem.id,
    ...docItem.data()
  }));
};

export const carregarProdutosDisponiveis = async (restauranteId) => {
  if (!restauranteId) return [];

  const q = query(
    collection(db, 'produtos'),
    where('restauranteId', '==', restauranteId),
    where('disponivel', '==', true),
    orderBy('categoria', 'asc')
  );

  const snapshot = await getDocs(q);

  return snapshot.docs.map((docItem) => ({
    id: docItem.id,
    ...docItem.data()
  }));
};

export const carregarProdutoPorId = async (produtoId) => {
  if (!produtoId) return null;

  const snapshot = await getDoc(doc(db, 'produtos', produtoId));

  if (!snapshot.exists()) return null;

  return {
    id: snapshot.id,
    ...snapshot.data()
  };
};

export const salvarProduto = async (restauranteId, produtoAtual) => {
  if (!restauranteId) {
    throw new Error('Restaurante não informado');
  }

  const dadosProduto = {
    nome: produtoAtual.nome || '',
    descricao: produtoAtual.descricao || '',
    preco: parseFloat(produtoAtual.preco || 0),
    categoria: produtoAtual.categoria || '',
    imagemUrl: produtoAtual.imagemUrl || '',
    disponivel: Boolean(produtoAtual.disponivel),
    restauranteId
  };

  if (produtoAtual.id) {
    await updateDoc(doc(db, 'produtos', produtoAtual.id), dadosProduto);
  } else {
    await addDoc(collection(db, 'produtos'), {
      ...dadosProduto,
      createdAt: new Date()
    });
  }
};

export const excluirProduto = async (id) => {
  if (!id) return;
  await deleteDoc(doc(db, 'produtos', id));
};

/* =========================
   CARRINHO
========================= */

export const carregarCarrinho = async (user) => {
  if (!user?.uid) return [];

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
      const produtoSnap = await getDoc(doc(db, 'produtos', item.produtoId));

      if (!produtoSnap.exists()) return null;

      const produto = {
        id: produtoSnap.id,
        ...produtoSnap.data()
      };

      let restaurante = null;

      if (produto.restauranteId) {
        const restauranteSnap = await getDoc(
          doc(db, 'restaurantes', produto.restauranteId)
        );

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
    })
  );

  return itensComProduto.filter(Boolean);
};

export const adicionarAoCarrinho = async (user, produto, quantidade = 1) => {
  if (!user?.uid) {
    throw new Error('Usuário não autenticado');
  }

  const q = query(
    collection(db, 'carrinho'),
    where('userId', '==', user.uid),
    where('produtoId', '==', produto.id)
  );

  const snapshot = await getDocs(q);

  if (!snapshot.empty) {
    const itemExistente = snapshot.docs[0];
    const dados = itemExistente.data();

    await updateDoc(doc(db, 'carrinho', itemExistente.id), {
      quantidade: (dados.quantidade || 1) + quantidade
    });
  } else {
    await addDoc(collection(db, 'carrinho'), {
      userId: user.uid,
      produtoId: produto.id,
      restauranteId: produto.restauranteId || '',
      nomeProduto: produto.nome || '',
      imagemUrl: produto.imagemUrl || '',
      preco: parseFloat(produto.preco || 0),
      quantidade,
      createdAt: new Date()
    });
  }
};

export const aumentarQuantidadeCarrinho = async (itemId, quantidadeAtual) => {
  if (!itemId) return;

  await updateDoc(doc(db, 'carrinho', itemId), {
    quantidade: quantidadeAtual + 1
  });
};

export const diminuirQuantidadeCarrinho = async (itemId, quantidadeAtual) => {
  if (!itemId) return;

  if (quantidadeAtual <= 1) {
    await deleteDoc(doc(db, 'carrinho', itemId));
    return;
  }

  await updateDoc(doc(db, 'carrinho', itemId), {
    quantidade: quantidadeAtual - 1
  });
};

export const removerItemCarrinho = async (itemId) => {
  if (!itemId) return;
  await deleteDoc(doc(db, 'carrinho', itemId));
};

export const limparCarrinho = async (user) => {
  if (!user?.uid) return;

  const q = query(
    collection(db, 'carrinho'),
    where('userId', '==', user.uid)
  );

  const snapshot = await getDocs(q);

  for (const item of snapshot.docs) {
    await deleteDoc(doc(db, 'carrinho', item.id));
  }
};

export const calcularTotalCarrinho = (itens) => {
  return itens.reduce((acc, item) => {
    return acc + (item.produto.preco * item.quantidade);
  }, 0);
};

/* =========================
   PEDIDOS
========================= */

export const criarPedido = async ({
  user,
  itens,
  total,
  endereco,
  formaPagamento
}) => {
  if (!user?.uid) {
    throw new Error('Usuário não autenticado');
  }

  const pedidoRef = await addDoc(collection(db, 'pedidos'), {
    userId: user.uid,
    itens,
    total,
    endereco,
    formaPagamento,
    status: 'recebido',
    createdAt: new Date()
  });

  return pedidoRef.id;
};

export const carregarPedidos = async (user) => {
  if (!user?.uid) return [];

  const q = query(
    collection(db, 'pedidos'),
    where('userId', '==', user.uid),
    orderBy('createdAt', 'desc')
  );

  const snapshot = await getDocs(q);

  return snapshot.docs.map((docItem) => ({
    id: docItem.id,
    ...docItem.data()
  }));
};

export const carregarPedidoPorId = async (pedidoId) => {
  if (!pedidoId) return null;

  const snapshot = await getDoc(doc(db, 'pedidos', pedidoId));

  if (!snapshot.exists()) return null;

  return {
    id: snapshot.id,
    ...snapshot.data()
  };
};

export const atualizarStatusPedido = async (pedidoId, status) => {
  if (!pedidoId) return;

  await updateDoc(doc(db, 'pedidos', pedidoId), {
    status
  });
};

/* =========================
   HELPERS
========================= */

export const formatarMoeda = (valor) => {
  return new Intl.NumberFormat('pt-BR', {
    style: 'currency',
    currency: 'BRL'
  }).format(valor || 0);
};