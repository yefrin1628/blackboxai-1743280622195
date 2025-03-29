package com.example.simuladorventas.datos.dao

import androidx.room.*
import com.example.simuladorventas.datos.model.Sale
import kotlinx.coroutines.flow.Flow
import java.util.*

/**
 * DAO (Data Access Object) para operaciones con la tabla de ventas.
 * Proporciona métodos para:
 * - Insertar nuevas ventas
 * - Actualizar ventas existentes
 * - Eliminar ventas
 * - Consultar ventas con diferentes filtros
 */
@Dao
interface SaleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSale(sale: Sale)

    @Update
    suspend fun updateSale(sale: Sale)

    @Delete
    suspend fun deleteSale(sale: Sale)

    @Query("SELECT * FROM sales ORDER BY date DESC")
    fun getAllSales(): Flow<List<Sale>>

    @Query("SELECT * FROM sales WHERE date BETWEEN :startDate AND :endDate ORDER BY date DESC")
    fun getSalesByDateRange(startDate: Date, endDate: Date): Flow<List<Sale>>

    @Query("SELECT SUM(quantity * unitPrice * (1 - discount)) FROM sales WHERE date BETWEEN :start AND :end")
    suspend fun getTotalSalesBetweenDates(start: Date, end: Date): Double

    @Query("SELECT * FROM sales WHERE id = :id")
    suspend fun getSaleById(id: Long): Sale?
}