package com.pripridelivery.viewmodel;

import androidx.lifecycle.ViewModel;
import com.pripridelivery.data.model.Pedido;
import com.pripridelivery.data.repository.PedidoRepository;
import dagger.hilt.android.lifecycle.HiltViewModel;
import kotlinx.coroutines.flow.*;
import javax.inject.Inject;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u001d\b\u0086\b\u0018\u00002\u00020\u0001B]\u0012\u000e\b\u0002\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003\u0012\b\b\u0002\u0010\u0005\u001a\u00020\u0006\u0012\b\b\u0002\u0010\u0007\u001a\u00020\b\u0012\b\b\u0002\u0010\t\u001a\u00020\u0006\u0012\b\b\u0002\u0010\n\u001a\u00020\u0006\u0012\b\b\u0002\u0010\u000b\u001a\u00020\f\u0012\b\b\u0002\u0010\r\u001a\u00020\f\u0012\n\b\u0002\u0010\u000e\u001a\u0004\u0018\u00010\f\u00a2\u0006\u0002\u0010\u000fJ\u000f\u0010\u001c\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003H\u00c6\u0003J\t\u0010\u001d\u001a\u00020\u0006H\u00c6\u0003J\t\u0010\u001e\u001a\u00020\bH\u00c6\u0003J\t\u0010\u001f\u001a\u00020\u0006H\u00c6\u0003J\t\u0010 \u001a\u00020\u0006H\u00c6\u0003J\t\u0010!\u001a\u00020\fH\u00c6\u0003J\t\u0010\"\u001a\u00020\fH\u00c6\u0003J\u000b\u0010#\u001a\u0004\u0018\u00010\fH\u00c6\u0003Ja\u0010$\u001a\u00020\u00002\u000e\b\u0002\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00062\b\b\u0002\u0010\u0007\u001a\u00020\b2\b\b\u0002\u0010\t\u001a\u00020\u00062\b\b\u0002\u0010\n\u001a\u00020\u00062\b\b\u0002\u0010\u000b\u001a\u00020\f2\b\b\u0002\u0010\r\u001a\u00020\f2\n\b\u0002\u0010\u000e\u001a\u0004\u0018\u00010\fH\u00c6\u0001J\u0013\u0010%\u001a\u00020\u00062\b\u0010&\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\'\u001a\u00020\bH\u00d6\u0001J\t\u0010(\u001a\u00020\fH\u00d6\u0001R\u0011\u0010\u0005\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0011R\u0011\u0010\u000b\u001a\u00020\f\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0013R\u0013\u0010\u000e\u001a\u0004\u0018\u00010\f\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u0013R\u0011\u0010\r\u001a\u00020\f\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0013R\u0011\u0010\n\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010\u0011R\u0011\u0010\t\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\u0011R\u0017\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0018\u0010\u0019R\u0011\u0010\u0007\u001a\u00020\b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001a\u0010\u001b\u00a8\u0006)"}, d2 = {"Lcom/pripridelivery/viewmodel/PedidoUiState;", "", "pedidos", "", "Lcom/pripridelivery/data/model/Pedido;", "carregando", "", "statusAtual", "", "pedidoConcluido", "mostrarModalVerificacao", "codigoInserido", "", "erroVerificacao", "erro", "(Ljava/util/List;ZIZZLjava/lang/String;Ljava/lang/String;Ljava/lang/String;)V", "getCarregando", "()Z", "getCodigoInserido", "()Ljava/lang/String;", "getErro", "getErroVerificacao", "getMostrarModalVerificacao", "getPedidoConcluido", "getPedidos", "()Ljava/util/List;", "getStatusAtual", "()I", "component1", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "copy", "equals", "other", "hashCode", "toString", "app_debug"})
public final class PedidoUiState {
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.pripridelivery.data.model.Pedido> pedidos = null;
    private final boolean carregando = false;
    private final int statusAtual = 0;
    private final boolean pedidoConcluido = false;
    private final boolean mostrarModalVerificacao = false;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String codigoInserido = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String erroVerificacao = null;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String erro = null;
    
    public PedidoUiState(@org.jetbrains.annotations.NotNull()
    java.util.List<com.pripridelivery.data.model.Pedido> pedidos, boolean carregando, int statusAtual, boolean pedidoConcluido, boolean mostrarModalVerificacao, @org.jetbrains.annotations.NotNull()
    java.lang.String codigoInserido, @org.jetbrains.annotations.NotNull()
    java.lang.String erroVerificacao, @org.jetbrains.annotations.Nullable()
    java.lang.String erro) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.pripridelivery.data.model.Pedido> getPedidos() {
        return null;
    }
    
    public final boolean getCarregando() {
        return false;
    }
    
    public final int getStatusAtual() {
        return 0;
    }
    
    public final boolean getPedidoConcluido() {
        return false;
    }
    
    public final boolean getMostrarModalVerificacao() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getCodigoInserido() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getErroVerificacao() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getErro() {
        return null;
    }
    
    public PedidoUiState() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.pripridelivery.data.model.Pedido> component1() {
        return null;
    }
    
    public final boolean component2() {
        return false;
    }
    
    public final int component3() {
        return 0;
    }
    
    public final boolean component4() {
        return false;
    }
    
    public final boolean component5() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component6() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component7() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component8() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.pripridelivery.viewmodel.PedidoUiState copy(@org.jetbrains.annotations.NotNull()
    java.util.List<com.pripridelivery.data.model.Pedido> pedidos, boolean carregando, int statusAtual, boolean pedidoConcluido, boolean mostrarModalVerificacao, @org.jetbrains.annotations.NotNull()
    java.lang.String codigoInserido, @org.jetbrains.annotations.NotNull()
    java.lang.String erroVerificacao, @org.jetbrains.annotations.Nullable()
    java.lang.String erro) {
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