package com.pripridelivery.data.repository

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.pripridelivery.data.model.Produto
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ProdutoRepository @Inject constructor(
    private val db: FirebaseFirestore
) {
    private val collection = db.collection("produtos")

    suspend fun carregarPorRestaurante(restauranteId: String): List<Produto> {
        val snapshot = collection
            .whereEqualTo("restaurante_id", restauranteId)
            .get().await()
        return snapshot.documents
            .map { docToProduto(it) }
            .sortedByDescending { it.createdAt?.toDate() }
    }

    suspend fun carregarDisponiveis(restauranteId: String): List<Produto> {
        val snapshot = collection
            .whereEqualTo("restaurante_id", restauranteId)
            .whereEqualTo("disponivel", true)
            .get().await()
        return snapshot.documents
            .map { docToProduto(it) }
            .sortedByDescending { it.createdAt?.toDate() }
    }

    suspend fun carregarPorId(id: String): Produto? {
        val doc = collection.document(id).get().await()
        return if (doc.exists()) docToProduto(doc) else null
    }

    suspend fun salvar(restauranteId: String, produto: Produto) {
        val dados = hashMapOf(
            "nome" to produto.nome,
            "descricao" to produto.descricao,
            "preco" to produto.preco,
            "categoria" to produto.categoria,
            "imagem_url" to produto.imagemUrl,
            "disponivel" to produto.disponivel,
            "restaurante_id" to restauranteId
        )

        if (produto.id.isNotBlank()) {
            collection.document(produto.id).update(dados as Map<String, Any>).await()
        } else {
            dados["created_at"] = System.currentTimeMillis()
            collection.add(dados).await()
        }
    }

    suspend fun excluir(id: String) {
        collection.document(id).delete().await()
    }

    private fun docToProduto(doc: DocumentSnapshot): Produto {
        return Produto(
            id = doc.id,
            nome = doc.getString("nome") ?: "",
            descricao = doc.getString("descricao") ?: "",
            preco = doc.getDouble("preco") ?: 0.0,
            categoria = doc.getString("categoria") ?: "",
            imagemUrl = doc.getString("imagem_url") ?: "",
            disponivel = doc.getBoolean("disponivel") ?: true,
            restauranteId = doc.getString("restaurante_id") ?: "",
            createdAt = doc.getTimestamp("created_at")
        )
    }
}
