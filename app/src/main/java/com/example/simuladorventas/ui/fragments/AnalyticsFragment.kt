package com.example.simuladorventas.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.simuladorventas.databinding.FragmentAnalyticsBinding
import com.example.simuladorventas.viewmodel.SalesViewModel
import com.example.simuladorventas.viewmodel.GoalsViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * Fragmento para mostrar análisis y estadísticas:
 * - Gráficos de ventas por período
 * - Comparación con objetivos
 * - Tendencias y proyecciones
 */
@AndroidEntryPoint
class AnalyticsFragment : Fragment() {
    private var _binding: FragmentAnalyticsBinding? = null
    private val binding get() = _binding!!
    private val salesViewModel: SalesViewModel by viewModels()
    private val goalsViewModel: GoalsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAnalyticsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObservers()
        setupChart()
    }

    private fun setupObservers() {
        salesViewModel.sales.observe(viewLifecycleOwner) { sales ->
            // Actualizar gráfico de ventas
        }

        goalsViewModel.progress.observe(viewLifecycleOwner) { (current, target) ->
            // Actualizar comparación con objetivos
        }
    }

    private fun setupChart() {
        // Configurar gráfico con MPAndroidChart u otra librería
        with(binding.chartSales) {
            setTouchEnabled(true)
            setDrawGridBackground(false)
            description.isEnabled = false
            setScaleEnabled(true)
            setPinchZoom(true)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}