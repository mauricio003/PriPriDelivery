package com.pripridelivery.ui.screens;

import androidx.compose.foundation.layout.*;
import androidx.compose.foundation.text.KeyboardOptions;
import androidx.compose.material.icons.Icons;
import androidx.compose.material.icons.filled.*;
import androidx.compose.material3.*;
import androidx.compose.runtime.*;
import androidx.compose.ui.Alignment;
import androidx.compose.ui.Modifier;
import androidx.compose.ui.text.font.FontWeight;
import androidx.compose.ui.text.input.KeyboardType;
import com.pripridelivery.data.model.Endereco;
import com.pripridelivery.viewmodel.AuthViewModel;
import com.pripridelivery.viewmodel.EnderecoViewModel;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u00002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\u001a,\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00010\u00052\f\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\u00010\u0005H\u0007\u001a<\u0010\u0007\u001a\u00020\u00012\b\u0010\b\u001a\u0004\u0018\u00010\u00032\u0006\u0010\t\u001a\u00020\n2\u0012\u0010\u000b\u001a\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u00010\f2\f\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u00010\u0005H\u0007\u001a(\u0010\u000e\u001a\u00020\u00012\u0006\u0010\u000f\u001a\u00020\u00102\f\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00010\u00052\b\b\u0002\u0010\u0012\u001a\u00020\u0013H\u0007\u00a8\u0006\u0014"}, d2 = {"EnderecoCard", "", "endereco", "Lcom/pripridelivery/data/model/Endereco;", "onEditar", "Lkotlin/Function0;", "onExcluir", "EnderecoFormDialog", "enderecoInicial", "salvando", "", "onSalvar", "Lkotlin/Function1;", "onDismiss", "EnderecoScreen", "authViewModel", "Lcom/pripridelivery/viewmodel/AuthViewModel;", "onVoltarClick", "enderecoViewModel", "Lcom/pripridelivery/viewmodel/EnderecoViewModel;", "app_debug"})
public final class EnderecoScreenKt {
    
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    @androidx.compose.runtime.Composable()
    public static final void EnderecoScreen(@org.jetbrains.annotations.NotNull()
    com.pripridelivery.viewmodel.AuthViewModel authViewModel, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onVoltarClick, @org.jetbrains.annotations.NotNull()
    com.pripridelivery.viewmodel.EnderecoViewModel enderecoViewModel) {
    }
    
    @androidx.compose.runtime.Composable()
    public static final void EnderecoCard(@org.jetbrains.annotations.NotNull()
    com.pripridelivery.data.model.Endereco endereco, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onEditar, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onExcluir) {
    }
    
    @androidx.compose.runtime.Composable()
    public static final void EnderecoFormDialog(@org.jetbrains.annotations.Nullable()
    com.pripridelivery.data.model.Endereco enderecoInicial, boolean salvando, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super com.pripridelivery.data.model.Endereco, kotlin.Unit> onSalvar, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onDismiss) {
    }
}