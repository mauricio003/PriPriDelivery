package com.pripridelivery.data.repository

import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.pripridelivery.data.model.ItemCarrinho
import com.pripridelivery.data.model.Produto
import com.pripridelivery.data.model.Restaurante
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class CarrinhoRepository @Inject constructor(
    private val db: FirebaseFirestore
) {
    private val collection = db.collection("carrinho")

    /**
     * Carrega todos os itens do carrinho do usuário, com dados do produto e restaurante.
     */
    suspend fun carregarCarrinho(userId: String): List<ItemCarrinho> {
        val snapshot = collection
            .whereEqualTo("user_id", userId)
            .get().await()

        val itensBase = snapshot.documents.map { doc ->
            ItemCarrinho(
                id = doc.id,
                userId = doc.getString("user_id") ?: "",
                produtoId = doc.getString("produto_id") ?: "",
                quantidade = (doc.getLong("quantidade") ?: 1).toInt()
            )
        }

        return itensBase.mapNotNull { item ->
            try {
                val produtoDoc = db.collection("produtos").document(item.produtoId).get().await()
                if (!produtoDoc.exists()) return@mapNotNull null

                val produto = Produto(
                    id = produtoDoc.id,
                    nome = produtoDoc.getString("nome") ?: "",
                    descricao = produtoDoc.getString("descricao") ?: "",
                    preco = produtoDoc.getDouble("preco") ?: 0.0,
                    categoria = produtoDoc.getString("categoria") ?: "",
                    imagemUrl = produtoDoc.getString("imagem_url") ?: "",
                    disponivel = produtoDoc.getBoolean("disponivel") ?: true,
                    restauranteId = produtoDoc.getString("restaurante_id") ?: ""
                )

                var restaurante: Restaurante? = null
                if (produto.restauranteId.isNotBlank()) {
                    val restDoc = db.collection("restaurantes").document(produto.restauranteId).get().await()
                    if (restDoc.exists()) {
                        restaurante = Restaurante(
                            id = restDoc.id,
                            nome = restDoc.getString("nome") ?: "",
                            categoria = restDoc.getString("categoria") ?: "",
                            taxaEntregaNormal = restDoc.getDouble("taxa_entrega_normal") ?: 5.0,
                            taxaEntregaRapida = restDoc.getDouble("taxa_entrega_rapida") ?: 8.0,
                            tempoEntregaNormal = (restDoc.getLong("tempo_entrega_normal") ?: 45).toInt(),
                            tempoEntregaRapida = (restDoc.getLong("tempo_entrega_rapida") ?: 25).toInt()
                        )
                    }
                }

                item.copy(produto = produto, restaurante = restaurante)
            } catch (e: Exception) {
                null
            }
        }
    }

    /**
     * Adiciona produto ao carrinho ou incrementa quantidade se já existir.
     */
    suspend fun adicionarAoCarrinho(userId: String, produtoId: String, quantidade: Int = 1) {
        val snapshot = collection
            .whereEqualTo("user_id", userId)
            .whereEqualTo("produto_id", produtoId)
            .get().await()

        if (snapshot.documents.isNotEmpty()) {
            val doc = snapshot.documents[0]
            val qtdAtual = (doc.getLong("quantidade") ?: 1).toInt()
            collection.document(doc.id).update("quantidade", qtdAtual + quantidade).await()
        } else {
            collection.add(
                hashMapOf(
                    "user_id" to userId,
                    "produto_id" to produtoId,
                    "quantidade" to quantidade,
                    "created_at" to Timestamp.now()
                )
            ).await()
        }
    }

    suspend fun aumentarQuantidade(itemId: String, quantidadeAtual: Int) {
        collection.document(itemId).update("quantidade", quantidadeAtual + 1).await()
    }

    suspend fun diminuirQuantidade(itemId: String, quantidadeAtual: Int) {
        if (quantidadeAtual <= 1) {
            collection.document(itemId).delete().await()
        } else {
            collection.document(itemId).update("quantidade", quantidadeAtual - 1).await()
        }
    }

    suspend fun removerItem(itemId: String) {
        collection.document(itemId).delete().await()
    }

    suspend fun limparCarrinho(userId: String) {
        val snapshot = collection
            .whereEqualTo("user_id", userId)
            .get().await()

        for (doc in snapshot.documents) {
            collection.document(doc.id).delete().await()
        }
    }

    fun calcularTotal(itens: List<ItemCarrinho>): Double {
        return itens.sumOf { (it.produto?.preco ?: 0.0) * it.quantidade }
    }
}
