package com.pripridelivery.util

import java.text.NumberFormat
import java.util.Locale

object FormatUtil {

    private val currencyFormatter = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))

    /**
     * Formata um valor numérico como moeda brasileira (R$).
     */
    fun formatarMoeda(valor: Double): String {
        return currencyFormatter.format(valor)
    }

    /**
     * Formata número de telefone para o formato brasileiro com código do país.
     */
    fun formatarTelefone(telefone: String): String? {
        val nums = telefone.replace(Regex("\\D"), "")
        return when {
            nums.length == 10 || nums.length == 11 -> "+55$nums"
            nums.startsWith("55") && (nums.length == 12 || nums.length == 13) -> "+$nums"
            else -> null
        }
    }

    /**
     * Formata tempo em segundos para formato m:ss.
     */
    fun formatarTempo(segundos: Int): String {
        val m = segundos / 60
        val s = segundos % 60
        return "$m:${s.toString().padStart(2, '0')}"
    }
}
