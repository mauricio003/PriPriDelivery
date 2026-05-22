package com.pripridelivery.ui.screens;

import androidx.compose.foundation.layout.*;
import androidx.compose.material.icons.Icons;
import androidx.compose.material.icons.filled.*;
import androidx.compose.material3.*;
import androidx.compose.runtime.*;
import androidx.compose.ui.Alignment;
import androidx.compose.ui.Modifier;
import androidx.compose.ui.text.font.FontWeight;
import com.pripridelivery.data.model.Pedido;
import com.pripridelivery.util.FormatUtil;
import com.pripridelivery.viewmodel.AuthViewModel;
import com.pripridelivery.viewmodel.PedidoViewModel;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u0000.\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\u001a<\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00010\u00052\u0012\u0010\u0006\u001a\u000e\u0012\u0004\u0012\u00020\b\u0012\u0004\u0012\u00020\u00010\u00072\b\b\u0002\u0010\t\u001a\u00020\nH\u0007\u001a\u001e\u0010\u000b\u001a\u00020\u00012\u0006\u0010\f\u001a\u00020\r2\f\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u00010\u0005H\u0007\u001a\u0010\u0010\u000f\u001a\u00020\b2\u0006\u0010\u0010\u001a\u00020\bH\u0002\u00a8\u0006\u0011"}, d2 = {"MeusPedidosScreen", "", "authViewModel", "Lcom/pripridelivery/viewmodel/AuthViewModel;", "onVoltarClick", "Lkotlin/Function0;", "onPedidoClick", "Lkotlin/Function1;", "", "pedidoViewModel", "Lcom/pripridelivery/viewmodel/PedidoViewModel;", "PedidoCard", "pedido", "Lcom/pripridelivery/data/model/Pedido;", "onClick", "formatarData", "dataStr", "app_debug"})
public final class MeusPedidosScreenKt {
    
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    @androidx.compose.runtime.Composable()
    public static final void MeusPedidosScreen(@org.jetbrains.annotations.NotNull()
    com.pripridelivery.viewmodel.AuthViewModel authViewModel, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onVoltarClick, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onPedidoClick, @org.jetbrains.annotations.NotNull()
    com.pripridelivery.viewmodel.PedidoViewModel pedidoViewModel) {
    }
    
    @androidx.compose.runtime.Composable()
    public static final void PedidoCard(@org.jetbrains.annotations.NotNull()
    com.pripridelivery.data.model.Pedido pedido, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onClick) {
    }
    
    private static final java.lang.String formatarData(java.lang.String dataStr) {
        return null;
    }
}