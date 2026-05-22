package com.pripridelivery.ui.screens;

import androidx.compose.foundation.layout.*;
import androidx.compose.material.icons.Icons;
import androidx.compose.material.icons.filled.*;
import androidx.compose.material3.*;
import androidx.compose.runtime.*;
import androidx.compose.ui.Alignment;
import androidx.compose.ui.Modifier;
import androidx.compose.ui.layout.ContentScale;
import androidx.compose.ui.text.font.FontWeight;
import com.pripridelivery.data.model.Produto;
import com.pripridelivery.util.FormatUtil;
import com.pripridelivery.viewmodel.*;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u0000>\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0004\u001aR\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\f\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\u00010\u00072\f\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00010\u00072\b\b\u0002\u0010\t\u001a\u00020\n2\b\b\u0002\u0010\u000b\u001a\u00020\f2\b\b\u0002\u0010\r\u001a\u00020\u000eH\u0007\u001aB\u0010\u000f\u001a\u00020\u00012\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u00132\f\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\u00010\u00072\f\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\u00010\u00072\f\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u00010\u0007H\u0007\u00a8\u0006\u0017"}, d2 = {"ComprarScreen", "", "restauranteId", "", "authViewModel", "Lcom/pripridelivery/viewmodel/AuthViewModel;", "onVoltarClick", "Lkotlin/Function0;", "onPagamentoClick", "produtoViewModel", "Lcom/pripridelivery/viewmodel/ProdutoViewModel;", "carrinhoViewModel", "Lcom/pripridelivery/viewmodel/CarrinhoViewModel;", "restauranteViewModel", "Lcom/pripridelivery/viewmodel/RestauranteViewModel;", "ProdutoCompraCard", "produto", "Lcom/pripridelivery/data/model/Produto;", "quantidade", "", "onMais", "onMenos", "onAdicionar", "app_debug"})
public final class ComprarScreenKt {
    
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    @androidx.compose.runtime.Composable()
    public static final void ComprarScreen(@org.jetbrains.annotations.NotNull()
    java.lang.String restauranteId, @org.jetbrains.annotations.NotNull()
    com.pripridelivery.viewmodel.AuthViewModel authViewModel, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onVoltarClick, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onPagamentoClick, @org.jetbrains.annotations.NotNull()
    com.pripridelivery.viewmodel.ProdutoViewModel produtoViewModel, @org.jetbrains.annotations.NotNull()
    com.pripridelivery.viewmodel.CarrinhoViewModel carrinhoViewModel, @org.jetbrains.annotations.NotNull()
    com.pripridelivery.viewmodel.RestauranteViewModel restauranteViewModel) {
    }
    
    @androidx.compose.runtime.Composable()
    public static final void ProdutoCompraCard(@org.jetbrains.annotations.NotNull()
    com.pripridelivery.data.model.Produto produto, int quantidade, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onMais, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onMenos, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onAdicionar) {
    }
}