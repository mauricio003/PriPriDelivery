package com.pripridelivery.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.pripridelivery.data.model.Restaurante
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class RestauranteRepository @Inject constructor(
    private val db: FirebaseFirestore
) {
    private val collection = db.collection("restaurantes")

    suspend fun carregarTodos(): List<Restaurante> {
        val snapshot = collection.get().await()
        return snapshot.documents.map { doc ->
            docToRestaurante(doc)
        }
    }

    suspend fun carregarPorUsuario(userId: String): List<Restaurante> {
        val snapshot = collection
            .whereEqualTo("userId", userId)
            .get().await()
        return snapshot.documents.map { doc ->
            docToRestaurante(doc)
        }.sortedByDescending { it.createdAt?.toDate() }
    }

    suspend fun carregarPorId(id: String): Restaurante? {
        val doc = collection.document(id).get().await()
        return if (doc.exists()) docToRestaurante(doc) else null
    }

    suspend fun salvar(userId: String, restaurante: Restaurante) {
        val dados = hashMapOf(
            "nome" to restaurante.nome,
            "descricao" to restaurante.descricao,
            "categoria" to restaurante.categoria,
            "horario_abertura" to restaurante.horarioAbertura,
            "horario_fechamento" to restaurante.horarioFechamento,
            "imagem_url" to restaurante.imagemUrl,
            "user_id" to userId,
            "taxa_entrega_normal" to restaurante.taxaEntregaNormal,
            "taxa_entrega_rapida" to restaurante.taxaEntregaRapida,
            "tempo_entrega_normal" to restaurante.tempoEntregaNormal,
            "tempo_entrega_rapida" to restaurante.tempoEntregaRapida
        )

        if (restaurante.id.isNotBlank()) {
            collection.document(restaurante.id)
                .update(dados as Map<String, Any>)
                .await()
        } else {
            dados["createdAt"] = System.currentTimeMillis()
            collection.add(dados).await()
        }
    }

    suspend fun excluir(id: String) {
        collection.document(id).delete().await()
    }

    suspend fun adicionarCategoria(restauranteId: String, categoria: String) {
        collection.document(restauranteId)
            .update("categorias_produtos", com.google.firebase.firestore.FieldValue.arrayUnion(categoria))
            .await()
    }

    @Suppress("UNCHECKED_CAST")
    private fun docToRestaurante(doc: com.google.firebase.firestore.DocumentSnapshot): Restaurante {
        return Restaurante(
            id = doc.id,
            nome = doc.getString("nome") ?: "",
            descricao = doc.getString("descricao") ?: "",
            categoria = doc.getString("categoria") ?: "",
            horarioAbertura = doc.getString("horario_abertura") ?: "",
            horarioFechamento = doc.getString("horario_fechamento") ?: "",
            imagemUrl = doc.getString("imagem_url") ?: "",
            userId = doc.getString("user_id") ?: "",
            taxaEntregaNormal = doc.getDouble("taxa_entrega_normal") ?: 5.0,
            taxaEntregaRapida = doc.getDouble("taxa_entrega_rapida") ?: 8.0,
            tempoEntregaNormal = (doc.getLong("tempo_entrega_normal") ?: 45).toInt(),
            tempoEntregaRapida = (doc.getLong("tempo_entrega_rapida") ?: 25).toInt(),
            createdAt = doc.getTimestamp("created_at")
        )
    }
}
