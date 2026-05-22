package com.pripridelivery.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pripridelivery.data.model.*
import com.pripridelivery.data.repository.CarrinhoRepository
import com.pripridelivery.data.repository.EnderecoRepository
import com.pripridelivery.data.repository.PedidoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

data class PagamentoUiState(
    val itens: List<ItemCarrinho> = emptyList(),
    val total: Double = 0.0,
    val enderecos: List<Endereco> = emptyList(),
    val enderecoSelecionado: Endereco? = null,
    val formaPagamento: String = "",
    val tipoEntrega: String = "entrega",
    val carregando: Boolean = false,
    val erro: String? = null,
    val sucesso: Boolean = false,
    val pedidoId: String? = null,
    val codigoVerificacao: String? = null,
    val mostrarNovoEndereco: Boolean = false,
    val dadosCartao: DadosCartao = DadosCartao()
)

data class DadosCartao(
    val numero: String = "",
    val nome: String = "",
    val validade: String = "",
    val cvv: String = ""
)

@HiltViewModel
class PagamentoViewModel @Inject constructor(
    private val enderecoRepository: EnderecoRepository,
    private val carrinhoRepository: CarrinhoRepository,
    private val pedidoRepository: PedidoRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(PagamentoUiState())
    val uiState: StateFlow<PagamentoUiState> = _uiState.asStateFlow()

    fun inicializar(itens: List<ItemCarrinho>, total: Double, userId: String) {
        _uiState.update { it.copy(itens = itens, total = total) }
        carregarEnderecos(userId)
    }

    private fun carregarEnderecos(userId: String) {
        viewModelScope.launch {
            try {
                val lista = enderecoRepository.carregarEnderecos(userId)
                val principal = lista.find { it.principal } ?: lista.firstOrNull()
                _uiState.update { it.copy(enderecos = lista, enderecoSelecionado = principal) }
            } catch (e: Exception) {
                _uiState.update { it.copy(erro = "Erro ao carregar endereços") }
            }
        }
    }

    fun selecionarEndereco(endereco: Endereco) {
        _uiState.update { it.copy(enderecoSelecionado = endereco) }
    }

    fun selecionarFormaPagamento(forma: String) {
        _uiState.update { it.copy(formaPagamento = forma) }
    }

    fun selecionarTipoEntrega(tipo: String) {
        _uiState.update { it.copy(tipoEntrega = tipo) }
    }

    fun atualizarCartao(dados: DadosCartao) {
        _uiState.update { it.copy(dadosCartao = dados) }
    }

    fun processarPagamento(userId: String) {
        val state = _uiState.value
        if (state.enderecoSelecionado == null) {
            _uiState.update { it.copy(erro = "Selecione um endereço de entrega") }; return
        }
        if (state.formaPagamento.isBlank()) {
            _uiState.update { it.copy(erro = "Selecione uma forma de pagamento") }; return
        }
        if (state.formaPagamento == "cartao") {
            val c = state.dadosCartao
            if (c.numero.isBlank() || c.nome.isBlank() || c.validade.isBlank() || c.cvv.isBlank()) {
                _uiState.update { it.copy(erro = "Preencha todos os dados do cartão") }; return
            }
        }

        viewModelScope.launch {
            _uiState.update { it.copy(carregando = true, erro = null) }
            try {
                val pedidoId = generateId()
                val codigoVerificacao = (100000..999999).random().toString()

                val pedido = Pedido(
                    userId = userId,
                    pedidoId = pedidoId,
                    codigoVerificacao = codigoVerificacao,
                    total = state.total,
                    itens = state.itens.map { ItemPedido(it.produto?.nome ?: "", it.quantidade, it.produto?.preco ?: 0.0) },
                    status = "O restaurante aceitou o pedido",
                    createdAt = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                )
                pedidoRepository.criarPedido(pedido)
                carrinhoRepository.limparCarrinho(userId)

                _uiState.update {
                    it.copy(carregando = false, sucesso = true, pedidoId = pedidoId, codigoVerificacao = codigoVerificacao)
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(erro = "Erro ao processar pagamento", carregando = false) }
            }
        }
    }

    private fun generateId(): String {
        val chars = "abcdefghijklmnopqrstuvwxyz0123456789"
        return (1..9).map { chars.random() }.joinToString("")
    }
}
