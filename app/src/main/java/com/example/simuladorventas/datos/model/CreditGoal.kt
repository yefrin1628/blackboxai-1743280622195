package com.example.simuladorventas.datos.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

/**
 * Modelo de datos que representa un objetivo de crédito.
 * 
 * @property id Identificador único (autogenerado)
 * @property description Descripción del objetivo
 * @property targetAmount Monto meta a alcanzar
 * @property currentAmount Monto actual acumulado
 * @property startDate Fecha de inicio
 * @property endDate Fecha límite
 * @property isAchieved Indicador si se alcanzó el objetivo
 * 
 * @method calculateProgress() Calcula el progreso actual (0-1)
 * @method daysRemaining() Calcula días restantes para el objetivo
 */
@Entity(tableName = "credit_goals")
data class CreditGoal(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val description: String,
    val targetAmount: Double,
    val currentAmount: Double = 0.0,
    val startDate: Date = Date(),
    val endDate: Date,
    val isAchieved: Boolean = false
) {
    /**
     * Calcula el progreso hacia el objetivo como porcentaje (0 a 1)
     */
    fun calculateProgress(): Double {
        require(targetAmount > 0) { "El monto objetivo debe ser positivo" }
        return (currentAmount / targetAmount).coerceIn(0.0..1.0)
    }

    /**
     * Calcula días restantes para alcanzar el objetivo
     */
    fun daysRemaining(): Long {
        val now = Date().time
        val remaining = endDate.time - now
        return if (remaining > 0) remaining / (1000 * 60 * 60 * 24) else 0
    }
}