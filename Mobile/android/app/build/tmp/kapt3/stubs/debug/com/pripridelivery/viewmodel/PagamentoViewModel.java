package com.pripridelivery.viewmodel;

import androidx.lifecycle.ViewModel;
import com.pripridelivery.data.model.*;
import com.pripridelivery.data.repository.CarrinhoRepository;
import com.pripridelivery.data.repository.EnderecoRepository;
import com.pripridelivery.data.repository.PedidoRepository;
import dagger.hilt.android.lifecycle.HiltViewModel;
import kotlinx.coroutines.flow.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.inject.Inject;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000`\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0006\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\b\u0007\u0018\u00002\u00020\u0001B\u001f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\u0002\u0010\bJ\u000e\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u0013J\u0010\u0010\u0014\u001a\u00020\u00112\u0006\u0010\u0015\u001a\u00020\u0016H\u0002J\b\u0010\u0017\u001a\u00020\u0016H\u0002J$\u0010\u0018\u001a\u00020\u00112\f\u0010\u0019\u001a\b\u0012\u0004\u0012\u00020\u001b0\u001a2\u0006\u0010\u001c\u001a\u00020\u001d2\u0006\u0010\u0015\u001a\u00020\u0016J\u000e\u0010\u001e\u001a\u00020\u00112\u0006\u0010\u0015\u001a\u00020\u0016J\u000e\u0010\u001f\u001a\u00020\u00112\u0006\u0010 \u001a\u00020!J\u000e\u0010\"\u001a\u00020\u00112\u0006\u0010#\u001a\u00020\u0016J\u000e\u0010$\u001a\u00020\u00112\u0006\u0010%\u001a\u00020\u0016R\u0014\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u000b0\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u000b0\r\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000f\u00a8\u0006&"}, d2 = {"Lcom/pripridelivery/viewmodel/PagamentoViewModel;", "Landroidx/lifecycle/ViewModel;", "enderecoRepository", "Lcom/pripridelivery/data/repository/EnderecoRepository;", "carrinhoRepository", "Lcom/pripridelivery/data/repository/CarrinhoRepository;", "pedidoRepository", "Lcom/pripridelivery/data/repository/PedidoRepository;", "(Lcom/pripridelivery/data/repository/EnderecoRepository;Lcom/pripridelivery/data/repository/CarrinhoRepository;Lcom/pripridelivery/data/repository/PedidoRepository;)V", "_uiState", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lcom/pripridelivery/viewmodel/PagamentoUiState;", "uiState", "Lkotlinx/coroutines/flow/StateFlow;", "getUiState", "()Lkotlinx/coroutines/flow/StateFlow;", "atualizarCartao", "", "dados", "Lcom/pripridelivery/viewmodel/DadosCartao;", "carregarEnderecos", "userId", "", "generateId", "inicializar", "itens", "", "Lcom/pripridelivery/data/model/ItemCarrinho;", "total", "", "processarPagamento", "selecionarEndereco", "endereco", "Lcom/pripridelivery/data/model/Endereco;", "selecionarFormaPagamento", "forma", "selecionarTipoEntrega", "tipo", "app_debug"})
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class PagamentoViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.pripridelivery.data.repository.EnderecoRepository enderecoRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final com.pripridelivery.data.repository.CarrinhoRepository carrinhoRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final com.pripridelivery.data.repository.PedidoRepository pedidoRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.pripridelivery.viewmodel.PagamentoUiState> _uiState = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.pripridelivery.viewmodel.PagamentoUiState> uiState = null;
    
    @javax.inject.Inject()
    public PagamentoViewModel(@org.jetbrains.annotations.NotNull()
    com.pripridelivery.data.repository.EnderecoRepository enderecoRepository, @org.jetbrains.annotations.NotNull()
    com.pripridelivery.data.repository.CarrinhoRepository carrinhoRepository, @org.jetbrains.annotations.NotNull()
    com.pripridelivery.data.repository.PedidoRepository pedidoRepository) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.pripridelivery.viewmodel.PagamentoUiState> getUiState() {
        return null;
    }
    
    public final void inicializar(@org.jetbrains.annotations.NotNull()
    java.util.List<com.pripridelivery.data.model.ItemCarrinho> itens, double total, @org.jetbrains.annotations.NotNull()
    java.lang.String userId) {
    }
    
    private final void carregarEnderecos(java.lang.String userId) {
    }
    
    public final void selecionarEndereco(@org.jetbrains.annotations.NotNull()
    com.pripridelivery.data.model.Endereco endereco) {
    }
    
    public final void selecionarFormaPagamento(@org.jetbrains.annotations.NotNull()
    java.lang.String forma) {
    }
    
    public final void selecionarTipoEntrega(@org.jetbrains.annotations.NotNull()
    java.lang.String tipo) {
    }
    
    public final void atualizarCartao(@org.jetbrains.annotations.NotNull()
    com.pripridelivery.viewmodel.DadosCartao dados) {
    }
    
    public final void processarPagamento(@org.jetbrains.annotations.NotNull()
    java.lang.String userId) {
    }
    
    private final java.lang.String generateId() {
        return null;
    }
}