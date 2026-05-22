package com.pripridelivery.viewmodel;

import androidx.lifecycle.ViewModel;
import com.pripridelivery.data.model.Restaurante;
import com.pripridelivery.data.repository.RestauranteRepository;
import dagger.hilt.android.lifecycle.HiltViewModel;
import kotlinx.coroutines.flow.*;
import javax.inject.Inject;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\n\b\u0007\u0018\u00002\u00020\u0001B\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0012\u0010\f\u001a\u00020\r2\n\b\u0002\u0010\u000e\u001a\u0004\u0018\u00010\u000fJ\u0016\u0010\u0010\u001a\u00020\r2\u0006\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u0012J\u000e\u0010\u0014\u001a\u00020\r2\u0006\u0010\u0015\u001a\u00020\u0012J\u000e\u0010\u0016\u001a\u00020\r2\u0006\u0010\u0017\u001a\u00020\u0012J\u0016\u0010\u0018\u001a\u00020\r2\u0006\u0010\u0017\u001a\u00020\u00122\u0006\u0010\u0015\u001a\u00020\u0012J\u0006\u0010\u0019\u001a\u00020\rJ\u0006\u0010\u001a\u001a\u00020\rJ\u0016\u0010\u001b\u001a\u00020\r2\u0006\u0010\u0017\u001a\u00020\u00122\u0006\u0010\u000e\u001a\u00020\u000fR\u0014\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00070\t\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000b\u00a8\u0006\u001c"}, d2 = {"Lcom/pripridelivery/viewmodel/RestauranteViewModel;", "Landroidx/lifecycle/ViewModel;", "restauranteRepository", "Lcom/pripridelivery/data/repository/RestauranteRepository;", "(Lcom/pripridelivery/data/repository/RestauranteRepository;)V", "_uiState", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lcom/pripridelivery/viewmodel/RestauranteUiState;", "uiState", "Lkotlinx/coroutines/flow/StateFlow;", "getUiState", "()Lkotlinx/coroutines/flow/StateFlow;", "abrirFormulario", "", "restaurante", "Lcom/pripridelivery/data/model/Restaurante;", "adicionarCategoria", "restauranteId", "", "categoria", "carregarPorId", "id", "carregarPorUsuario", "userId", "excluir", "fecharFormulario", "limparSucesso", "salvar", "app_debug"})
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class RestauranteViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.pripridelivery.data.repository.RestauranteRepository restauranteRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.pripridelivery.viewmodel.RestauranteUiState> _uiState = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.pripridelivery.viewmodel.RestauranteUiState> uiState = null;
    
    @javax.inject.Inject()
    public RestauranteViewModel(@org.jetbrains.annotations.NotNull()
    com.pripridelivery.data.repository.RestauranteRepository restauranteRepository) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.pripridelivery.viewmodel.RestauranteUiState> getUiState() {
        return null;
    }
    
    public final void carregarPorUsuario(@org.jetbrains.annotations.NotNull()
    java.lang.String userId) {
    }
    
    public final void carregarPorId(@org.jetbrains.annotations.NotNull()
    java.lang.String id) {
    }
    
    public final void salvar(@org.jetbrains.annotations.NotNull()
    java.lang.String userId, @org.jetbrains.annotations.NotNull()
    com.pripridelivery.data.model.Restaurante restaurante) {
    }
    
    public final void excluir(@org.jetbrains.annotations.NotNull()
    java.lang.String userId, @org.jetbrains.annotations.NotNull()
    java.lang.String id) {
    }
    
    public final void adicionarCategoria(@org.jetbrains.annotations.NotNull()
    java.lang.String restauranteId, @org.jetbrains.annotations.NotNull()
    java.lang.String categoria) {
    }
    
    public final void abrirFormulario(@org.jetbrains.annotations.Nullable()
    com.pripridelivery.data.model.Restaurante restaurante) {
    }
    
    public final void fecharFormulario() {
    }
    
    public final void limparSucesso() {
    }
}