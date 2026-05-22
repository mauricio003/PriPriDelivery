package com.pripridelivery.viewmodel;

import androidx.lifecycle.ViewModel;
import com.pripridelivery.data.model.ItemCarrinho;
import com.pripridelivery.data.repository.CarrinhoRepository;
import dagger.hilt.android.lifecycle.HiltViewModel;
import kotlinx.coroutines.flow.*;
import javax.inject.Inject;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0006\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0014\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\b\u0018\u00002\u00020\u0001B?\u0012\u000e\b\u0002\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003\u0012\b\b\u0002\u0010\u0005\u001a\u00020\u0006\u0012\b\b\u0002\u0010\u0007\u001a\u00020\b\u0012\n\b\u0002\u0010\t\u001a\u0004\u0018\u00010\n\u0012\b\b\u0002\u0010\u000b\u001a\u00020\b\u00a2\u0006\u0002\u0010\fJ\u000f\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003H\u00c6\u0003J\t\u0010\u0017\u001a\u00020\u0006H\u00c6\u0003J\t\u0010\u0018\u001a\u00020\bH\u00c6\u0003J\u000b\u0010\u0019\u001a\u0004\u0018\u00010\nH\u00c6\u0003J\t\u0010\u001a\u001a\u00020\bH\u00c6\u0003JC\u0010\u001b\u001a\u00020\u00002\u000e\b\u0002\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00062\b\b\u0002\u0010\u0007\u001a\u00020\b2\n\b\u0002\u0010\t\u001a\u0004\u0018\u00010\n2\b\b\u0002\u0010\u000b\u001a\u00020\bH\u00c6\u0001J\u0013\u0010\u001c\u001a\u00020\b2\b\u0010\u001d\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\u001e\u001a\u00020\u001fH\u00d6\u0001J\t\u0010 \u001a\u00020\nH\u00d6\u0001R\u0011\u0010\u0007\u001a\u00020\b\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000eR\u0011\u0010\u000b\u001a\u00020\b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u000eR\u0013\u0010\t\u001a\u0004\u0018\u00010\n\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0011R\u0017\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0013R\u0011\u0010\u0005\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u0015\u00a8\u0006!"}, d2 = {"Lcom/pripridelivery/viewmodel/CarrinhoUiState;", "", "itens", "", "Lcom/pripridelivery/data/model/ItemCarrinho;", "total", "", "carregando", "", "erro", "", "carrinhoAberto", "(Ljava/util/List;DZLjava/lang/String;Z)V", "getCarregando", "()Z", "getCarrinhoAberto", "getErro", "()Ljava/lang/String;", "getItens", "()Ljava/util/List;", "getTotal", "()D", "component1", "component2", "component3", "component4", "component5", "copy", "equals", "other", "hashCode", "", "toString", "app_debug"})
public final class CarrinhoUiState {
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.pripridelivery.data.model.ItemCarrinho> itens = null;
    private final double total = 0.0;
    private final boolean carregando = false;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String erro = null;
    private final boolean carrinhoAberto = false;
    
    public CarrinhoUiState(@org.jetbrains.annotations.NotNull()
    java.util.List<com.pripridelivery.data.model.ItemCarrinho> itens, double total, boolean carregando, @org.jetbrains.annotations.Nullable()
    java.lang.String erro, boolean carrinhoAberto) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.pripridelivery.data.model.ItemCarrinho> getItens() {
        return null;
    }
    
    public final double getTotal() {
        return 0.0;
    }
    
    public final boolean getCarregando() {
        return false;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getErro() {
        return null;
    }
    
    public final boolean getCarrinhoAberto() {
        return false;
    }
    
    public CarrinhoUiState() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.pripridelivery.data.model.ItemCarrinho> component1() {
        return null;
    }
    
    public final double component2() {
        return 0.0;
    }
    
    public final boolean component3() {
        return false;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component4() {
        return null;
    }
    
    public final boolean component5() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.pripridelivery.viewmodel.CarrinhoUiState copy(@org.jetbrains.annotations.NotNull()
    java.util.List<com.pripridelivery.data.model.ItemCarrinho> itens, double total, boolean carregando, @org.jetbrains.annotations.Nullable()
    java.lang.String erro, boolean carrinhoAberto) {
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