package com.example.simuladorventas.datos.repository

import com.example.simuladorventas.datos.dao.SaleDao
import com.example.simuladorventas.datos.model.Sale
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SalesRepositoryImpl @Inject constructor(
    private val saleDao: SaleDao
) : SalesRepository {

    override suspend fun getSales(): List<Sale> = 
        withContext(Dispatchers.IO) {
            saleDao.getAll()
        }

    override suspend fun addSale(sale: Sale) = 
        withContext(Dispatchers.IO) {
            saleDao.insert(sale)
        }

    override suspend fun getMonthlyTotal(): Double =
        withContext(Dispatchers.IO) {
            val sales = saleDao.getCurrentMonthSales()
            sales.sumOf { it.quantity * it.unitPrice }
        }

    override fun getSalesStream(): Flow<List<Sale>> =
        saleDao.getSalesStream()
}