package com.pripridelivery.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.pripridelivery.data.model.ItemPedido
import com.pripridelivery.data.model.Pedido
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class PedidoRepository @Inject constructor(
    private val db: FirebaseFirestore
) {
    private val collection = db.collection("pedidos")

    suspend fun criarPedido(pedido: Pedido): String {
        val dados = hashMapOf(
            "user_id" to pedido.userId,
            "pedido_id" to pedido.pedidoId,
            "codigo_verificacao" to pedido.codigoVerificacao,
            "total" to pedido.total,
            "itens" to pedido.itens.map { item ->
                hashMapOf(
                    "nome" to item.nome,
                    "quantidade" to item.quantidade,
                    "preco" to item.preco
                )
            },
            "status" to pedido.status,
            "created_at" to pedido.createdAt
        )

        val docRef = collection.add(dados).await()
        return docRef.id
    }

    @Suppress("UNCHECKED_CAST")
    suspend fun carregarPedidos(userId: String): List<Pedido> {
        val snapshot = collection
            .whereEqualTo("user_id", userId)
            .get().await()

        return snapshot.documents.map { doc ->
            val itensRaw = doc.get("itens") as? List<Map<String, Any>> ?: emptyList()
            val itens = itensRaw.map { item ->
                ItemPedido(
                    nome = item["nome"] as? String ?: "",
                    quantidade = (item["quantidade"] as? Long ?: 1).toInt(),
                    preco = (item["preco"] as? Double) ?: 0.0
                )
            }

            Pedido(
                id = doc.id,
                userId = doc.getString("user_id") ?: "",
                pedidoId = doc.getString("pedido_id") ?: "",
                codigoVerificacao = doc.getString("codigo_verificacao") ?: "",
                total = doc.getDouble("total") ?: 0.0,
                itens = itens,
                status = doc.getString("status") ?: "recebido",
                createdAt = doc.getString("created_at") ?: ""
            )
        }.sortedByDescending { it.createdAt }
    }

    suspend fun atualizarStatus(pedidoId: String, status: String) {
        val snapshot = collection
            .whereEqualTo("pedido_id", pedidoId)
            .get().await()

        for (doc in snapshot.documents) {
            collection.document(doc.id).update("status", status).await()
        }
    }
}
