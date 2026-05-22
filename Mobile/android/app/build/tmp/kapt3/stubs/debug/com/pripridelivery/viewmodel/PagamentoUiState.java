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

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000B\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0006\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b%\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\b\u0018\u00002\u00020\u0001B\u009b\u0001\u0012\u000e\b\u0002\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003\u0012\b\b\u0002\u0010\u0005\u001a\u00020\u0006\u0012\u000e\b\u0002\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\b0\u0003\u0012\n\b\u0002\u0010\t\u001a\u0004\u0018\u00010\b\u0012\b\b\u0002\u0010\n\u001a\u00020\u000b\u0012\b\b\u0002\u0010\f\u001a\u00020\u000b\u0012\b\b\u0002\u0010\r\u001a\u00020\u000e\u0012\n\b\u0002\u0010\u000f\u001a\u0004\u0018\u00010\u000b\u0012\b\b\u0002\u0010\u0010\u001a\u00020\u000e\u0012\n\b\u0002\u0010\u0011\u001a\u0004\u0018\u00010\u000b\u0012\n\b\u0002\u0010\u0012\u001a\u0004\u0018\u00010\u000b\u0012\b\b\u0002\u0010\u0013\u001a\u00020\u000e\u0012\b\b\u0002\u0010\u0014\u001a\u00020\u0015\u00a2\u0006\u0002\u0010\u0016J\u000f\u0010*\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003H\u00c6\u0003J\u000b\u0010+\u001a\u0004\u0018\u00010\u000bH\u00c6\u0003J\u000b\u0010,\u001a\u0004\u0018\u00010\u000bH\u00c6\u0003J\t\u0010-\u001a\u00020\u000eH\u00c6\u0003J\t\u0010.\u001a\u00020\u0015H\u00c6\u0003J\t\u0010/\u001a\u00020\u0006H\u00c6\u0003J\u000f\u00100\u001a\b\u0012\u0004\u0012\u00020\b0\u0003H\u00c6\u0003J\u000b\u00101\u001a\u0004\u0018\u00010\bH\u00c6\u0003J\t\u00102\u001a\u00020\u000bH\u00c6\u0003J\t\u00103\u001a\u00020\u000bH\u00c6\u0003J\t\u00104\u001a\u00020\u000eH\u00c6\u0003J\u000b\u00105\u001a\u0004\u0018\u00010\u000bH\u00c6\u0003J\t\u00106\u001a\u00020\u000eH\u00c6\u0003J\u009f\u0001\u00107\u001a\u00020\u00002\u000e\b\u0002\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00062\u000e\b\u0002\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\b0\u00032\n\b\u0002\u0010\t\u001a\u0004\u0018\u00010\b2\b\b\u0002\u0010\n\u001a\u00020\u000b2\b\b\u0002\u0010\f\u001a\u00020\u000b2\b\b\u0002\u0010\r\u001a\u00020\u000e2\n\b\u0002\u0010\u000f\u001a\u0004\u0018\u00010\u000b2\b\b\u0002\u0010\u0010\u001a\u00020\u000e2\n\b\u0002\u0010\u0011\u001a\u0004\u0018\u00010\u000b2\n\b\u0002\u0010\u0012\u001a\u0004\u0018\u00010\u000b2\b\b\u0002\u0010\u0013\u001a\u00020\u000e2\b\b\u0002\u0010\u0014\u001a\u00020\u0015H\u00c6\u0001J\u0013\u00108\u001a\u00020\u000e2\b\u00109\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010:\u001a\u00020;H\u00d6\u0001J\t\u0010<\u001a\u00020\u000bH\u00d6\u0001R\u0011\u0010\r\u001a\u00020\u000e\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\u0018R\u0013\u0010\u0012\u001a\u0004\u0018\u00010\u000b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0019\u0010\u001aR\u0011\u0010\u0014\u001a\u00020\u0015\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001b\u0010\u001cR\u0013\u0010\t\u001a\u0004\u0018\u00010\b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001d\u0010\u001eR\u0017\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\b0\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001f\u0010 R\u0013\u0010\u000f\u001a\u0004\u0018\u00010\u000b\u00a2\u0006\b\n\u0000\u001a\u0004\b!\u0010\u001aR\u0011\u0010\n\u001a\u00020\u000b\u00a2\u0006\b\n\u0000\u001a\u0004\b\"\u0010\u001aR\u0017\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b#\u0010 R\u0011\u0010\u0013\u001a\u00020\u000e\u00a2\u0006\b\n\u0000\u001a\u0004\b$\u0010\u0018R\u0013\u0010\u0011\u001a\u0004\u0018\u00010\u000b\u00a2\u0006\b\n\u0000\u001a\u0004\b%\u0010\u001aR\u0011\u0010\u0010\u001a\u00020\u000e\u00a2\u0006\b\n\u0000\u001a\u0004\b&\u0010\u0018R\u0011\u0010\f\u001a\u00020\u000b\u00a2\u0006\b\n\u0000\u001a\u0004\b\'\u0010\u001aR\u0011\u0010\u0005\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b(\u0010)\u00a8\u0006="}, d2 = {"Lcom/pripridelivery/viewmodel/PagamentoUiState;", "", "itens", "", "Lcom/pripridelivery/data/model/ItemCarrinho;", "total", "", "enderecos", "Lcom/pripridelivery/data/model/Endereco;", "enderecoSelecionado", "formaPagamento", "", "tipoEntrega", "carregando", "", "erro", "sucesso", "pedidoId", "codigoVerificacao", "mostrarNovoEndereco", "dadosCartao", "Lcom/pripridelivery/viewmodel/DadosCartao;", "(Ljava/util/List;DLjava/util/List;Lcom/pripridelivery/data/model/Endereco;Ljava/lang/String;Ljava/lang/String;ZLjava/lang/String;ZLjava/lang/String;Ljava/lang/String;ZLcom/pripridelivery/viewmodel/DadosCartao;)V", "getCarregando", "()Z", "getCodigoVerificacao", "()Ljava/lang/String;", "getDadosCartao", "()Lcom/pripridelivery/viewmodel/DadosCartao;", "getEnderecoSelecionado", "()Lcom/pripridelivery/data/model/Endereco;", "getEnderecos", "()Ljava/util/List;", "getErro", "getFormaPagamento", "getItens", "getMostrarNovoEndereco", "getPedidoId", "getSucesso", "getTipoEntrega", "getTotal", "()D", "component1", "component10", "component11", "component12", "component13", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "copy", "equals", "other", "hashCode", "", "toString", "app_debug"})
public final class PagamentoUiState {
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.pripridelivery.data.model.ItemCarrinho> itens = null;
    private final double total = 0.0;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.pripridelivery.data.model.Endereco> enderecos = null;
    @org.jetbrains.annotations.Nullable()
    private final com.pripridelivery.data.model.Endereco enderecoSelecionado = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String formaPagamento = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String tipoEntrega = null;
    private final boolean carregando = false;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String erro = null;
    private final boolean sucesso = false;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String pedidoId = null;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String codigoVerificacao = null;
    private final boolean mostrarNovoEndereco = false;
    @org.jetbrains.annotations.NotNull()
    private final com.pripridelivery.viewmodel.DadosCartao dadosCartao = null;
    
    public PagamentoUiState(@org.jetbrains.annotations.NotNull()
    java.util.List<com.pripridelivery.data.model.ItemCarrinho> itens, double total, @org.jetbrains.annotations.NotNull()
    java.util.List<com.pripridelivery.data.model.Endereco> enderecos, @org.jetbrains.annotations.Nullable()
    com.pripridelivery.data.model.Endereco enderecoSelecionado, @org.jetbrains.annotations.NotNull()
    java.lang.String formaPagamento, @org.jetbrains.annotations.NotNull()
    java.lang.String tipoEntrega, boolean carregando, @org.jetbrains.annotations.Nullable()
    java.lang.String erro, boolean sucesso, @org.jetbrains.annotations.Nullable()
    java.lang.String pedidoId, @org.jetbrains.annotations.Nullable()
    java.lang.String codigoVerificacao, boolean mostrarNovoEndereco, @org.jetbrains.annotations.NotNull()
    com.pripridelivery.viewmodel.DadosCartao dadosCartao) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.pripridelivery.data.model.ItemCarrinho> getItens() {
        return null;
    }
    
    public final double getTotal() {
        return 0.0;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.pripridelivery.data.model.Endereco> getEnderecos() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.pripridelivery.data.model.Endereco getEnderecoSelecionado() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getFormaPagamento() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getTipoEntrega() {
        return null;
    }
    
    public final boolean getCarregando() {
        return false;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getErro() {
        return null;
    }
    
    public final boolean getSucesso() {
        return false;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getPedidoId() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getCodigoVerificacao() {
        return null;
    }
    
    public final boolean getMostrarNovoEndereco() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.pripridelivery.viewmodel.DadosCartao getDadosCartao() {
        return null;
    }
    
    public PagamentoUiState() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.pripridelivery.data.model.ItemCarrinho> component1() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component10() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component11() {
        return null;
    }
    
    public final boolean component12() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.pripridelivery.viewmodel.DadosCartao component13() {
        return null;
    }
    
    public final double component2() {
        return 0.0;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.pripridelivery.data.model.Endereco> component3() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.pripridelivery.data.model.Endereco component4() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component5() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component6() {
        return null;
    }
    
    public final boolean component7() {
        return false;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component8() {
        return null;
    }
    
    public final boolean component9() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.pripridelivery.viewmodel.PagamentoUiState copy(@org.jetbrains.annotations.NotNull()
    java.util.List<com.pripridelivery.data.model.ItemCarrinho> itens, double total, @org.jetbrains.annotations.NotNull()
    java.util.List<com.pripridelivery.data.model.Endereco> enderecos, @org.jetbrains.annotations.Nullable()
    com.pripridelivery.data.model.Endereco enderecoSelecionado, @org.jetbrains.annotations.NotNull()
    java.lang.String formaPagamento, @org.jetbrains.annotations.NotNull()
    java.lang.String tipoEntrega, boolean carregando, @org.jetbrains.annotations.Nullable()
    java.lang.String erro, boolean sucesso, @org.jetbrains.annotations.Nullable()
    java.lang.String pedidoId, @org.jetbrains.annotations.Nullable()
    java.lang.String codigoVerificacao, boolean mostrarNovoEndereco, @org.jetbrains.annotations.NotNull()
    com.pripridelivery.viewmodel.DadosCartao dadosCartao) {
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