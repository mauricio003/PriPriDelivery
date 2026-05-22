package com.pripridelivery.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pripridelivery.data.model.Produto
import com.pripridelivery.data.repository.ProdutoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProdutoUiState(
    val produtos: List<Produto> = emptyList(),
    val produtosFiltrados: List<Produto> = emptyList(),
    val carregando: Boolean = true,
    val salvando: Boolean = false,
    val sucesso: Boolean = false,
    val erro: String? = null,
    val busca: String = "",
    val mostrarFormulario: Boolean = false,
    val produtoEditando: Produto? = null,
    // Quantidades para adição ao carrinho (tela de compra)
    val quantidades: Map<String, Int> = emptyMap()
)

@HiltViewModel
class ProdutoViewModel @Inject constructor(
    private val produtoRepository: ProdutoRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProdutoUiState())
    val uiState: StateFlow<ProdutoUiState> = _uiState.asStateFlow()

    fun carregarProdutos(restauranteId: String, apenasDisponiveis: Boolean = false) {
        viewModelScope.launch {
            _uiState.update { it.copy(carregando = true, erro = null) }
            try {
                val lista = if (apenasDisponiveis) {
                    produtoRepository.carregarDisponiveis(restauranteId)
                } else {
                    produtoRepository.carregarPorRestaurante(restauranteId)
                }

                val quantidades = lista.associate { it.id to 1 }

                _uiState.update {
                    it.copy(
                        produtos = lista,
                        produtosFiltrados = lista,
                        quantidades = quantidades,
                        carregando = false
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(erro = "Erro ao carregar produtos", carregando = false)
                }
            }
        }
    }

    fun atualizarBusca(texto: String) {
        _uiState.update { state ->
            val filtrados = state.produtos.filter { produto ->
                produto.nome.contains(texto, ignoreCase = true) ||
                        produto.categoria.contains(texto, ignoreCase = true)
            }
            state.copy(busca = texto, produtosFiltrados = filtrados)
        }
    }

    fun ajustarQuantidade(produtoId: String, delta: Int) {
        _uiState.update { state ->
            val qtdAtual = state.quantidades[produtoId] ?: 1
            val novaQtd = maxOf(1, qtdAtual + delta)
            state.copy(quantidades = state.quantidades + (produtoId to novaQtd))
        }
    }

    fun resetarQuantidade(produtoId: String) {
        _uiState.update { state ->
            state.copy(quantidades = state.quantidades + (produtoId to 1))
        }
    }

    fun getQuantidade(produtoId: String): Int {
        return _uiState.value.quantidades[produtoId] ?: 1
    }

    fun salvar(restauranteId: String, produto: Produto) {
        viewModelScope.launch {
            _uiState.update { it.copy(salvando = true, erro = null) }
            try {
                produtoRepository.salvar(restauranteId, produto)
                _uiState.update {
                    it.copy(salvando = false, sucesso = true, mostrarFormulario = false)
                }
                carregarProdutos(restauranteId)
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(erro = "Erro ao salvar produto", salvando = false)
                }
            }
        }
    }

    fun excluir(restauranteId: String, produtoId: String) {
        viewModelScope.launch {
            try {
                produtoRepository.excluir(produtoId)
                carregarProdutos(restauranteId)
            } catch (e: Exception) {
                _uiState.update { it.copy(erro = "Erro ao excluir produto") }
            }
        }
    }

    fun abrirFormulario(produto: Produto? = null) {
        _uiState.update { it.copy(produtoEditando = produto, mostrarFormulario = true) }
    }

    fun fecharFormulario() {
        _uiState.update { it.copy(produtoEditando = null, mostrarFormulario = false) }
    }

    val categorias: List<String>
        get() = _uiState.value.produtosFiltrados
            .map { it.categoria }
            .filter { it.isNotBlank() }
            .distinct()
}
