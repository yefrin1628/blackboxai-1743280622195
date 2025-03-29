package com.example.simuladorventas.di

import com.example.simuladorventas.datos.database.AppDatabase
import com.example.simuladorventas.datos.repository.SalesRepository
import com.example.simuladorventas.datos.repository.GoalsRepository
import org.koin.dsl.module

/**
 * Módulo principal de inyección de dependencias para la aplicación.
 * Proporciona instancias únicas de:
 * - Base de datos Room
 * - Repositorios
 * - ViewModels
 */
val module = module {
    // Database
    single { AppDatabase.getDatabase(get()) }
    
    // DAOs
    single { get<AppDatabase>().saleDao() }
    single { get<AppDatabase>().goalDao() }
    
    // Repositories
    single { SalesRepository(get()) }
    single { GoalsRepository(get()) }
    
    // ViewModels
    viewModel { SalesViewModel(get()) }
    viewModel { GoalsViewModel(get()) }
}