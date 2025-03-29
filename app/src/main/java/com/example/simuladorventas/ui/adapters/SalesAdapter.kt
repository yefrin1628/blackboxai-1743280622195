package com.example.simuladorventas.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.simuladorventas.databinding.ItemSaleBinding
import com.example.simuladorventas.datos.model.Sale
import com.example.simuladorventas.utils.Formatters

class SalesAdapter(
    private val onItemClick: (Sale) -> Unit
) : ListAdapter<Sale, SalesAdapter.SaleViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SaleViewHolder {
        val binding = ItemSaleBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return SaleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SaleViewHolder, position: Int) {
        val sale = getItem(position)
        holder.bind(sale)
    }

    inner class SaleViewHolder(
        private val binding: ItemSaleBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(sale: Sale) {
            with(binding) {
                tvProductName.text = sale.productName
                tvQuantity.text = sale.quantity.toString()
                tvUnitPrice.text = Formatters.formatCurrency(sale.unitPrice)
                tvSubtotal.text = Formatters.formatCurrency(sale.calculateSubtotal())
                
                root.setOnClickListener { onItemClick(sale) }
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Sale>() {
        override fun areItemsTheSame(oldItem: Sale, newItem: Sale): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Sale, newItem: Sale): Boolean {
            return oldItem == newItem
        }
    }
}