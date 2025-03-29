package com.example.simuladorventas.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.simuladorventas.R
import com.example.simuladorventas.databinding.FragmentSalesBinding
import com.example.simuladorventas.datos.model.Sale
import com.example.simuladorventas.ui.adapters.SalesAdapter
import com.example.simuladorventas.viewmodel.SalesViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SalesFragment : Fragment() {

    // Binding
    private var _binding: FragmentSalesBinding? = null
    private val binding get() = _binding!!

    // ViewModel
    private val viewModel: SalesViewModel by viewModels()

    // Adapter
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
        initUI()
        setupObservers()
    }

    private fun initUI() {
        // Configurar RecyclerView
        salesAdapter = SalesAdapter { sale ->
            // Acción al hacer clic en una venta
        }

        binding.rvSales.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = salesAdapter
            setHasFixedSize(true)
        }

        // Configurar listeners
        binding.btnAddSale.setOnClickListener { addNewSale() }
        binding.btnCalculateProjection.setOnClickListener { calculateProjection() }
    }

    private fun setupObservers() {
        // Observar lista de ventas
        viewModel.salesList.observe(viewLifecycleOwner, Observer { sales ->
            sales?.let { salesAdapter.submitList(it) }
        })

        // Observar total mensual
        viewModel.monthlyTotal.observe(viewLifecycleOwner, Observer { total ->
            total?.let {
                binding.tvTotal.text = getString(R.string.label_total, it)
            }
        })
    }

    private fun addNewSale() {
        val product = binding.etProductName.text.toString()
        val quantity = binding.etQuantity.text.toString().toIntOrNull()
        val price = binding.etUnitPrice.text.toString().toDoubleOrNull()
        val discount = binding.etDiscount.text.toString().toDoubleOrNull() ?: 0.0

        if (isValidSale(product, quantity, price)) {
            viewModel.registerSale(
                Sale(
                    productName = product,
                    quantity = quantity!!,
                    unitPrice = price!!,
                    discount = discount
                )
            )
            clearForm()
        }
    }

    private fun calculateProjection() {
        val percentage = binding.etProjectionPercentage.text.toString().toDoubleOrNull() ?: 0.0
        viewModel.calculateProjection(percentage).observe(viewLifecycleOwner, Observer { projection ->
            binding.tvProjection.text = getString(R.string.label_projection, projection)
        })
    }

    private fun isValidSale(product: String, quantity: Int?, price: Double?): Boolean {
        var valid = true

        if (product.isBlank()) {
            binding.etProductName.error = "Producto requerido"
            valid = false
        }

        if (quantity == null || quantity <= 0) {
            binding.etQuantity.error = "Cantidad inválida"
            valid = false
        }

        if (price == null || price <= 0) {
            binding.etUnitPrice.error = "Precio inválido"
            valid = false
        }

        return valid
    }

    private fun clearForm() {
        with(binding) {
            etProductName.text?.clear()
            etQuantity.text?.clear()
            etUnitPrice.text?.clear()
            etDiscount.text?.clear()
            etProductName.error = null
            etQuantity.error = null
            etUnitPrice.error = null
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}