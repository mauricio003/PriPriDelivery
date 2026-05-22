package com.pripridelivery.viewmodel;

import androidx.lifecycle.ViewModel;
import com.pripridelivery.data.model.Produto;
import com.pripridelivery.data.repository.ProdutoRepository;
import dagger.hilt.android.lifecycle.HiltViewModel;
import kotlinx.coroutines.flow.*;
import javax.inject.Inject;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0010$\n\u0002\u0010\b\n\u0002\b \b\u0086\b\u0018\u00002\u00020\u0001B\u0085\u0001\u0012\u000e\b\u0002\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003\u0012\u000e\b\u0002\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0007\u0012\b\b\u0002\u0010\b\u001a\u00020\u0007\u0012\b\b\u0002\u0010\t\u001a\u00020\u0007\u0012\n\b\u0002\u0010\n\u001a\u0004\u0018\u00010\u000b\u0012\b\b\u0002\u0010\f\u001a\u00020\u000b\u0012\b\b\u0002\u0010\r\u001a\u00020\u0007\u0012\n\b\u0002\u0010\u000e\u001a\u0004\u0018\u00010\u0004\u0012\u0014\b\u0002\u0010\u000f\u001a\u000e\u0012\u0004\u0012\u00020\u000b\u0012\u0004\u0012\u00020\u00110\u0010\u00a2\u0006\u0002\u0010\u0012J\u000f\u0010\"\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003H\u00c6\u0003J\u0015\u0010#\u001a\u000e\u0012\u0004\u0012\u00020\u000b\u0012\u0004\u0012\u00020\u00110\u0010H\u00c6\u0003J\u000f\u0010$\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003H\u00c6\u0003J\t\u0010%\u001a\u00020\u0007H\u00c6\u0003J\t\u0010&\u001a\u00020\u0007H\u00c6\u0003J\t\u0010\'\u001a\u00020\u0007H\u00c6\u0003J\u000b\u0010(\u001a\u0004\u0018\u00010\u000bH\u00c6\u0003J\t\u0010)\u001a\u00020\u000bH\u00c6\u0003J\t\u0010*\u001a\u00020\u0007H\u00c6\u0003J\u000b\u0010+\u001a\u0004\u0018\u00010\u0004H\u00c6\u0003J\u0089\u0001\u0010,\u001a\u00020\u00002\u000e\b\u0002\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u00032\u000e\b\u0002\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00040\u00032\b\b\u0002\u0010\u0006\u001a\u00020\u00072\b\b\u0002\u0010\b\u001a\u00020\u00072\b\b\u0002\u0010\t\u001a\u00020\u00072\n\b\u0002\u0010\n\u001a\u0004\u0018\u00010\u000b2\b\b\u0002\u0010\f\u001a\u00020\u000b2\b\b\u0002\u0010\r\u001a\u00020\u00072\n\b\u0002\u0010\u000e\u001a\u0004\u0018\u00010\u00042\u0014\b\u0002\u0010\u000f\u001a\u000e\u0012\u0004\u0012\u00020\u000b\u0012\u0004\u0012\u00020\u00110\u0010H\u00c6\u0001J\u0013\u0010-\u001a\u00020\u00072\b\u0010.\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010/\u001a\u00020\u0011H\u00d6\u0001J\t\u00100\u001a\u00020\u000bH\u00d6\u0001R\u0011\u0010\f\u001a\u00020\u000b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0014R\u0011\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0016R\u0013\u0010\n\u001a\u0004\u0018\u00010\u000b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\u0014R\u0011\u0010\r\u001a\u00020\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0018\u0010\u0016R\u0013\u0010\u000e\u001a\u0004\u0018\u00010\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0019\u0010\u001aR\u0017\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001b\u0010\u001cR\u0017\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001d\u0010\u001cR\u001d\u0010\u000f\u001a\u000e\u0012\u0004\u0012\u00020\u000b\u0012\u0004\u0012\u00020\u00110\u0010\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001e\u0010\u001fR\u0011\u0010\b\u001a\u00020\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b \u0010\u0016R\u0011\u0010\t\u001a\u00020\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b!\u0010\u0016\u00a8\u00061"}, d2 = {"Lcom/pripridelivery/viewmodel/ProdutoUiState;", "", "produtos", "", "Lcom/pripridelivery/data/model/Produto;", "produtosFiltrados", "carregando", "", "salvando", "sucesso", "erro", "", "busca", "mostrarFormulario", "produtoEditando", "quantidades", "", "", "(Ljava/util/List;Ljava/util/List;ZZZLjava/lang/String;Ljava/lang/String;ZLcom/pripridelivery/data/model/Produto;Ljava/util/Map;)V", "getBusca", "()Ljava/lang/String;", "getCarregando", "()Z", "getErro", "getMostrarFormulario", "getProdutoEditando", "()Lcom/pripridelivery/data/model/Produto;", "getProdutos", "()Ljava/util/List;", "getProdutosFiltrados", "getQuantidades", "()Ljava/util/Map;", "getSalvando", "getSucesso", "component1", "component10", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "copy", "equals", "other", "hashCode", "toString", "app_debug"})
public final class ProdutoUiState {
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.pripridelivery.data.model.Produto> produtos = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.pripridelivery.data.model.Produto> produtosFiltrados = null;
    private final boolean carregando = false;
    private final boolean salvando = false;
    private final boolean sucesso = false;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String erro = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String busca = null;
    private final boolean mostrarFormulario = false;
    @org.jetbrains.annotations.Nullable()
    private final com.pripridelivery.data.model.Produto produtoEditando = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.Map<java.lang.String, java.lang.Integer> quantidades = null;
    
    public ProdutoUiState(@org.jetbrains.annotations.NotNull()
    java.util.List<com.pripridelivery.data.model.Produto> produtos, @org.jetbrains.annotations.NotNull()
    java.util.List<com.pripridelivery.data.model.Produto> produtosFiltrados, boolean carregando, boolean salvando, boolean sucesso, @org.jetbrains.annotations.Nullable()
    java.lang.String erro, @org.jetbrains.annotations.NotNull()
    java.lang.String busca, boolean mostrarFormulario, @org.jetbrains.annotations.Nullable()
    com.pripridelivery.data.model.Produto produtoEditando, @org.jetbrains.annotations.NotNull()
    java.util.Map<java.lang.String, java.lang.Integer> quantidades) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.pripridelivery.data.model.Produto> getProdutos() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.pripridelivery.data.model.Produto> getProdutosFiltrados() {
        return null;
    }
    
    public final boolean getCarregando() {
        return false;
    }
    
    public final boolean getSalvando() {
        return false;
    }
    
    public final boolean getSucesso() {
        return false;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getErro() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getBusca() {
        return null;
    }
    
    public final boolean getMostrarFormulario() {
        return false;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.pripridelivery.data.model.Produto getProdutoEditando() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.Map<java.lang.String, java.lang.Integer> getQuantidades() {
        return null;
    }
    
    public ProdutoUiState() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.pripridelivery.data.model.Produto> component1() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.Map<java.lang.String, java.lang.Integer> component10() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.pripridelivery.data.model.Produto> component2() {
        return null;
    }
    
    public final boolean component3() {
        return false;
    }
    
    public final boolean component4() {
        return false;
    }
    
    public final boolean component5() {
        return false;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component6() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component7() {
        return null;
    }
    
    public final boolean component8() {
        return false;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.pripridelivery.data.model.Produto component9() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.pripridelivery.viewmodel.ProdutoUiState copy(@org.jetbrains.annotations.NotNull()
    java.util.List<com.pripridelivery.data.model.Produto> produtos, @org.jetbrains.annotations.NotNull()
    java.util.List<com.pripridelivery.data.model.Produto> produtosFiltrados, boolean carregando, boolean salvando, boolean sucesso, @org.jetbrains.annotations.Nullable()
    java.lang.String erro, @org.jetbrains.annotations.NotNull()
    java.lang.String busca, boolean mostrarFormulario, @org.jetbrains.annotations.Nullable()
    com.pripridelivery.data.model.Produto produtoEditando, @org.jetbrains.annotations.NotNull()
    java.util.Map<java.lang.String, java.lang.Integer> quantidades) {
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