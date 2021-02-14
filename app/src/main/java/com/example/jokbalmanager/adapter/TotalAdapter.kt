package com.example.jokbalmanager.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.jokbalmanager.databinding.MonthItemBinding
import com.example.jokbalmanager.model.MonthOrder

class TotalAdapter(private var orders: List<MonthOrder>) :
    RecyclerView.Adapter<TotalAdapter.TotalViewHolder>() {
    private lateinit var binding: MonthItemBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TotalViewHolder {
        binding = MonthItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TotalViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TotalViewHolder, position: Int) {
        holder.bind(orders[position])
    }

    override fun getItemCount(): Int = orders.size

    fun setOrders(orders: List<MonthOrder>) {
        this.orders = orders
        notifyDataSetChanged()
    }

    class TotalViewHolder(private val binding: MonthItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(order: MonthOrder) {
            binding.apply {
                dateText.text = order.month
                totalWeightText.text = "${order.weight}kg"
                totalPriceText.text = "${order.price}원"
                totalBalanceText.text = "${order.balance}원"
            }
        }
    }
}
