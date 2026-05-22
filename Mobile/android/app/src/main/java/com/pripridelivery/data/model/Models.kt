package com.pripridelivery.data.model

import com.google.firebase.Timestamp

data class Usuario(
    val uid: String = "",
    val nome: String = "",
    val email: String = "",
    val cpf: String = "",
    val telefone: String = "",
    val createdAt: Timestamp? = null
) {
    // Construtor vazio necessário para o Firestore
    constructor() : this(uid = "")
}

data class Endereco(
    val id: String = "",
    val cep: String = "",
    val logradouro: String = "",
    val numero: String = "",
    val complemento: String = "",
    val bairro: String = "",
    val cidade: String = "",
    val estado: String = "",
    val principal: Boolean = false,
    val userId: String = "",
    val enderecoMapa: String = "",
    val latitude: Double? = null,
    val longitude: Double? = null,
    val createdAt: Timestamp? = null
) {
    constructor() : this(id = "")
}

data class Restaurante(
    val id: String = "",
    val nome: String = "",
    val descricao: String = "",
    val categoria: String = "",
    val horarioAbertura: String = "",
    val horarioFechamento: String = "",
    val imagemUrl: String = "",
    val userId: String = "",
    val categoriasProdutos: List<String> = emptyList(),
    val taxaEntregaNormal: Double = 5.0,
    val taxaEntregaRapida: Double = 8.0,
    val tempoEntregaNormal: Int = 45,
    val tempoEntregaRapida: Int = 25,
    val createdAt: Timestamp? = null
) {
    constructor() : this(id = "")
}

data class Produto(
    val id: String = "",
    val nome: String = "",
    val descricao: String = "",
    val preco: Double = 0.0,
    val categoria: String = "",
    val imagemUrl: String = "",
    val disponivel: Boolean = true,
    val restauranteId: String = "",
    val createdAt: Timestamp? = null
) {
    constructor() : this(id = "")
}

data class ItemCarrinho(
    val id: String = "",
    val userId: String = "",
    val produtoId: String = "",
    val quantidade: Int = 1,
    val createdAt: Timestamp? = null,
    // Campos preenchidos após join
    val produto: Produto? = null,
    val restaurante: Restaurante? = null
) {
    constructor() : this(id = "")
}

data class ItemPedido(
    val nome: String = "",
    val quantidade: Int = 1,
    val preco: Double = 0.0
)

data class Pedido(
    val id: String = "",
    val userId: String = "",
    val pedidoId: String = "",
    val codigoVerificacao: String = "",
    val total: Double = 0.0,
    val itens: List<ItemPedido> = emptyList(),
    val status: String = "recebido",
    val createdAt: String = ""
) {
    constructor() : this(id = "")
}
