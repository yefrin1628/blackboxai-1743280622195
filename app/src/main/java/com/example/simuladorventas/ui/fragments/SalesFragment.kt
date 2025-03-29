package com.example.simuladorventas.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.simuladorventas.databinding.FragmentSalesBinding
import com.example.simuladorventas.ui.adapters.SalesAdapter
import com.example.simuladorventas.viewmodel.SalesViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * Fragmento para gestión de ventas:
 * - Formulario para registrar nuevas ventas
 * - Listado de ventas recientes
 * - Visualización de totales y proyecciones
 */
@AndroidEntryPoint
class SalesFragment : Fragment() {
    private var _binding: FragmentSalesBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SalesViewModel by viewModels()
    private lateinit var salesAdapter: SalesAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSalesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupListeners()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        salesAdapter = SalesAdapter { sale ->
            // Manejar clic en venta
        }
        
        binding.rvSales.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = salesAdapter
            setHasFixedSize(true)
        }
    }

    private fun setupListeners() {
        binding.btnAddSale.setOnClickListener {
            addNewSale()
        }
        
        binding.btnCalculateProjection.setOnClickListener {
            calculateProjection()
        }
    }

    private fun observeViewModel() {
        viewModel.sales.observe(viewLifecycleOwner) { sales ->
            salesAdapter.submitList(sales)
        }
        
        viewModel.currentMonthTotal.observe(viewLifecycleOwner) { total ->
            binding.tvTotal.text = getString(R.string.label_total, total)
        }
    }

    private fun addNewSale() {
        val productName = binding.etProductName.text.toString()
        val quantity = binding.etQuantity.text.toString().toIntOrNull()
        val unitPrice = binding.etUnitPrice.text.toString().toDoubleOrNull()
        val discount = binding.etDiscount.text.toString().toDoubleOrNull() ?: 0.0

        if (validateInput(productName, quantity, unitPrice)) {
            viewModel.addSale(
                Sale(
                    productName = productName,
                    quantity = quantity!!,
                    unitPrice = unitPrice!!,
                    discount = discount
                )
            )
            clearForm()
        }
    }

    private fun calculateProjection() {
        val percentage = binding.etProjectionPercentage.text.toString().toDoubleOrNull() ?: 0.0
        val projection = viewModel.calculateProjection(percentage)
        binding.tvProjection.text = getString(R.string.label_projection, projection)
    }

    private fun validateInput(
        productName: String,
        quantity: Int?,
        unitPrice: Double?
    ): Boolean {
        // Implementar validaciones detalladas
        return !productName.isBlank() && quantity != null && unitPrice != null
    }

    private fun clearForm() {
        binding.etProductName.text?.clear()
        binding.etQuantity.text?.clear()
        binding.etUnitPrice.text?.clear()
        binding.etDiscount.text?.clear()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}