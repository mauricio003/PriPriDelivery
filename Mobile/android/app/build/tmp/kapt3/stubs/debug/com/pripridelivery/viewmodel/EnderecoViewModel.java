package com.pripridelivery.viewmodel;

import androidx.lifecycle.ViewModel;
import com.pripridelivery.data.model.Endereco;
import com.pripridelivery.data.repository.EnderecoRepository;
import dagger.hilt.android.lifecycle.HiltViewModel;
import kotlinx.coroutines.flow.*;
import javax.inject.Inject;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000<\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0007\u0018\u00002\u00020\u0001B\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0006\u0010\f\u001a\u00020\rJ\u000e\u0010\u000e\u001a\u00020\r2\u0006\u0010\u000f\u001a\u00020\u0010J\u0016\u0010\u0011\u001a\u00020\r2\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0012\u001a\u00020\u0010J\u0010\u0010\u0013\u001a\u00020\r2\b\u0010\u0014\u001a\u0004\u0018\u00010\u0015J\u0006\u0010\u0016\u001a\u00020\rJ\u0016\u0010\u0017\u001a\u00020\r2\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0014\u001a\u00020\u0015R\u0014\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00070\t\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000b\u00a8\u0006\u0018"}, d2 = {"Lcom/pripridelivery/viewmodel/EnderecoViewModel;", "Landroidx/lifecycle/ViewModel;", "enderecoRepository", "Lcom/pripridelivery/data/repository/EnderecoRepository;", "(Lcom/pripridelivery/data/repository/EnderecoRepository;)V", "_uiState", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lcom/pripridelivery/viewmodel/EnderecoUiState;", "uiState", "Lkotlinx/coroutines/flow/StateFlow;", "getUiState", "()Lkotlinx/coroutines/flow/StateFlow;", "cancelarFormulario", "", "carregarEnderecos", "userId", "", "excluirEndereco", "id", "iniciarEdicao", "endereco", "Lcom/pripridelivery/data/model/Endereco;", "limparSucesso", "salvarEndereco", "app_debug"})
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class EnderecoViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.pripridelivery.data.repository.EnderecoRepository enderecoRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.pripridelivery.viewmodel.EnderecoUiState> _uiState = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.pripridelivery.viewmodel.EnderecoUiState> uiState = null;
    
    @javax.inject.Inject()
    public EnderecoViewModel(@org.jetbrains.annotations.NotNull()
    com.pripridelivery.data.repository.EnderecoRepository enderecoRepository) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.pripridelivery.viewmodel.EnderecoUiState> getUiState() {
        return null;
    }
    
    public final void carregarEnderecos(@org.jetbrains.annotations.NotNull()
    java.lang.String userId) {
    }
    
    public final void salvarEndereco(@org.jetbrains.annotations.NotNull()
    java.lang.String userId, @org.jetbrains.annotations.NotNull()
    com.pripridelivery.data.model.Endereco endereco) {
    }
    
    public final void excluirEndereco(@org.jetbrains.annotations.NotNull()
    java.lang.String userId, @org.jetbrains.annotations.NotNull()
    java.lang.String id) {
    }
    
    public final void iniciarEdicao(@org.jetbrains.annotations.Nullable()
    com.pripridelivery.data.model.Endereco endereco) {
    }
    
    public final void cancelarFormulario() {
    }
    
    public final void limparSucesso() {
    }
}