package com.pripridelivery.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pripridelivery.data.model.Restaurante
import com.pripridelivery.data.repository.RestauranteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class RestauranteUiState(
    val restaurantes: List<Restaurante> = emptyList(),
    val restauranteAtual: Restaurante? = null,
    val carregando: Boolean = true,
    val salvando: Boolean = false,
    val sucesso: Boolean = false,
    val erro: String? = null,
    val mostrarFormulario: Boolean = false
)

@HiltViewModel
class RestauranteViewModel @Inject constructor(
    private val restauranteRepository: RestauranteRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(RestauranteUiState())
    val uiState: StateFlow<RestauranteUiState> = _uiState.asStateFlow()

    fun carregarPorUsuario(userId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(carregando = true, erro = null) }
            try {
                val lista = restauranteRepository.carregarPorUsuario(userId)
                _uiState.update { it.copy(restaurantes = lista, carregando = false) }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(erro = "Erro ao carregar restaurantes", carregando = false)
                }
            }
        }
    }

    fun carregarPorId(id: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(carregando = true) }
            try {
                val restaurante = restauranteRepository.carregarPorId(id)
                _uiState.update { it.copy(restauranteAtual = restaurante, carregando = false) }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(erro = "Restaurante não encontrado", carregando = false)
                }
            }
        }
    }

    fun salvar(userId: String, restaurante: Restaurante) {
        viewModelScope.launch {
            _uiState.update { it.copy(salvando = true, erro = null) }
            try {
                restauranteRepository.salvar(userId, restaurante)
                _uiState.update {
                    it.copy(salvando = false, sucesso = true, mostrarFormulario = false)
                }
                carregarPorUsuario(userId)
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(erro = "Erro ao salvar restaurante", salvando = false)
                }
            }
        }
    }

    fun excluir(userId: String, id: String) {
        viewModelScope.launch {
            try {
                restauranteRepository.excluir(id)
                carregarPorUsuario(userId)
            } catch (e: Exception) {
                _uiState.update { it.copy(erro = "Erro ao excluir restaurante") }
            }
        }
    }

    fun adicionarCategoria(restauranteId: String, categoria: String) {
        viewModelScope.launch {
            try {
                restauranteRepository.adicionarCategoria(restauranteId, categoria)
            } catch (e: Exception) {
                _uiState.update { it.copy(erro = "Erro ao adicionar categoria") }
            }
        }
    }

    fun abrirFormulario(restaurante: Restaurante? = null) {
        _uiState.update {
            it.copy(restauranteAtual = restaurante, mostrarFormulario = true)
        }
    }

    fun fecharFormulario() {
        _uiState.update { it.copy(restauranteAtual = null, mostrarFormulario = false) }
    }

    fun limparSucesso() {
        _uiState.update { it.copy(sucesso = false) }
    }
}
