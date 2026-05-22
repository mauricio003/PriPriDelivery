package com.pripridelivery.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.pripridelivery.data.model.Usuario
import com.pripridelivery.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AuthUiState(
    val usuario: FirebaseUser? = null,
    val dadosUsuario: Usuario? = null,
    val estaAutenticado: Boolean = false,
    val carregando: Boolean = true,
    val erro: String? = null,
    // OTP
    val etapaOtp: Int = 0,         // 0 = email/telefone, 1 = código
    val timerOtp: Int = 0,
    val codigoEnviado: Boolean = false
)

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            authRepository.authStateFlow().collect { user ->
                _uiState.update {
                    it.copy(
                        usuario = user,
                        estaAutenticado = user != null,
                        carregando = false
                    )
                }
                if (user != null) {
                    carregarDadosUsuario(user.uid)
                }
            }
        }
    }

    private fun carregarDadosUsuario(uid: String) {
        viewModelScope.launch {
            try {
                val dados = authRepository.carregarUsuario(uid)
                _uiState.update { it.copy(dadosUsuario = dados) }
            } catch (e: Exception) {
                // Usuário pode não ter dados salvos ainda
            }
        }
    }

    fun loginAnonimo() {
        viewModelScope.launch {
            _uiState.update { it.copy(carregando = true, erro = null) }
            try {
                authRepository.signInAnonymously()
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(erro = authRepository.traduzirErro(e), carregando = false)
                }
            }
        }
    }

    fun loginComGoogle(idToken: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(carregando = true, erro = null) }
            try {
                val user = authRepository.signInWithGoogle(idToken)
                if (user != null) {
                    authRepository.salvarUsuario(
                        Usuario(
                            uid = user.uid,
                            nome = user.displayName ?: "",
                            email = user.email ?: ""
                        )
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(erro = authRepository.traduzirErro(e), carregando = false)
                }
            }
        }
    }

    fun loginComFacebook(token: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(carregando = true, erro = null) }
            try {
                val user = authRepository.signInWithFacebook(token)
                if (user != null) {
                    authRepository.salvarUsuario(
                        Usuario(
                            uid = user.uid,
                            nome = user.displayName ?: "",
                            email = user.email ?: ""
                        )
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(erro = authRepository.traduzirErro(e), carregando = false)
                }
            }
        }
    }

    fun salvarDadosUsuario(usuario: Usuario) {
        viewModelScope.launch {
            try {
                authRepository.salvarUsuario(usuario)
                _uiState.update { it.copy(dadosUsuario = usuario) }
            } catch (e: Exception) {
                _uiState.update { it.copy(erro = e.message) }
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
            _uiState.update {
                AuthUiState(carregando = false)
            }
        }
    }

    fun limparErro() {
        _uiState.update { it.copy(erro = null) }
    }
}
