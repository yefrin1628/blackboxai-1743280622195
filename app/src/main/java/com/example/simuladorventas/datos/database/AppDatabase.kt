package com.example.simuladorventas.datos.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.simuladorventas.datos.model.Sale
import com.example.simuladorventas.datos.model.CreditGoal
import com.example.simuladorventas.datos.dao.SaleDao
import com.example.simuladorventas.datos.dao.GoalDao

/**
 * Clase abstracta que representa la base de datos Room para la aplicación.
 * 
 * Configuración:
 * - Entidades: Sale y CreditGoal
 * - Versión: 1
 * - No exporta esquema (exportSchema = false)
 * 
 * Proporciona acceso a:
 * - saleDao(): DAO para operaciones con ventas
 * - goalDao(): DAO para operaciones con objetivos
 * 
 * Implementa patrón Singleton para asegurar una única instancia de la base de datos.
 */
@Database(
    entities = [Sale::class, CreditGoal::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun saleDao(): SaleDao
    abstract fun goalDao(): GoalDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        /**
         * Obtiene la instancia de la base de datos.
         * @param context Contexto de la aplicación
         * @return Instancia única de AppDatabase
         */
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "sales_database"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}