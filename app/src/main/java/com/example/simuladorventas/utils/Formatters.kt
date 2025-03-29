package com.example.simuladorventas.utils

import android.content.Context
import com.example.simuladorventas.R
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

/**
 * Clase utilitaria para formatear diferentes tipos de datos:
 * - Moneda
 * - Fechas
 * - Porcentajes
 * - Números decimales
 */
object Formatters {
    private val currencyFormat: NumberFormat by lazy {
        NumberFormat.getCurrencyInstance().apply {
            maximumFractionDigits = 2
            minimumFractionDigits = 2
        }
    }
    
    private val percentageFormat: NumberFormat by lazy {
        NumberFormat.getPercentInstance().apply {
            maximumFractionDigits = 1
            minimumFractionDigits = 1
        }
    }
    
    private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    private val dateTimeFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())

    /**
     * Formatea un valor Double a moneda local
     * @param amount Cantidad a formatear
     * @return String formateada (ej: $1,000.50)
     */
    fun formatCurrency(amount: Double): String {
        return currencyFormat.format(amount)
    }

    /**
     * Formatea una fecha a formato corto
     * @param date Fecha a formatear
     * @return String con fecha formateada (ej: 15/03/2023)
     */
    fun formatDate(date: Date): String {
        return dateFormat.format(date)
    }

    /**
     * Formatea una fecha con hora
     * @param date Fecha a formatear
     * @return String con fecha y hora (ej: 15/03/2023 14:30)
     */
    fun formatDateTime(date: Date): String {
        return dateTimeFormat.format(date)
    }

    /**
     * Formatea un valor decimal a porcentaje
     * @param value Valor entre 0 y 1
     * @return String con porcentaje (ej: 10.5%)
     */
    fun formatPercentage(value: Double): String {
        return percentageFormat.format(value)
    }

    /**
     * Formatea moneda usando los recursos de la aplicación
     * @param context Contexto para acceder a recursos
     * @param amount Cantidad a formatear
     * @return String con formato de moneda desde strings.xml
     */
    fun getFormattedCurrency(context: Context, amount: Double): String {
        return context.getString(R.string.format_currency, amount)
    }

    /**
     * Formatea un número a cantidad decimal
     * @param value Valor a formatear
     * @param decimalDigits Número de decimales
     * @return String formateada
     */
    fun formatDecimal(value: Double, decimalDigits: Int = 2): String {
        return "%.${decimalDigits}f".format(Locale.getDefault(), value)
    }
}