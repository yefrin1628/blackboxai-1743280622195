package com.example.simuladorventas.datos.dao

import androidx.room.*
import com.example.simuladorventas.datos.model.CreditGoal
import kotlinx.coroutines.flow.Flow
import java.util.*

/**
 * DAO (Data Access Object) para operaciones con la tabla de objetivos de crédito.
 * 
 * Proporciona métodos para:
 * - CRUD básico de objetivos
 * - Consultas especializadas
 * - Actualizaciones parciales
 * 
 * Todos los métodos son suspend para ejecución en corrutinas.
 */
@Dao
interface GoalDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGoal(goal: CreditGoal)

    @Update
    suspend fun updateGoal(goal: CreditGoal)

    @Delete
    suspend fun deleteGoal(goal: CreditGoal)

    @Query("SELECT * FROM credit_goals ORDER BY endDate ASC")
    fun getAllGoals(): Flow<List<CreditGoal>>

    @Query("SELECT * FROM credit_goals WHERE isAchieved = 0 ORDER BY endDate ASC")
    fun getActiveGoals(): Flow<List<CreditGoal>>

    @Query("SELECT * FROM credit_goals WHERE id = :id")
    suspend fun getGoalById(id: Long): CreditGoal?

    @Query("UPDATE credit_goals SET currentAmount = currentAmount + :amount WHERE id = :goalId")
    suspend fun addToGoal(goalId: Long, amount: Double)

    @Query("UPDATE credit_goals SET isAchieved = 1 WHERE id = :goalId")
    suspend fun markGoalAsAchieved(goalId: Long)

    @Query("SELECT SUM(targetAmount) FROM credit_goals WHERE strftime('%Y-%m', endDate/1000, 'unixepoch') = strftime('%Y-%m', :date/1000, 'unixepoch')")
    suspend fun getMonthlyTargetTotal(date: Date): Double

    @Query("SELECT SUM(currentAmount) FROM credit_goals WHERE strftime('%Y-%m', endDate/1000, 'unixepoch') = strftime('%Y-%m', :date/1000, 'unixepoch')")
    suspend fun getMonthlyCurrentTotal(date: Date): Double
}