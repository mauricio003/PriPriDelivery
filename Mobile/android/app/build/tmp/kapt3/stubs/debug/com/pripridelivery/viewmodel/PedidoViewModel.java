package com.pripridelivery.viewmodel;

import androidx.lifecycle.ViewModel;
import com.pripridelivery.data.model.Pedido;
import com.pripridelivery.data.repository.PedidoRepository;
import dagger.hilt.android.lifecycle.HiltViewModel;
import kotlinx.coroutines.flow.*;
import javax.inject.Inject;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\t\b\u0007\u0018\u00002\u00020\u0001B\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u000e\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\nJ\u000e\u0010\u0014\u001a\u00020\u00122\u0006\u0010\u0015\u001a\u00020\nJ\u0018\u0010\u0016\u001a\u00020\u00122\u0006\u0010\u0017\u001a\u00020\n2\b\b\u0002\u0010\u0018\u001a\u00020\nJ\u000e\u0010\u0019\u001a\u00020\u00122\u0006\u0010\u001a\u001a\u00020\nR\u0014\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\b\u001a\b\u0012\u0004\u0012\u00020\n0\t\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u0017\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u00070\u000e\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010\u00a8\u0006\u001b"}, d2 = {"Lcom/pripridelivery/viewmodel/PedidoViewModel;", "Landroidx/lifecycle/ViewModel;", "pedidoRepository", "Lcom/pripridelivery/data/repository/PedidoRepository;", "(Lcom/pripridelivery/data/repository/PedidoRepository;)V", "_uiState", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lcom/pripridelivery/viewmodel/PedidoUiState;", "statusPedido", "", "", "getStatusPedido", "()Ljava/util/List;", "uiState", "Lkotlinx/coroutines/flow/StateFlow;", "getUiState", "()Lkotlinx/coroutines/flow/StateFlow;", "atualizarCodigoInserido", "", "codigo", "carregarPedidos", "userId", "iniciarAcompanhamento", "pedidoId", "backendUrl", "verificarCodigo", "codigoCorreto", "app_debug"})
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class PedidoViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.pripridelivery.data.repository.PedidoRepository pedidoRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.pripridelivery.viewmodel.PedidoUiState> _uiState = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.pripridelivery.viewmodel.PedidoUiState> uiState = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<java.lang.String> statusPedido = null;
    
    @javax.inject.Inject()
    public PedidoViewModel(@org.jetbrains.annotations.NotNull()
    com.pripridelivery.data.repository.PedidoRepository pedidoRepository) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.pripridelivery.viewmodel.PedidoUiState> getUiState() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<java.lang.String> getStatusPedido() {
        return null;
    }
    
    public final void carregarPedidos(@org.jetbrains.annotations.NotNull()
    java.lang.String userId) {
    }
    
    public final void iniciarAcompanhamento(@org.jetbrains.annotations.NotNull()
    java.lang.String pedidoId, @org.jetbrains.annotations.NotNull()
    java.lang.String backendUrl) {
    }
    
    public final void atualizarCodigoInserido(@org.jetbrains.annotations.NotNull()
    java.lang.String codigo) {
    }
    
    public final void verificarCodigo(@org.jetbrains.annotations.NotNull()
    java.lang.String codigoCorreto) {
    }
}