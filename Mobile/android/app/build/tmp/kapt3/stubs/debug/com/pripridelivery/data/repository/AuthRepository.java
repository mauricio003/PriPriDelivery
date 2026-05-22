package com.pripridelivery.data.repository;

import com.google.firebase.auth.*;
import com.google.firebase.firestore.FirebaseFirestore;
import com.pripridelivery.data.model.Usuario;
import kotlinx.coroutines.flow.Flow;
import javax.inject.Inject;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000F\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0006\n\u0002\u0010\u0002\n\u0002\b\u000b\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\u0018\u00002\u00020\u0001B\u0017\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J\u000e\u0010\u000b\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\b0\fJ\u0018\u0010\r\u001a\u0004\u0018\u00010\u000e2\u0006\u0010\u000f\u001a\u00020\u0010H\u0086@\u00a2\u0006\u0002\u0010\u0011J \u0010\u0012\u001a\u0004\u0018\u00010\b2\u0006\u0010\u0013\u001a\u00020\u00102\u0006\u0010\u0014\u001a\u00020\u0010H\u0086@\u00a2\u0006\u0002\u0010\u0015J\u000e\u0010\u0016\u001a\u00020\u0017H\u0086@\u00a2\u0006\u0002\u0010\u0018J\u0016\u0010\u0019\u001a\u00020\u00172\u0006\u0010\u001a\u001a\u00020\u000eH\u0086@\u00a2\u0006\u0002\u0010\u001bJ\u0010\u0010\u001c\u001a\u0004\u0018\u00010\bH\u0086@\u00a2\u0006\u0002\u0010\u0018J\u0018\u0010\u001d\u001a\u0004\u0018\u00010\b2\u0006\u0010\u001e\u001a\u00020\u0010H\u0086@\u00a2\u0006\u0002\u0010\u0011J\u0018\u0010\u001f\u001a\u0004\u0018\u00010\b2\u0006\u0010 \u001a\u00020\u0010H\u0086@\u00a2\u0006\u0002\u0010\u0011J\u0012\u0010!\u001a\u00020\u00102\n\u0010\"\u001a\u00060#j\u0002`$R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0013\u0010\u0007\u001a\u0004\u0018\u00010\b8F\u00a2\u0006\u0006\u001a\u0004\b\t\u0010\nR\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006%"}, d2 = {"Lcom/pripridelivery/data/repository/AuthRepository;", "", "auth", "Lcom/google/firebase/auth/FirebaseAuth;", "db", "Lcom/google/firebase/firestore/FirebaseFirestore;", "(Lcom/google/firebase/auth/FirebaseAuth;Lcom/google/firebase/firestore/FirebaseFirestore;)V", "currentUser", "Lcom/google/firebase/auth/FirebaseUser;", "getCurrentUser", "()Lcom/google/firebase/auth/FirebaseUser;", "authStateFlow", "Lkotlinx/coroutines/flow/Flow;", "carregarUsuario", "Lcom/pripridelivery/data/model/Usuario;", "uid", "", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "createUserWithEmailAndPassword", "email", "senha", "(Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "logout", "", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "salvarUsuario", "usuario", "(Lcom/pripridelivery/data/model/Usuario;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "signInAnonymously", "signInWithFacebook", "token", "signInWithGoogle", "idToken", "traduzirErro", "e", "Ljava/lang/Exception;", "Lkotlin/Exception;", "app_debug"})
public final class AuthRepository {
    @org.jetbrains.annotations.NotNull()
    private final com.google.firebase.auth.FirebaseAuth auth = null;
    @org.jetbrains.annotations.NotNull()
    private final com.google.firebase.firestore.FirebaseFirestore db = null;
    
    @javax.inject.Inject()
    public AuthRepository(@org.jetbrains.annotations.NotNull()
    com.google.firebase.auth.FirebaseAuth auth, @org.jetbrains.annotations.NotNull()
    com.google.firebase.firestore.FirebaseFirestore db) {
        super();
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.google.firebase.auth.FirebaseUser getCurrentUser() {
        return null;
    }
    
    /**
     * Observa mudanças no estado de autenticação.
     */
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<com.google.firebase.auth.FirebaseUser> authStateFlow() {
        return null;
    }
    
    /**
     * Login anônimo (usado após verificação OTP por email).
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object signInAnonymously(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.google.firebase.auth.FirebaseUser> $completion) {
        return null;
    }
    
    /**
     * Login com Google.
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object signInWithGoogle(@org.jetbrains.annotations.NotNull()
    java.lang.String idToken, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.google.firebase.auth.FirebaseUser> $completion) {
        return null;
    }
    
    /**
     * Login com Facebook.
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object signInWithFacebook(@org.jetbrains.annotations.NotNull()
    java.lang.String token, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.google.firebase.auth.FirebaseUser> $completion) {
        return null;
    }
    
    /**
     * Cadastro com email e senha.
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object createUserWithEmailAndPassword(@org.jetbrains.annotations.NotNull()
    java.lang.String email, @org.jetbrains.annotations.NotNull()
    java.lang.String senha, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.google.firebase.auth.FirebaseUser> $completion) {
        return null;
    }
    
    /**
     * Salva dados do usuário no Firestore.
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object salvarUsuario(@org.jetbrains.annotations.NotNull()
    com.pripridelivery.data.model.Usuario usuario, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    /**
     * Carrega dados do usuário do Firestore.
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object carregarUsuario(@org.jetbrains.annotations.NotNull()
    java.lang.String uid, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.pripridelivery.data.model.Usuario> $completion) {
        return null;
    }
    
    /**
     * Logout.
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object logout(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    /**
     * Traduz erros do Firebase Auth para mensagens em português.
     */
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String traduzirErro(@org.jetbrains.annotations.NotNull()
    java.lang.Exception e) {
        return null;
    }
}