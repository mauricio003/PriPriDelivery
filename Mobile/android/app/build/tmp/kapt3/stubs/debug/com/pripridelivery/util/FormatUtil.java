package com.pripridelivery.util;

import java.text.NumberFormat;
import java.util.Locale;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0006\n\u0002\b\u0004\n\u0002\u0010\b\n\u0000\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\tJ\u0010\u0010\n\u001a\u0004\u0018\u00010\u00072\u0006\u0010\u000b\u001a\u00020\u0007J\u000e\u0010\f\u001a\u00020\u00072\u0006\u0010\r\u001a\u00020\u000eR\u0016\u0010\u0003\u001a\n \u0005*\u0004\u0018\u00010\u00040\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u000f"}, d2 = {"Lcom/pripridelivery/util/FormatUtil;", "", "()V", "currencyFormatter", "Ljava/text/NumberFormat;", "kotlin.jvm.PlatformType", "formatarMoeda", "", "valor", "", "formatarTelefone", "telefone", "formatarTempo", "segundos", "", "app_debug"})
public final class FormatUtil {
    private static final java.text.NumberFormat currencyFormatter = null;
    @org.jetbrains.annotations.NotNull()
    public static final com.pripridelivery.util.FormatUtil INSTANCE = null;
    
    private FormatUtil() {
        super();
    }
    
    /**
     * Formata um valor numérico como moeda brasileira (R$).
     */
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String formatarMoeda(double valor) {
        return null;
    }
    
    /**
     * Formata número de telefone para o formato brasileiro com código do país.
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String formatarTelefone(@org.jetbrains.annotations.NotNull()
    java.lang.String telefone) {
        return null;
    }
    
    /**
     * Formata tempo em segundos para formato m:ss.
     */
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String formatarTempo(int segundos) {
        return null;
    }
}