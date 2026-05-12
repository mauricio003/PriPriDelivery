import { initializeApp, cert } from 'firebase-admin/app';
import { getFirestore } from 'firebase-admin/firestore';
import dotenv from 'dotenv';
import { readFileSync } from 'fs';

dotenv.config();

// Para rodar este script, você precisa baixar o arquivo JSON de conta de serviço 
// do console do Firebase (Configurações do Projeto > Contas de Serviço > Gerar nova chave privada)
// e salvar como 'serviceAccountKey.json' na raiz do projeto.

try {
  const serviceAccount = JSON.parse(readFileSync('./serviceAccountKey.json', 'utf8'));

  initializeApp({
    credential: cert(serviceAccount)
  });

  const db = getFirestore();

  const restaurantes = [
    {
      nome: "Burger House",
      categoria: "Lanche",
      descricao: "Os melhores hambúrgueres artesanais da cidade.",
      horario_abertura: "11:00",
      horario_fechamento: "23:00",
      imagemUrl: "https://images.unsplash.com/photo-1571091718767-18b5b1457add?w=500&auto=format&fit=crop",
      taxaEntregaNormal: 5.0,
      taxaEntregaRapida: 8.0,
      tempoEntregaNormal: 45,
      tempoEntregaRapida: 25,
      userId: "admin_seed"
    },
    {
      nome: "Pizza Express",
      categoria: "Pizza",
      descricao: "Pizza quentinha e crocante em minutos.",
      horario_abertura: "18:00",
      horario_fechamento: "00:00",
      imagemUrl: "https://images.unsplash.com/photo-1513104890138-7c749659a591?w=500&auto=format&fit=crop",
      taxaEntregaNormal: 6.0,
      taxaEntregaRapida: 10.0,
      tempoEntregaNormal: 50,
      tempoEntregaRapida: 30,
      userId: "admin_seed"
    },
    {
      nome: "Sushi Zen",
      categoria: "Japonesa",
      descricao: "Sashimis e sushis frescos todos os dias.",
      horario_abertura: "12:00",
      horario_fechamento: "22:00",
      imagemUrl: "https://images.unsplash.com/photo-1579871494447-9811cf80d66c?w=500&auto=format&fit=crop",
      taxaEntregaNormal: 7.0,
      taxaEntregaRapida: 12.0,
      tempoEntregaNormal: 60,
      tempoEntregaRapida: 40,
      userId: "admin_seed"
    }
  ];

  const popular = async () => {
    console.log("🚀 Iniciando população do banco de dados...");

    for (const rest of restaurantes) {
      const docRef = await db.collection('restaurantes').add({
        ...rest,
        createdAt: new Date()
      });
      console.log(`✅ Restaurante adicionado: ${rest.nome} (ID: ${docRef.id})`);

      // Adicionar produtos para cada restaurante
      const produtos = [
        {
          nome: `Combo ${rest.nome} Especial`,
          descricao: "Acompanha batata e refrigerante.",
          preco: 35.90,
          imagemUrl: rest.imagemUrl,
          restaurante_id: docRef.id,
          categoria: "Destaque"
        },
        {
          nome: `${rest.nome} Classic`,
          descricao: "A opção favorita dos nossos clientes.",
          preco: 22.50,
          imagemUrl: rest.imagemUrl,
          restaurante_id: docRef.id,
          categoria: "Individual"
        }
      ];

      for (const prod of produtos) {
        await db.collection('produtos').add(prod);
      }
      console.log(`   📦 Produtos adicionados para ${rest.nome}`);
    }

    console.log("✨ Banco de dados populado com sucesso!");
    process.exit();
  };

  popular();

} catch (error) {
  console.error("❌ Erro ao rodar script de semente:", error.message);
  console.log("\n💡 DICA: Certifique-se de que você tem o arquivo 'serviceAccountKey.json' na raiz do projeto.");
  console.log("💡 E instale o firebase-admin: npm install firebase-admin");
  process.exit(1);
}
