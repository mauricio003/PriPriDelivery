package com.pripridelivery.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.pripridelivery.data.model.Endereco
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class EnderecoRepository @Inject constructor(
    private val db: FirebaseFirestore
) {
    private val collection = db.collection("enderecos")

    suspend fun carregarEnderecos(userId: String): List<Endereco> {
        val snapshot = collection
            .whereEqualTo("user_id", userId)
            .get().await()

        return snapshot.documents.map { doc ->
            Endereco(
                id = doc.id,
                cep = doc.getString("cep") ?: "",
                logradouro = doc.getString("logradouro") ?: "",
                numero = doc.getString("numero") ?: "",
                complemento = doc.getString("complemento") ?: "",
                bairro = doc.getString("bairro") ?: "",
                cidade = doc.getString("cidade") ?: "",
                estado = doc.getString("estado") ?: "",
                principal = doc.getBoolean("principal") ?: false,
                userId = doc.getString("user_id") ?: "",
                enderecoMapa = doc.getString("endereco_mapa") ?: "",
                latitude = doc.getDouble("latitude"),
                longitude = doc.getDouble("longitude")
            )
        }
    }

    suspend fun salvarEndereco(userId: String, endereco: Endereco) {
        // Se for principal, desmarcar os outros
        if (endereco.principal) {
            val existentes = collection
                .whereEqualTo("user_id", userId)
                .get().await()

            for (doc in existentes.documents) {
                collection.document(doc.id).update("principal", false).await()
            }
        }

        val dados = hashMapOf(
            "cep" to endereco.cep,
            "logradouro" to endereco.logradouro,
            "numero" to endereco.numero,
            "complemento" to endereco.complemento,
            "bairro" to endereco.bairro,
            "cidade" to endereco.cidade,
            "estado" to endereco.estado,
            "principal" to endereco.principal,
            "user_id" to userId,
            "endereco_mapa" to endereco.enderecoMapa,
            "latitude" to endereco.latitude,
            "longitude" to endereco.longitude
        )

        if (endereco.id.isNotBlank()) {
            collection.document(endereco.id).update(dados as Map<String, Any>).await()
        } else {
            dados["created_at"] = System.currentTimeMillis()
            collection.add(dados).await()
        }
    }

    suspend fun excluirEndereco(id: String) {
        collection.document(id).delete().await()
    }
}
