package com.pripridelivery.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pripridelivery.data.model.Restaurante
import com.pripridelivery.data.repository.RestauranteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeUiState(
    val restaurantes: List<Restaurante> = emptyList(),
    val restaurantesFiltrados: List<Restaurante> = emptyList(),
    val carregando: Boolean = true,
    val busca: String = "",
    val categoriaFiltro: String = "",
    val erro: String? = null
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val restauranteRepository: RestauranteRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        carregarRestaurantes()
    }

    fun carregarRestaurantes() {
        viewModelScope.launch {
            _uiState.update { it.copy(carregando = true, erro = null) }
            try {
                val lista = restauranteRepository.carregarTodos()
                _uiState.update {
                    it.copy(
                        restaurantes = lista,
                        restaurantesFiltrados = lista,
                        carregando = false
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        erro = "Não foi possível carregar os restaurantes",
                        carregando = false
                    )
                }
            }
        }
    }

    fun atualizarBusca(texto: String) {
        _uiState.update { state ->
            val filtrados = state.restaurantes.filter { rest ->
                rest.nome.contains(texto, ignoreCase = true) ||
                        rest.categoria.contains(texto, ignoreCase = true)
            }
            state.copy(busca = texto, restaurantesFiltrados = filtrados)
        }
    }

    fun filtrarPorCategoria(categoria: String) {
        _uiState.update { state ->
            val filtrados = if (categoria.isBlank()) {
                state.restaurantes
            } else {
                state.restaurantes.filter { it.categoria.equals(categoria, ignoreCase = true) }
            }
            state.copy(categoriaFiltro = categoria, restaurantesFiltrados = filtrados)
        }
    }

    val categorias: List<String>
        get() = _uiState.value.restaurantes
            .map { it.categoria }
            .filter { it.isNotBlank() }
            .distinct()
}
