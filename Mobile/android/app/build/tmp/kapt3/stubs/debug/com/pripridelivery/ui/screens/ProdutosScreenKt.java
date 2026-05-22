package com.pripridelivery.ui.screens;

import androidx.compose.foundation.layout.*;
import androidx.compose.material.icons.Icons;
import androidx.compose.material.icons.filled.*;
import androidx.compose.material3.*;
import androidx.compose.runtime.*;
import androidx.compose.ui.Alignment;
import androidx.compose.ui.Modifier;
import androidx.compose.ui.text.font.FontWeight;
import com.pripridelivery.data.model.Produto;
import com.pripridelivery.util.FormatUtil;
import com.pripridelivery.viewmodel.AuthViewModel;
import com.pripridelivery.viewmodel.ProdutoViewModel;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u00006\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\u001a<\u0010\u0000\u001a\u00020\u00012\b\u0010\u0002\u001a\u0004\u0018\u00010\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0012\u0010\u0006\u001a\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u00010\u00072\f\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00010\tH\u0007\u001a,\u0010\n\u001a\u00020\u00012\u0006\u0010\u000b\u001a\u00020\u00032\f\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u00010\t2\f\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u00010\tH\u0007\u001a0\u0010\u000e\u001a\u00020\u00012\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u00122\f\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u00010\t2\b\b\u0002\u0010\u0014\u001a\u00020\u0015H\u0007\u00a8\u0006\u0016"}, d2 = {"ProdutoFormDialog", "", "produtoInicial", "Lcom/pripridelivery/data/model/Produto;", "salvando", "", "onSalvar", "Lkotlin/Function1;", "onDismiss", "Lkotlin/Function0;", "ProdutoGerenciarCard", "produto", "onEditar", "onExcluir", "ProdutosScreen", "restauranteId", "", "authViewModel", "Lcom/pripridelivery/viewmodel/AuthViewModel;", "onVoltarClick", "produtoViewModel", "Lcom/pripridelivery/viewmodel/ProdutoViewModel;", "app_debug"})
public final class ProdutosScreenKt {
    
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    @androidx.compose.runtime.Composable()
    public static final void ProdutosScreen(@org.jetbrains.annotations.NotNull()
    java.lang.String restauranteId, @org.jetbrains.annotations.NotNull()
    com.pripridelivery.viewmodel.AuthViewModel authViewModel, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onVoltarClick, @org.jetbrains.annotations.NotNull()
    com.pripridelivery.viewmodel.ProdutoViewModel produtoViewModel) {
    }
    
    @androidx.compose.runtime.Composable()
    public static final void ProdutoGerenciarCard(@org.jetbrains.annotations.NotNull()
    com.pripridelivery.data.model.Produto produto, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onEditar, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onExcluir) {
    }
    
    @androidx.compose.runtime.Composable()
    public static final void ProdutoFormDialog(@org.jetbrains.annotations.Nullable()
    com.pripridelivery.data.model.Produto produtoInicial, boolean salvando, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super com.pripridelivery.data.model.Produto, kotlin.Unit> onSalvar, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onDismiss) {
    }
}