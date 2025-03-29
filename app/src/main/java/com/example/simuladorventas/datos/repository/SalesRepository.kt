package com.example.simuladorventas.datos.repository

import com.example.simuladorventas.datos.dao.SaleDao
import com.example.simuladorventas.datos.model.Sale
import kotlinx.coroutines.flow.Flow
import java.util.*
import javax.inject.Inject

/**
 * Repositorio para operaciones relacionadas con ventas.
 * 
 * Proporciona una capa de abstracción sobre el SaleDao
 * y maneja la lógica de negocio relacionada con ventas.
 */
class SalesRepository @Inject constructor(
    private val saleDao: SaleDao
) {
    suspend fun addSale(sale: Sale) = saleDao.insertSale(sale)
    
    suspend fun updateSale(sale: Sale) = saleDao.updateSale(sale)
    
    suspend fun deleteSale(sale: Sale) = saleDao.deleteSale(sale)
    
    fun getAllSales(): Flow<List<Sale>> = saleDao.getAllSales()
    
    fun getSalesByDateRange(startDate: Date, endDate: Date): Flow<List<Sale>> = 
        saleDao.getSalesByDateRange(startDate, endDate)
    
    suspend fun getTotalSalesBetweenDates(start: Date, end: Date): Double = 
        saleDao.getTotalSalesBetweenDates(start, end)
    
    suspend fun getSaleById(id: Long): Sale? = saleDao.getSaleById(id)
    
    /**
     * Calcula el total de ventas para el mes actual
     */
    suspend fun getCurrentMonthSales(): Double {
        val calendar = Calendar.getInstance()
        val start = calendar.apply { 
            set(Calendar.DAY_OF_MONTH, 1)
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
        }.time
        
        val end = calendar.apply {
            set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH))
            set(Calendar.HOUR_OF_DAY, 23)
            set(Calendar.MINUTE, 59)
            set(Calendar.SECOND, 59)
        }.time
        
        return saleDao.getTotalSalesBetweenDates(start, end)
    }
}