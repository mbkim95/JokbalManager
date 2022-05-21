package com.mbkim.jokbalmanager.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mbkim.jokbalmanager.databinding.DailyItemBinding
import com.mbkim.jokbalmanager.databinding.FooterDailyItemBinding
import com.mbkim.jokbalmanager.model.Order
import java.math.BigDecimal

class DailyAdapter(
    private var dates: List<Order>,
    private val itemClickListener: (String, Order) -> Unit
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == ORDER) {
            val binding =
                DailyItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            DailyViewHolder(binding, itemClickListener)
        } else {
            val binding =
                FooterDailyItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            FooterViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is DailyViewHolder) {
            holder.bind(dates[position])
        }
    }

    fun setDates(dates: List<Order>) {
        this.dates = dates
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = dates.size + FOOTER_COUNT

    override fun getItemViewType(position: Int): Int {
        return if (position in dates.indices) ORDER else FOOTER
    }

    class DailyViewHolder(
        private val binding: DailyItemBinding,
        private val itemClickListener: (String, Order) -> Unit
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(order: Order) {
            binding.apply {
                dateText.text = order.date
                totalWeightText.text = "${order.weight}kg"
                totalPriceText.text = "${(BigDecimal.valueOf(order.price).multiply(BigDecimal.valueOf(order.weight))).toLong()}원"
                totalBalanceText.text = "${calculateBalance(order)}원"

                root.setOnClickListener {
                    itemClickListener(order.date, order)
                }
            }
        }

        private fun calculateBalance(order: Order): Long {
            val price =
                BigDecimal.valueOf(order.price).multiply(BigDecimal.valueOf(order.weight)).toLong()
            return (price - order.deposit)
        }
    }

    class FooterViewHolder(binding: FooterDailyItemBinding) : RecyclerView.ViewHolder(binding.root)

    companion object {
        const val ORDER = 0
        const val FOOTER = 1
        const val FOOTER_COUNT = 1
    }
}

