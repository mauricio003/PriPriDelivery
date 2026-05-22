package com.pripridelivery.viewmodel;

import androidx.lifecycle.ViewModel;
import com.pripridelivery.data.model.Produto;
import com.pripridelivery.data.repository.ProdutoRepository;
import dagger.hilt.android.lifecycle.HiltViewModel;
import kotlinx.coroutines.flow.*;
import javax.inject.Inject;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000N\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0002\b\u0006\b\u0007\u0018\u00002\u00020\u0001B\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0012\u0010\u0011\u001a\u00020\u00122\n\b\u0002\u0010\u0013\u001a\u0004\u0018\u00010\u0014J\u0016\u0010\u0015\u001a\u00020\u00122\u0006\u0010\u0016\u001a\u00020\n2\u0006\u0010\u0017\u001a\u00020\u0018J\u000e\u0010\u0019\u001a\u00020\u00122\u0006\u0010\u001a\u001a\u00020\nJ\u0018\u0010\u001b\u001a\u00020\u00122\u0006\u0010\u001c\u001a\u00020\n2\b\b\u0002\u0010\u001d\u001a\u00020\u001eJ\u0016\u0010\u001f\u001a\u00020\u00122\u0006\u0010\u001c\u001a\u00020\n2\u0006\u0010\u0016\u001a\u00020\nJ\u0006\u0010 \u001a\u00020\u0012J\u000e\u0010!\u001a\u00020\u00182\u0006\u0010\u0016\u001a\u00020\nJ\u000e\u0010\"\u001a\u00020\u00122\u0006\u0010\u0016\u001a\u00020\nJ\u0016\u0010#\u001a\u00020\u00122\u0006\u0010\u001c\u001a\u00020\n2\u0006\u0010\u0013\u001a\u00020\u0014R\u0014\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\b\u001a\b\u0012\u0004\u0012\u00020\n0\t8F\u00a2\u0006\u0006\u001a\u0004\b\u000b\u0010\fR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u00070\u000e\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010\u00a8\u0006$"}, d2 = {"Lcom/pripridelivery/viewmodel/ProdutoViewModel;", "Landroidx/lifecycle/ViewModel;", "produtoRepository", "Lcom/pripridelivery/data/repository/ProdutoRepository;", "(Lcom/pripridelivery/data/repository/ProdutoRepository;)V", "_uiState", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lcom/pripridelivery/viewmodel/ProdutoUiState;", "categorias", "", "", "getCategorias", "()Ljava/util/List;", "uiState", "Lkotlinx/coroutines/flow/StateFlow;", "getUiState", "()Lkotlinx/coroutines/flow/StateFlow;", "abrirFormulario", "", "produto", "Lcom/pripridelivery/data/model/Produto;", "ajustarQuantidade", "produtoId", "delta", "", "atualizarBusca", "texto", "carregarProdutos", "restauranteId", "apenasDisponiveis", "", "excluir", "fecharFormulario", "getQuantidade", "resetarQuantidade", "salvar", "app_debug"})
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class ProdutoViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.pripridelivery.data.repository.ProdutoRepository produtoRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.pripridelivery.viewmodel.ProdutoUiState> _uiState = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.pripridelivery.viewmodel.ProdutoUiState> uiState = null;
    
    @javax.inject.Inject()
    public ProdutoViewModel(@org.jetbrains.annotations.NotNull()
    com.pripridelivery.data.repository.ProdutoRepository produtoRepository) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.pripridelivery.viewmodel.ProdutoUiState> getUiState() {
        return null;
    }
    
    public final void carregarProdutos(@org.jetbrains.annotations.NotNull()
    java.lang.String restauranteId, boolean apenasDisponiveis) {
    }
    
    public final void atualizarBusca(@org.jetbrains.annotations.NotNull()
    java.lang.String texto) {
    }
    
    public final void ajustarQuantidade(@org.jetbrains.annotations.NotNull()
    java.lang.String produtoId, int delta) {
    }
    
    public final void resetarQuantidade(@org.jetbrains.annotations.NotNull()
    java.lang.String produtoId) {
    }
    
    public final int getQuantidade(@org.jetbrains.annotations.NotNull()
    java.lang.String produtoId) {
        return 0;
    }
    
    public final void salvar(@org.jetbrains.annotations.NotNull()
    java.lang.String restauranteId, @org.jetbrains.annotations.NotNull()
    com.pripridelivery.data.model.Produto produto) {
    }
    
    public final void excluir(@org.jetbrains.annotations.NotNull()
    java.lang.String restauranteId, @org.jetbrains.annotations.NotNull()
    java.lang.String produtoId) {
    }
    
    public final void abrirFormulario(@org.jetbrains.annotations.Nullable()
    com.pripridelivery.data.model.Produto produto) {
    }
    
    public final void fecharFormulario() {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<java.lang.String> getCategorias() {
        return null;
    }
}