package com.pripridelivery.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pripridelivery.data.model.Endereco
import com.pripridelivery.data.repository.EnderecoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class EnderecoUiState(
    val enderecos: List<Endereco> = emptyList(),
    val carregando: Boolean = true,
    val erro: String? = null,
    val salvando: Boolean = false,
    val sucesso: Boolean = false,
    val enderecoEditando: Endereco? = null,
    val mostrarFormulario: Boolean = false
)

@HiltViewModel
class EnderecoViewModel @Inject constructor(
    private val enderecoRepository: EnderecoRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(EnderecoUiState())
    val uiState: StateFlow<EnderecoUiState> = _uiState.asStateFlow()

    fun carregarEnderecos(userId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(carregando = true, erro = null) }
            try {
                val lista = enderecoRepository.carregarEnderecos(userId)
                _uiState.update { it.copy(enderecos = lista, carregando = false) }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(erro = "Erro ao carregar endereços", carregando = false)
                }
            }
        }
    }

    fun salvarEndereco(userId: String, endereco: Endereco) {
        viewModelScope.launch {
            _uiState.update { it.copy(salvando = true, erro = null) }
            try {
                enderecoRepository.salvarEndereco(userId, endereco)
                _uiState.update {
                    it.copy(
                        salvando = false,
                        sucesso = true,
                        mostrarFormulario = false,
                        enderecoEditando = null
                    )
                }
                carregarEnderecos(userId)
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(erro = "Erro ao salvar endereço", salvando = false)
                }
            }
        }
    }

    fun excluirEndereco(userId: String, id: String) {
        viewModelScope.launch {
            try {
                enderecoRepository.excluirEndereco(id)
                carregarEnderecos(userId)
            } catch (e: Exception) {
                _uiState.update { it.copy(erro = "Erro ao excluir endereço") }
            }
        }
    }

    fun iniciarEdicao(endereco: Endereco?) {
        _uiState.update {
            it.copy(enderecoEditando = endereco, mostrarFormulario = true)
        }
    }

    fun cancelarFormulario() {
        _uiState.update {
            it.copy(enderecoEditando = null, mostrarFormulario = false)
        }
    }

    fun limparSucesso() {
        _uiState.update { it.copy(sucesso = false) }
    }
}
