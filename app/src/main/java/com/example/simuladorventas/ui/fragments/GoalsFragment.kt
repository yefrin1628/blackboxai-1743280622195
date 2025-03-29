package com.example.simuladorventas.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.simuladorventas.databinding.FragmentGoalsBinding
import com.example.simuladorventas.datos.model.CreditGoal
import com.example.simuladorventas.ui.adapters.GoalsAdapter
import com.example.simuladorventas.utils.Formatters
import com.example.simuladorventas.viewmodel.GoalsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GoalsFragment : Fragment() {
    private var _binding: FragmentGoalsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: GoalsViewModel by viewModels()
    private lateinit var goalsAdapter: GoalsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGoalsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        setupObservers()
    }

    private fun setupUI() {
        goalsAdapter = GoalsAdapter(
            onItemClick = { navigateToGoalDetail(it.id) },
            onAddAmountClick = { showAddAmountDialog(it) }
        )

        binding.rvGoals.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = goalsAdapter
            setHasFixedSize(true)
        }

        binding.fabAddGoal.setOnClickListener { navigateToNewGoal() }
        
        // Verifica si el SwipeRefreshLayout existe antes de configurarlo
        binding.swipeRefreshLayout?.setOnRefreshListener {
            viewModel.loadGoals()
            binding.swipeRefreshLayout?.isRefreshing = false
        }
    }

    private fun setupObservers() {
        viewModel.goalsLiveData.observe(viewLifecycleOwner) { goals ->
            goalsAdapter.submitList(goals)
        }

        viewModel.progressLiveData.observe(viewLifecycleOwner) { progress ->
            progress?.let { (current, target) ->
                binding.progressBar.apply {
                    max = target.toInt()
                    progress = current.toInt()
                }
                binding.tvProgress.text = Formatters.formatPercentage(current / target)
            }
        }
    }

    private fun navigateToGoalDetail(goalId: Long) {
        // Verifica que la acción de navegación exista
        try {
            val action = GoalsFragmentDirections.actionGoalsToGoalDetail(goalId)
            findNavController().navigate(action)
        } catch (e: IllegalArgumentException) {
            // Maneja el error si la acción no existe
            e.printStackTrace()
        }
    }

    private fun navigateToNewGoal() {
        try {
            val action = GoalsFragmentDirections.actionGoalsToNewGoal()
            findNavController().navigate(action)
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        }
    }

    private fun showAddAmountDialog(goal: CreditGoal) {
        // Implementación simplificada sin AddAmountDialog
        // Puedes implementar un diálogo básico con AlertDialog
        /*
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Agregar monto")
            .setMessage("Ingrese el monto a agregar para ${goal.description}")
            .setView(R.layout.dialog_add_amount) // Crea este layout
            .setPositiveButton("Agregar") { _, _ -> 
                // Obtener el monto ingresado y llamar a:
                viewModel.addAmount(goal.id, amount)
            }
            .setNegativeButton("Cancelar", null)
            .show()
        */
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}