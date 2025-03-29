package com.example.simuladorventas.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.simuladorventas.R
import com.example.simuladorventas.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * Actividad principal que contiene la navegación entre fragments.
 * 
 * Configura:
 * - BottomNavigationView
 * - NavController
 * - Toolbar
 * 
 * Usa Navigation Component para manejar los fragments:
 * 1. SalesFragment - Entrada de ventas
 * 2. GoalsFragment - Gestión de objetivos
 * 3. AnalyticsFragment - Visualización de resultados
 */
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupNavigation()
    }

    private fun setupNavigation() {
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        // Configura bottom navigation
        binding.bottomNavView.setupWithNavController(navController)

        // Configura toolbar
        setSupportActionBar(binding.toolbar)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            binding.toolbar.title = when(destination.id) {
                R.id.salesFragment -> getString(R.string.title_sales)
                R.id.goalsFragment -> getString(R.string.title_goals)
                R.id.analyticsFragment -> getString(R.string.title_analytics)
                else -> getString(R.string.app_name)
            }
        }
    }
}