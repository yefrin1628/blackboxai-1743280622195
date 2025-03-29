package com.example.simuladorventas.datos.repository

import com.example.simuladorventas.datos.model.Sale
import kotlinx.coroutines.flow.Flow

interface SalesRepository {
    suspend fun getSales(): List<Sale>
    suspend fun addSale(sale: Sale)
    suspend fun getMonthlyTotal(): Double
    fun getSalesStream(): Flow<List<Sale>>
}