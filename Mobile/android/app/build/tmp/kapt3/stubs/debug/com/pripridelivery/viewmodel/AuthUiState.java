package com.pripridelivery.viewmodel;

import androidx.lifecycle.ViewModel;
import com.google.firebase.auth.FirebaseUser;
import com.pripridelivery.data.model.Usuario;
import com.pripridelivery.data.repository.AuthRepository;
import dagger.hilt.android.lifecycle.HiltViewModel;
import kotlinx.coroutines.flow.*;
import javax.inject.Inject;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0002\b\u001e\b\u0086\b\u0018\u00002\u00020\u0001B[\u0012\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0007\u0012\b\b\u0002\u0010\b\u001a\u00020\u0007\u0012\n\b\u0002\u0010\t\u001a\u0004\u0018\u00010\n\u0012\b\b\u0002\u0010\u000b\u001a\u00020\f\u0012\b\b\u0002\u0010\r\u001a\u00020\f\u0012\b\b\u0002\u0010\u000e\u001a\u00020\u0007\u00a2\u0006\u0002\u0010\u000fJ\u000b\u0010\u001d\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\u000b\u0010\u001e\u001a\u0004\u0018\u00010\u0005H\u00c6\u0003J\t\u0010\u001f\u001a\u00020\u0007H\u00c6\u0003J\t\u0010 \u001a\u00020\u0007H\u00c6\u0003J\u000b\u0010!\u001a\u0004\u0018\u00010\nH\u00c6\u0003J\t\u0010\"\u001a\u00020\fH\u00c6\u0003J\t\u0010#\u001a\u00020\fH\u00c6\u0003J\t\u0010$\u001a\u00020\u0007H\u00c6\u0003J_\u0010%\u001a\u00020\u00002\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00072\b\b\u0002\u0010\b\u001a\u00020\u00072\n\b\u0002\u0010\t\u001a\u0004\u0018\u00010\n2\b\b\u0002\u0010\u000b\u001a\u00020\f2\b\b\u0002\u0010\r\u001a\u00020\f2\b\b\u0002\u0010\u000e\u001a\u00020\u0007H\u00c6\u0001J\u0013\u0010&\u001a\u00020\u00072\b\u0010\'\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010(\u001a\u00020\fH\u00d6\u0001J\t\u0010)\u001a\u00020\nH\u00d6\u0001R\u0011\u0010\b\u001a\u00020\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0011R\u0011\u0010\u000e\u001a\u00020\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0011R\u0013\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0014R\u0013\u0010\t\u001a\u0004\u0018\u00010\n\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0016R\u0011\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\u0011R\u0011\u0010\u000b\u001a\u00020\f\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0018\u0010\u0019R\u0011\u0010\r\u001a\u00020\f\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001a\u0010\u0019R\u0013\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001b\u0010\u001c\u00a8\u0006*"}, d2 = {"Lcom/pripridelivery/viewmodel/AuthUiState;", "", "usuario", "Lcom/google/firebase/auth/FirebaseUser;", "dadosUsuario", "Lcom/pripridelivery/data/model/Usuario;", "estaAutenticado", "", "carregando", "erro", "", "etapaOtp", "", "timerOtp", "codigoEnviado", "(Lcom/google/firebase/auth/FirebaseUser;Lcom/pripridelivery/data/model/Usuario;ZZLjava/lang/String;IIZ)V", "getCarregando", "()Z", "getCodigoEnviado", "getDadosUsuario", "()Lcom/pripridelivery/data/model/Usuario;", "getErro", "()Ljava/lang/String;", "getEstaAutenticado", "getEtapaOtp", "()I", "getTimerOtp", "getUsuario", "()Lcom/google/firebase/auth/FirebaseUser;", "component1", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "copy", "equals", "other", "hashCode", "toString", "app_debug"})
public final class AuthUiState {
    @org.jetbrains.annotations.Nullable()
    private final com.google.firebase.auth.FirebaseUser usuario = null;
    @org.jetbrains.annotations.Nullable()
    private final com.pripridelivery.data.model.Usuario dadosUsuario = null;
    private final boolean estaAutenticado = false;
    private final boolean carregando = false;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String erro = null;
    private final int etapaOtp = 0;
    private final int timerOtp = 0;
    private final boolean codigoEnviado = false;
    
    public AuthUiState(@org.jetbrains.annotations.Nullable()
    com.google.firebase.auth.FirebaseUser usuario, @org.jetbrains.annotations.Nullable()
    com.pripridelivery.data.model.Usuario dadosUsuario, boolean estaAutenticado, boolean carregando, @org.jetbrains.annotations.Nullable()
    java.lang.String erro, int etapaOtp, int timerOtp, boolean codigoEnviado) {
        super();
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.google.firebase.auth.FirebaseUser getUsuario() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.pripridelivery.data.model.Usuario getDadosUsuario() {
        return null;
    }
    
    public final boolean getEstaAutenticado() {
        return false;
    }
    
    public final boolean getCarregando() {
        return false;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getErro() {
        return null;
    }
    
    public final int getEtapaOtp() {
        return 0;
    }
    
    public final int getTimerOtp() {
        return 0;
    }
    
    public final boolean getCodigoEnviado() {
        return false;
    }
    
    public AuthUiState() {
        super();
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.google.firebase.auth.FirebaseUser component1() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.pripridelivery.data.model.Usuario component2() {
        return null;
    }
    
    public final boolean component3() {
        return false;
    }
    
    public final boolean component4() {
        return false;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component5() {
        return null;
    }
    
    public final int component6() {
        return 0;
    }
    
    public final int component7() {
        return 0;
    }
    
    public final boolean component8() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.pripridelivery.viewmodel.AuthUiState copy(@org.jetbrains.annotations.Nullable()
    com.google.firebase.auth.FirebaseUser usuario, @org.jetbrains.annotations.Nullable()
    com.pripridelivery.data.model.Usuario dadosUsuario, boolean estaAutenticado, boolean carregando, @org.jetbrains.annotations.Nullable()
    java.lang.String erro, int etapaOtp, int timerOtp, boolean codigoEnviado) {
        return null;
    }
    
    @java.lang.Override()
    public boolean equals(@org.jetbrains.annotations.Nullable()
    java.lang.Object other) {
        return false;
    }
    
    @java.lang.Override()
    public int hashCode() {
        return 0;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public java.lang.String toString() {
        return null;
    }
}