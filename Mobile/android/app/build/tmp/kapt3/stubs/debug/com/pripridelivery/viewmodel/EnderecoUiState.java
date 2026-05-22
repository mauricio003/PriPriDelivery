package com.pripridelivery.viewmodel;

import androidx.lifecycle.ViewModel;
import com.pripridelivery.data.model.Endereco;
import com.pripridelivery.data.repository.EnderecoRepository;
import dagger.hilt.android.lifecycle.HiltViewModel;
import kotlinx.coroutines.flow.*;
import javax.inject.Inject;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u001b\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\b\u0018\u00002\u00020\u0001BU\u0012\u000e\b\u0002\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003\u0012\b\b\u0002\u0010\u0005\u001a\u00020\u0006\u0012\n\b\u0002\u0010\u0007\u001a\u0004\u0018\u00010\b\u0012\b\b\u0002\u0010\t\u001a\u00020\u0006\u0012\b\b\u0002\u0010\n\u001a\u00020\u0006\u0012\n\b\u0002\u0010\u000b\u001a\u0004\u0018\u00010\u0004\u0012\b\b\u0002\u0010\f\u001a\u00020\u0006\u00a2\u0006\u0002\u0010\rJ\u000f\u0010\u0019\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003H\u00c6\u0003J\t\u0010\u001a\u001a\u00020\u0006H\u00c6\u0003J\u000b\u0010\u001b\u001a\u0004\u0018\u00010\bH\u00c6\u0003J\t\u0010\u001c\u001a\u00020\u0006H\u00c6\u0003J\t\u0010\u001d\u001a\u00020\u0006H\u00c6\u0003J\u000b\u0010\u001e\u001a\u0004\u0018\u00010\u0004H\u00c6\u0003J\t\u0010\u001f\u001a\u00020\u0006H\u00c6\u0003JY\u0010 \u001a\u00020\u00002\u000e\b\u0002\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00062\n\b\u0002\u0010\u0007\u001a\u0004\u0018\u00010\b2\b\b\u0002\u0010\t\u001a\u00020\u00062\b\b\u0002\u0010\n\u001a\u00020\u00062\n\b\u0002\u0010\u000b\u001a\u0004\u0018\u00010\u00042\b\b\u0002\u0010\f\u001a\u00020\u0006H\u00c6\u0001J\u0013\u0010!\u001a\u00020\u00062\b\u0010\"\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010#\u001a\u00020$H\u00d6\u0001J\t\u0010%\u001a\u00020\bH\u00d6\u0001R\u0011\u0010\u0005\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000fR\u0013\u0010\u000b\u001a\u0004\u0018\u00010\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0011R\u0017\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0013R\u0013\u0010\u0007\u001a\u0004\u0018\u00010\b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u0015R\u0011\u0010\f\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010\u000fR\u0011\u0010\t\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\u000fR\u0011\u0010\n\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0018\u0010\u000f\u00a8\u0006&"}, d2 = {"Lcom/pripridelivery/viewmodel/EnderecoUiState;", "", "enderecos", "", "Lcom/pripridelivery/data/model/Endereco;", "carregando", "", "erro", "", "salvando", "sucesso", "enderecoEditando", "mostrarFormulario", "(Ljava/util/List;ZLjava/lang/String;ZZLcom/pripridelivery/data/model/Endereco;Z)V", "getCarregando", "()Z", "getEnderecoEditando", "()Lcom/pripridelivery/data/model/Endereco;", "getEnderecos", "()Ljava/util/List;", "getErro", "()Ljava/lang/String;", "getMostrarFormulario", "getSalvando", "getSucesso", "component1", "component2", "component3", "component4", "component5", "component6", "component7", "copy", "equals", "other", "hashCode", "", "toString", "app_debug"})
public final class EnderecoUiState {
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.pripridelivery.data.model.Endereco> enderecos = null;
    private final boolean carregando = false;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String erro = null;
    private final boolean salvando = false;
    private final boolean sucesso = false;
    @org.jetbrains.annotations.Nullable()
    private final com.pripridelivery.data.model.Endereco enderecoEditando = null;
    private final boolean mostrarFormulario = false;
    
    public EnderecoUiState(@org.jetbrains.annotations.NotNull()
    java.util.List<com.pripridelivery.data.model.Endereco> enderecos, boolean carregando, @org.jetbrains.annotations.Nullable()
    java.lang.String erro, boolean salvando, boolean sucesso, @org.jetbrains.annotations.Nullable()
    com.pripridelivery.data.model.Endereco enderecoEditando, boolean mostrarFormulario) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.pripridelivery.data.model.Endereco> getEnderecos() {
        return null;
    }
    
    public final boolean getCarregando() {
        return false;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getErro() {
        return null;
    }
    
    public final boolean getSalvando() {
        return false;
    }
    
    public final boolean getSucesso() {
        return false;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.pripridelivery.data.model.Endereco getEnderecoEditando() {
        return null;
    }
    
    public final boolean getMostrarFormulario() {
        return false;
    }
    
    public EnderecoUiState() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.pripridelivery.data.model.Endereco> component1() {
        return null;
    }
    
    public final boolean component2() {
        return false;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component3() {
        return null;
    }
    
    public final boolean component4() {
        return false;
    }
    
    public final boolean component5() {
        return false;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.pripridelivery.data.model.Endereco component6() {
        return null;
    }
    
    public final boolean component7() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.pripridelivery.viewmodel.EnderecoUiState copy(@org.jetbrains.annotations.NotNull()
    java.util.List<com.pripridelivery.data.model.Endereco> enderecos, boolean carregando, @org.jetbrains.annotations.Nullable()
    java.lang.String erro, boolean salvando, boolean sucesso, @org.jetbrains.annotations.Nullable()
    com.pripridelivery.data.model.Endereco enderecoEditando, boolean mostrarFormulario) {
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