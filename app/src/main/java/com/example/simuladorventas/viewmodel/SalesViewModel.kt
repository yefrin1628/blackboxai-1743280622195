package com.example.simuladorventas.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simuladorventas.datos.model.Sale
import com.example.simuladorventas.datos.repository.SalesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

/**
 * ViewModel para manejar la lógica relacionada con ventas.
 * 
 * Expone:
 * - Estado actual de las ventas
 * - Métodos para operaciones CRUD
 * - Cálculos de proyecciones
 * 
 * Maneja:
 * - Carga asíncrona de datos
 * - Validación de entradas
 * - Actualización de UI
 */
@HiltViewModel
class SalesViewModel @Inject constructor(
    private val repository: SalesRepository
) : ViewModel() {
    private val _sales = MutableStateFlow<List<Sale>>(emptyList())
    val sales: StateFlow<List<Sale>> = _sales.asStateFlow()

    private val _currentMonthTotal = MutableStateFlow(0.0)
    val currentMonthTotal: StateFlow<Double> = _currentMonthTotal.asStateFlow()

    init {
        loadAllSales()
        loadCurrentMonthTotal()
    }

    private fun loadAllSales() {
        viewModelScope.launch {
            repository.getAllSales().collect { salesList ->
                _sales.value = salesList
            }
        }
    }

    private fun loadCurrentMonthTotal() {
        viewModelScope.launch {
            _currentMonthTotal.value = repository.getCurrentMonthSales()
        }
    }

    fun addSale(sale: Sale) {
        viewModelScope.launch {
            repository.addSale(sale)
        }
    }

    fun updateSale(sale: Sale) {
        viewModelScope.launch {
            repository.updateSale(sale)
        }
    }

    fun deleteSale(sale: Sale) {
        viewModelScope.launch {
            repository.deleteSale(sale)
        }
    }

    fun getSalesBetweenDates(start: Date, end: Date) {
        viewModelScope.launch {
            repository.getSalesByDateRange(start, end).collect { salesList ->
                _sales.value = salesList
            }
        }
    }

    fun calculateProjection(profitPercentage: Double): Double {
        return _currentMonthTotal.value * (1 + profitPercentage/100)
    }
}