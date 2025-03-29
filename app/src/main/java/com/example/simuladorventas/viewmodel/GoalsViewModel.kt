package com.example.simuladorventas.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simuladorventas.datos.model.CreditGoal
import com.example.simuladorventas.datos.repository.GoalsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

/**
 * ViewModel para manejar la lógica relacionada con objetivos de crédito.
 * 
 * @property repository Repositorio de objetivos inyectado
 * @property _goals Estado mutable de la lista de objetivos
 * @property goals Flujo público de solo lectura para los objetivos
 * @property _progress Progreso actual vs objetivo total
 * @property progress Flujo público de solo lectura para el progreso
 */
@HiltViewModel
class GoalsViewModel @Inject constructor(
    private val repository: GoalsRepository
) : ViewModel() {
    private val _goals = MutableStateFlow<List<CreditGoal>>(emptyList())
    val goals: StateFlow<List<CreditGoal>> = _goals.asStateFlow()

    private val _progress = MutableStateFlow<Pair<Double, Double>>(0.0 to 0.0)
    val progress: StateFlow<Pair<Double, Double>> = _progress.asStateFlow()

    private val _selectedGoal = MutableStateFlow<CreditGoal?>(null)
    val selectedGoal: StateFlow<CreditGoal?> = _selectedGoal.asStateFlow()

    init {
        loadAllGoals()
        loadProgress()
        checkExpiredGoalsPeriodically()
    }

    private fun loadAllGoals() {
        viewModelScope.launch {
            repository.getAllGoals().collect { goalsList ->
                _goals.value = goalsList
            }
        }
    }

    private fun loadProgress() {
        viewModelScope.launch {
            _progress.value = repository.getOverallProgress()
        }
    }

    private fun checkExpiredGoalsPeriodically() {
        viewModelScope.launch {
            while(true) {
                repository.checkExpiredGoals()
                kotlinx.coroutines.delay(60_000) // Check cada minuto
            }
        }
    }

    fun addGoal(goal: CreditGoal) {
        viewModelScope.launch {
            repository.addGoal(goal)
        }
    }

    fun updateGoal(goal: CreditGoal) {
        viewModelScope.launch {
            repository.updateGoal(goal)
        }
    }

    fun deleteGoal(goal: CreditGoal) {
        viewModelScope.launch {
            repository.deleteGoal(goal)
        }
    }

    fun addToGoal(goalId: Long, amount: Double) {
        viewModelScope.launch {
            repository.addToGoal(goalId, amount)
            loadProgress()
        }
    }

    fun selectGoal(goal: CreditGoal) {
        _selectedGoal.value = goal
    }

    fun getActiveGoals() {
        viewModelScope.launch {
            repository.getActiveGoals().collect { goalsList ->
                _goals.value = goalsList
            }
        }
    }

    fun calculateGoalProgress(goal: CreditGoal): Float {
        return goal.calculateProgress().toFloat()
    }
}