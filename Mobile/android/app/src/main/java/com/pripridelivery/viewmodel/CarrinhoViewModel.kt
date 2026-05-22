package com.pripridelivery.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pripridelivery.data.model.ItemCarrinho
import com.pripridelivery.data.repository.CarrinhoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CarrinhoUiState(
    val itens: List<ItemCarrinho> = emptyList(),
    val total: Double = 0.0,
    val carregando: Boolean = false,
    val erro: String? = null,
    val carrinhoAberto: Boolean = false
)

@HiltViewModel
class CarrinhoViewModel @Inject constructor(
    private val carrinhoRepository: CarrinhoRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CarrinhoUiState())
    val uiState: StateFlow<CarrinhoUiState> = _uiState.asStateFlow()

    fun carregarCarrinho(userId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(carregando = true) }
            try {
                val itens = carrinhoRepository.carregarCarrinho(userId)
                val total = carrinhoRepository.calcularTotal(itens)
                _uiState.update { it.copy(itens = itens, total = total, carregando = false) }
            } catch (e: Exception) {
                _uiState.update { it.copy(erro = "Erro ao carregar carrinho", carregando = false) }
            }
        }
    }

    fun adicionarAoCarrinho(userId: String, produtoId: String, quantidade: Int, restauranteIdProduto: String) {
        viewModelScope.launch {
            try {
                val itensAtuais = _uiState.value.itens
                if (itensAtuais.isNotEmpty()) {
                    val primeiroRest = itensAtuais[0].restaurante?.id
                    if (primeiroRest != null && primeiroRest != restauranteIdProduto) {
                        _uiState.update { it.copy(erro = "Você já possui itens de outro restaurante no carrinho.") }
                        return@launch
                    }
                }
                carrinhoRepository.adicionarAoCarrinho(userId, produtoId, quantidade)
                carregarCarrinho(userId)
            } catch (e: Exception) {
                _uiState.update { it.copy(erro = "Erro ao adicionar ao carrinho") }
            }
        }
    }

    fun aumentarQuantidade(userId: String, itemId: String, qtdAtual: Int) {
        viewModelScope.launch {
            try { carrinhoRepository.aumentarQuantidade(itemId, qtdAtual); carregarCarrinho(userId) }
            catch (e: Exception) { _uiState.update { it.copy(erro = "Erro ao atualizar quantidade") } }
        }
    }

    fun diminuirQuantidade(userId: String, itemId: String, qtdAtual: Int) {
        viewModelScope.launch {
            try { carrinhoRepository.diminuirQuantidade(itemId, qtdAtual); carregarCarrinho(userId) }
            catch (e: Exception) { _uiState.update { it.copy(erro = "Erro ao atualizar quantidade") } }
        }
    }

    fun removerItem(userId: String, itemId: String) {
        viewModelScope.launch {
            try { carrinhoRepository.removerItem(itemId); carregarCarrinho(userId) }
            catch (e: Exception) { _uiState.update { it.copy(erro = "Erro ao remover item") } }
        }
    }

    fun limparCarrinho(userId: String) {
        viewModelScope.launch {
            try { carrinhoRepository.limparCarrinho(userId); _uiState.update { it.copy(itens = emptyList(), total = 0.0) } }
            catch (e: Exception) { _uiState.update { it.copy(erro = "Erro ao limpar carrinho") } }
        }
    }

    fun abrirCarrinho() { _uiState.update { it.copy(carrinhoAberto = true) } }
    fun fecharCarrinho() { _uiState.update { it.copy(carrinhoAberto = false) } }
    fun limparErro() { _uiState.update { it.copy(erro = null) } }
}
