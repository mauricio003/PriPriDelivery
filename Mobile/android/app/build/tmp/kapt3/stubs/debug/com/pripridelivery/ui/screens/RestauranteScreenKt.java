package com.pripridelivery.ui.screens;

import androidx.compose.foundation.layout.*;
import androidx.compose.material.icons.Icons;
import androidx.compose.material.icons.filled.*;
import androidx.compose.material3.*;
import androidx.compose.runtime.*;
import androidx.compose.ui.Alignment;
import androidx.compose.ui.Modifier;
import androidx.compose.ui.text.font.FontWeight;
import com.pripridelivery.data.model.Restaurante;
import com.pripridelivery.viewmodel.AuthViewModel;
import com.pripridelivery.viewmodel.RestauranteViewModel;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u00006\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\u001a<\u0010\u0000\u001a\u00020\u00012\b\u0010\u0002\u001a\u0004\u0018\u00010\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0012\u0010\u0006\u001a\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u00010\u00072\f\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00010\tH\u0007\u001a:\u0010\n\u001a\u00020\u00012\u0006\u0010\u000b\u001a\u00020\u00032\f\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u00010\t2\f\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u00010\t2\f\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u00010\tH\u0007\u001a<\u0010\u000f\u001a\u00020\u00012\u0006\u0010\u0010\u001a\u00020\u00112\u0012\u0010\u0012\u001a\u000e\u0012\u0004\u0012\u00020\u0013\u0012\u0004\u0012\u00020\u00010\u00072\f\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\u00010\t2\b\b\u0002\u0010\u0015\u001a\u00020\u0016H\u0007\u00a8\u0006\u0017"}, d2 = {"RestauranteFormDialog", "", "restauranteInicial", "Lcom/pripridelivery/data/model/Restaurante;", "salvando", "", "onSalvar", "Lkotlin/Function1;", "onDismiss", "Lkotlin/Function0;", "RestauranteGerenciarCard", "restaurante", "onProdutos", "onEditar", "onExcluir", "RestauranteScreen", "authViewModel", "Lcom/pripridelivery/viewmodel/AuthViewModel;", "onProdutosClick", "", "onVoltarClick", "restauranteViewModel", "Lcom/pripridelivery/viewmodel/RestauranteViewModel;", "app_debug"})
public final class RestauranteScreenKt {
    
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    @androidx.compose.runtime.Composable()
    public static final void RestauranteScreen(@org.jetbrains.annotations.NotNull()
    com.pripridelivery.viewmodel.AuthViewModel authViewModel, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onProdutosClick, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onVoltarClick, @org.jetbrains.annotations.NotNull()
    com.pripridelivery.viewmodel.RestauranteViewModel restauranteViewModel) {
    }
    
    @androidx.compose.runtime.Composable()
    public static final void RestauranteGerenciarCard(@org.jetbrains.annotations.NotNull()
    com.pripridelivery.data.model.Restaurante restaurante, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onProdutos, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onEditar, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onExcluir) {
    }
    
    @androidx.compose.runtime.Composable()
    public static final void RestauranteFormDialog(@org.jetbrains.annotations.Nullable()
    com.pripridelivery.data.model.Restaurante restauranteInicial, boolean salvando, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super com.pripridelivery.data.model.Restaurante, kotlin.Unit> onSalvar, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onDismiss) {
    }
}