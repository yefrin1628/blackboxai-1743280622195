package com.example.simuladorventas.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.simuladorventas.databinding.ItemGoalBinding
import com.example.simuladorventas.datos.model.CreditGoal
import com.example.simuladorventas.utils.Formatters

class GoalsAdapter(
    private val onItemClick: (CreditGoal) -> Unit,
    private val onAddAmountClick: (CreditGoal) -> Unit
) : ListAdapter<CreditGoal, GoalsAdapter.GoalViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GoalViewHolder {
        val binding = ItemGoalBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return GoalViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GoalViewHolder, position: Int) {
        val goal = getItem(position)
        holder.bind(goal)
    }

    inner class GoalViewHolder(
        private val binding: ItemGoalBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(goal: CreditGoal) {
            with(binding) {
                tvDescription.text = goal.description
                tvTargetAmount.text = Formatters.formatCurrency(goal.targetAmount)
                tvCurrentAmount.text = Formatters.formatCurrency(goal.currentAmount)
                tvEndDate.text = Formatters.formatDate(goal.endDate)
                tvDaysRemaining.text = context.getString(
                    R.string.label_days_remaining,
                    goal.daysRemaining()
                )
                progressBar.progress = (goal.calculateProgress() * 100).toInt()

                // Cambiar color según estado
                val colorRes = if (goal.isAchieved) R.color.success else R.color.colorPrimary
                progressBar.setIndicatorColor(context.getColor(colorRes))

                root.setOnClickListener { onItemClick(goal) }
                btnAddAmount.setOnClickListener { onAddAmountClick(goal) }
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<CreditGoal>() {
        override fun areItemsTheSame(oldItem: CreditGoal, newItem: CreditGoal): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: CreditGoal, newItem: CreditGoal): Boolean {
            return oldItem == newItem
        }
    }
}