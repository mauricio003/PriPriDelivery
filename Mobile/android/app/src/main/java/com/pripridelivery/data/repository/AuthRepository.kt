package com.pripridelivery.data.repository

import com.google.firebase.auth.*
import com.google.firebase.firestore.FirebaseFirestore
import com.pripridelivery.data.model.Usuario
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val auth: FirebaseAuth,
    private val db: FirebaseFirestore
) {
    val currentUser: FirebaseUser? get() = auth.currentUser

    /**
     * Observa mudanças no estado de autenticação.
     */
    fun authStateFlow(): Flow<FirebaseUser?> = callbackFlow {
        val listener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            trySend(firebaseAuth.currentUser)
        }
        auth.addAuthStateListener(listener)
        awaitClose { auth.removeAuthStateListener(listener) }
    }

    /**
     * Login anônimo (usado após verificação OTP por email).
     */
    suspend fun signInAnonymously(): FirebaseUser? {
        val result = auth.signInAnonymously().await()
        return result.user
    }

    /**
     * Login com Google.
     */
    suspend fun signInWithGoogle(idToken: String): FirebaseUser? {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        val result = auth.signInWithCredential(credential).await()
        return result.user
    }

    /**
     * Login com Facebook.
     */
    suspend fun signInWithFacebook(token: String): FirebaseUser? {
        val credential = FacebookAuthProvider.getCredential(token)
        val result = auth.signInWithCredential(credential).await()
        return result.user
    }

    /**
     * Cadastro com email e senha.
     */
    suspend fun createUserWithEmailAndPassword(email: String, senha: String): FirebaseUser? {
        val result = auth.createUserWithEmailAndPassword(email, senha).await()
        return result.user
    }

    /**
     * Salva dados do usuário no Firestore.
     */
    suspend fun salvarUsuario(usuario: Usuario) {
        db.collection("usuarios")
            .document(usuario.uid)
            .set(usuario)
            .await()
    }

    /**
     * Carrega dados do usuário do Firestore.
     */
    suspend fun carregarUsuario(uid: String): Usuario? {
        val snapshot = db.collection("usuarios").document(uid).get().await()
        return if (snapshot.exists()) {
            snapshot.toObject(Usuario::class.java)?.copy(uid = snapshot.id)
        } else null
    }

    /**
     * Logout.
     */
    suspend fun logout() {
        auth.signOut()
    }

    /**
     * Traduz erros do Firebase Auth para mensagens em português.
     */
    fun traduzirErro(e: Exception): String {
        return when (e) {
            is FirebaseAuthInvalidCredentialsException -> "Credenciais inválidas."
            is FirebaseAuthUserCollisionException -> "Já existe conta com este email."
            is FirebaseAuthWeakPasswordException -> "A senha deve ter pelo menos 6 caracteres."
            is FirebaseAuthInvalidUserException -> "Usuário não encontrado."
            else -> e.message ?: "Ocorreu um erro. Tente novamente."
        }
    }
}
