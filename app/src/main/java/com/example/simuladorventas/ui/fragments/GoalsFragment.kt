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
import com.example.simuladorventas.ui.adapters.GoalsAdapter
import com.example.simuladorventas.utils.Formatters
import com.example.simuladorventas.viewmodel.GoalsViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * Fragmento para gestión de objetivos de crédito:
 * - Listado de objetivos activos/completados
 * - Visualización de progreso general
 * - Navegación a detalle/edición de objetivos
 */
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
        setupRecyclerView()
        setupListeners()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        goalsAdapter = GoalsAdapter(
            onItemClick = { goal ->
                navigateToGoalDetail(goal.id)
            },
            onAddAmountClick = { goal ->
                showAddAmountDialog(goal)
            }
        )
        
        binding.rvGoals.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = goalsAdapter
            setHasFixedSize(true)
        }
    }

    private fun setupListeners() {
        binding.fabAddGoal.setOnClickListener {
            navigateToNewGoal()
        }

        binding.swipeRefresh.setOnRefreshListener {
            viewModel.getActiveGoals()
            binding.swipeRefresh.isRefreshing = false
        }
    }

    private fun observeViewModel() {
        viewModel.goals.observe(viewLifecycleOwner) { goals ->
            goalsAdapter.submitList(goals)
        }

        viewModel.progress.observe(viewLifecycleOwner) { (current, target) ->
            binding.progressBar.max = target.toInt()
            binding.progressBar.progress = current.toInt()
            binding.tvProgress.text = Formatters.formatPercentage(current / target)
        }
    }

    private fun navigateToGoalDetail(goalId: Long) {
        val action = GoalsFragmentDirections.actionGoalsFragmentToGoalDetailFragment(goalId)
        findNavController().navigate(action)
    }

    private fun navigateToNewGoal() {
        val action = GoalsFragmentDirections.actionGoalsFragmentToNewGoalFragment()
        findNavController().navigate(action)
    }

    private fun showAddAmountDialog(goal: CreditGoal) {
        // Implementar diálogo para agregar monto al objetivo
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}