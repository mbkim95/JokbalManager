package com.example.jokbalmanager.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.jokbalmanager.R
import com.example.jokbalmanager.databinding.DailyItemBinding
import com.example.jokbalmanager.model.DayOrder
import com.example.jokbalmanager.model.Jok
import com.example.jokbalmanager.model.Order

class DailyAdapter(private var dates: List<DayOrder>) :
    RecyclerView.Adapter<DailyAdapter.DailyViewHolder>() {
    private lateinit var binding: DailyItemBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailyViewHolder {
        binding = DailyItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DailyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DailyViewHolder, position: Int) {
        holder.bind(dates[position])
    }

    override fun getItemCount(): Int = dates.size

    fun setDates(dates: List<DayOrder>) {
        this.dates = dates
        notifyDataSetChanged()
    }

    class DailyViewHolder(private val binding: DailyItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(day: DayOrder) {
            binding.apply {
                dateText.text = day.date
                bindOrderDetail(day.orders)
                root.setOnClickListener {
                    orderDetailLayout.visibility = when (orderDetailLayout.visibility) {
                        View.VISIBLE -> View.GONE
                        else -> View.VISIBLE
                    }
                }
            }
        }

        private fun calculateBalance(order: Order): Int {
            return ((order.price * order.weight).toInt() - order.deposit)
        }

        private fun bindOrderDetail(orders: List<Order>) {
            if (orders.isEmpty()) {
                binding.apply {
                    totalWeightText.text = root.context.getText(R.string.zero_weight)
                    totalBalanceText.text = root.context.getText(R.string.zero_balance)
                    front.root.visibility = View.GONE
                    back.root.visibility = View.GONE
                    mix.root.visibility = View.GONE
                }
                return
            }

            var totalWeight = 0.0
            var totalBalance = 0
            orders.forEach {
                totalWeight += it.weight
                totalBalance += calculateBalance(it)
                when (it.type) {
                    Jok.FRONT -> {
                        binding.front.apply {
                            root.visibility = View.VISIBLE
                            orderTypeText.text = root.context.getString(R.string.front_leg)
                            weightText.text = "${it.weight}kg"
                            balanceText.text = "${calculateBalance(it)}원"
                        }
                    }
                    Jok.BACK -> {
                        binding.back.apply {
                            root.visibility = View.VISIBLE
                            orderTypeText.text = root.context.getString(R.string.back_leg)
                            weightText.text = "${it.weight}kg"
                            balanceText.text = "${calculateBalance(it)}원"
                        }
                    }
                    Jok.MIX -> {
                        binding.mix.apply {
                            root.visibility = View.VISIBLE
                            orderTypeText.text = root.context.getString(R.string.mix_leg)
                            weightText.text = "${it.weight}kg"
                            balanceText.text = "${calculateBalance(it)}원"
                        }
                    }
                }
            }
            binding.apply {
                totalWeightText.text = "${totalWeight}kg"
                totalBalanceText.text = "${totalBalance}원"
            }
        }
    }
}

