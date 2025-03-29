package com.example.simuladorventas.datos.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

/**
 * Modelo de datos que representa una venta individual.
 * @property id Identificador único generado automáticamente
 * @property productName Nombre del producto vendido
 * @property quantity Cantidad de unidades vendidas
 * @property unitPrice Precio por unidad del producto
 * @property discount Descuento aplicado (porcentaje entre 0 y 1)
 * @property date Fecha en que se registró la venta
 */
@Entity(tableName = "sales")
data class Sale(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val productName: String,
    val quantity: Int,
    val unitPrice: Double,
    val discount: Double = 0.0,
    val date: Date = Date()
) {
    /**
     * Calcula el subtotal de la venta aplicando el descuento.
     * @return Subtotal como Double
     */
    fun calculateSubtotal(): Double {
        require(discount in 0.0..1.0) { "El descuento debe estar entre 0 y 1" }
        val totalBeforeDiscount = quantity * unitPrice
        return totalBeforeDiscount - (totalBeforeDiscount * discount)
    }
}