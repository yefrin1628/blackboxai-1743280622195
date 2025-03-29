package com.example.simuladorventas

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Clase principal de la aplicación que maneja la configuración inicial.
 * HiltAndroidApp habilita la inyección de dependencias con Hilt.
 */
@HiltAndroidApp
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        // Configuración adicional si es necesaria
    }
}