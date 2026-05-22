package com.pripridelivery.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pripridelivery.data.model.Pedido
import com.pripridelivery.data.repository.PedidoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class PedidoUiState(
    val pedidos: List<Pedido> = emptyList(),
    val carregando: Boolean = true,
    val statusAtual: Int = 0,
    val pedidoConcluido: Boolean = false,
    val mostrarModalVerificacao: Boolean = false,
    val codigoInserido: String = "",
    val erroVerificacao: String = "",
    val erro: String? = null
)

@HiltViewModel
class PedidoViewModel @Inject constructor(
    private val pedidoRepository: PedidoRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(PedidoUiState())
    val uiState: StateFlow<PedidoUiState> = _uiState.asStateFlow()

    val statusPedido = listOf(
        "O restaurante aceitou o pedido",
        "Pedido sendo preparado",
        "Encontrando motorista parceiro",
        "Seu motorista está indo até você",
        "Seu pedido chegou"
    )

    fun carregarPedidos(userId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(carregando = true) }
            try {
                val lista = pedidoRepository.carregarPedidos(userId)
                _uiState.update { it.copy(pedidos = lista, carregando = false) }
            } catch (e: Exception) {
                _uiState.update { it.copy(erro = "Erro ao carregar pedidos", carregando = false) }
            }
        }
    }

    fun iniciarAcompanhamento(pedidoId: String, backendUrl: String = "http://10.0.2.2:3001") {
        viewModelScope.launch {
            while (true) {
                try {
                    val url = "$backendUrl/api/order-status/$pedidoId"
                    val connection = java.net.URL(url).openConnection() as java.net.HttpURLConnection
                    connection.requestMethod = "GET"
                    connection.connectTimeout = 5000

                    if (connection.responseCode == 200) {
                        val response = connection.inputStream.bufferedReader().readText()
                        // Parse simples do JSON
                        val indexMatch = Regex("\"statusIndex\"\\s*:\\s*(\\d+)").find(response)
                        val statusIndex = indexMatch?.groupValues?.get(1)?.toIntOrNull() ?: 0

                        if (statusIndex != _uiState.value.statusAtual) {
                            _uiState.update { it.copy(statusAtual = statusIndex) }
                            if (statusIndex == 4 && !_uiState.value.pedidoConcluido) {
                                _uiState.update { it.copy(mostrarModalVerificacao = true) }
                            }
                        }
                    }
                    connection.disconnect()
                } catch (_: Exception) { }

                delay(3000)
            }
        }
    }

    fun atualizarCodigoInserido(codigo: String) {
        _uiState.update { it.copy(codigoInserido = codigo.filter { c -> c.isDigit() }) }
    }

    fun verificarCodigo(codigoCorreto: String) {
        if (_uiState.value.codigoInserido == codigoCorreto) {
            _uiState.update { it.copy(pedidoConcluido = true, mostrarModalVerificacao = false) }
        } else {
            _uiState.update { it.copy(erroVerificacao = "Código incorreto. Verifique seu e-mail.") }
        }
    }
}
