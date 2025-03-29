package com.example.simuladorventas

import android.app.Application
import com.example.simuladorventas.di.AppModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

/**
 * Clase principal de la aplicación que maneja la configuración inicial.
 * Configura Koin para la inyección de dependencias y realiza otras inicializaciones.
 * 
 * @property applicationContext Contexto global de la aplicación
 * @method onCreate() Se ejecuta al iniciar la aplicación, configura Koin DI
 */
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        
        // Configuración de Koin para Dependency Injection
        startKoin {
            androidContext(this@App)
            modules(AppModule.module)
        }
    }
}