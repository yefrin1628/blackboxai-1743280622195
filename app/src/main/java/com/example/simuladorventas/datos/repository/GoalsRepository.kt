package com.example.simuladorventas.datos.repository

import com.example.simuladorventas.datos.dao.GoalDao
import com.example.simuladorventas.datos.model.CreditGoal
import kotlinx.coroutines.flow.Flow
import java.util.*
import javax.inject.Inject

/**
 * Repositorio para operaciones relacionadas con objetivos de crédito.
 * 
 * Proporciona una capa de abstracción sobre el GoalDao
 * y maneja la lógica de negocio relacionada con objetivos.
 */
class GoalsRepository @Inject constructor(
    private val goalDao: GoalDao
) {
    suspend fun addGoal(goal: CreditGoal) = goalDao.insertGoal(goal)
    
    suspend fun updateGoal(goal: CreditGoal) = goalDao.updateGoal(goal)
    
    suspend fun deleteGoal(goal: CreditGoal) = goalDao.deleteGoal(goal)
    
    fun getAllGoals(): Flow<List<CreditGoal>> = goalDao.getAllGoals()
    
    fun getActiveGoals(): Flow<List<CreditGoal>> = goalDao.getActiveGoals()
    
    suspend fun getGoalById(id: Long): CreditGoal? = goalDao.getGoalById(id)
    
    suspend fun addToGoal(goalId: Long, amount: Double) = 
        goalDao.addToGoal(goalId, amount)
    
    suspend fun markGoalAsAchieved(goalId: Long) = 
        goalDao.markGoalAsAchieved(goalId)
    
    /**
     * Calcula el progreso general de todos los objetivos activos
     */
    suspend fun getOverallProgress(): Pair<Double, Double> {
        val current = goalDao.getMonthlyCurrentTotal(Date())
        val target = goalDao.getMonthlyTargetTotal(Date())
        return Pair(current, target)
    }
    
    /**
     * Verifica y actualiza el estado de los objetivos vencidos
     */
    suspend fun checkExpiredGoals() {
        val now = Date()
        getActiveGoals().collect { goals ->
            goals.forEach { goal ->
                if (goal.endDate.before(now) && !goal.isAchieved) {
                    updateGoal(goal.copy(isAchieved = goal.currentAmount >= goal.targetAmount))
                }
            }
        }
    }
}