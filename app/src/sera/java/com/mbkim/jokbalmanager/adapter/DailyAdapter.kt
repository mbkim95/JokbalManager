package com.mbkim.jokbalmanager.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mbkim.jokbalmanager.R
import com.mbkim.jokbalmanager.databinding.DailyItemBinding
import com.mbkim.jokbalmanager.databinding.FooterDailyItemBinding
import com.mbkim.jokbalmanager.model.DayOrder
import com.mbkim.jokbalmanager.model.Jok
import com.mbkim.jokbalmanager.model.Order
import java.math.BigDecimal

class DailyAdapter(
    private var dates: List<DayOrder>,
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

    fun setDates(dates: List<DayOrder>) {
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
                front.root.setOnClickListener {
                    itemClickListener(day.date, day.orders.filter { it.type == Jok.FRONT }[0])
                }
                back.root.setOnClickListener {
                    itemClickListener(day.date, day.orders.filter { it.type == Jok.BACK }[0])
                }
                mix.root.setOnClickListener {
                    itemClickListener(day.date, day.orders.filter { it.type == Jok.MIX }[0])
                }
            }
        }

        private fun calculateBalance(order: Order): Long {
            val price =
                BigDecimal.valueOf(order.price).multiply(BigDecimal.valueOf(order.weight)).toLong()
            return (price - order.deposit)
        }

        private fun bindOrderDetail(orders: List<Order>) {
            if (orders.isEmpty()) {
                binding.apply {
                    totalWeightText.text = root.context.getText(R.string.zero_weight)
                    totalBalanceText.text = root.context.getText(R.string.zero_balance)
                    totalPriceText.text = root.context.getText(R.string.zero_balance)
                    front.root.visibility = View.GONE
                    back.root.visibility = View.GONE
                    mix.root.visibility = View.GONE
                }
                return
            }

            var totalWeight = BigDecimal.valueOf(0.0)
            var totalBalance = 0L
            var totalPrice = 0L
            orders.forEach {
                totalWeight = totalWeight.add(BigDecimal.valueOf(it.weight))
                totalBalance += calculateBalance(it)
                totalPrice += (BigDecimal.valueOf(it.price)
                    .multiply(BigDecimal.valueOf(it.weight))).toLong()
                when (it.type) {
                    Jok.FRONT -> {
                        binding.front.apply {
                            root.visibility = View.VISIBLE
                            orderTypeText.text = root.context.getString(R.string.front_leg)
                            weightText.text = "${it.weight}kg"
                            val price =
                                BigDecimal.valueOf(it.price).multiply(BigDecimal.valueOf(it.weight))
                                    .toLong()
                            totalPriceText.text = "${price}원"
                            balanceText.text = "${calculateBalance(it)}원"
                        }
                    }
                    Jok.BACK -> {
                        binding.back.apply {
                            root.visibility = View.VISIBLE
                            orderTypeText.text = root.context.getString(R.string.back_leg)
                            weightText.text = "${it.weight}kg"
                            val price =
                                BigDecimal.valueOf(it.price).multiply(BigDecimal.valueOf(it.weight))
                                    .toLong()
                            totalPriceText.text = "${price}원"
                            balanceText.text = "${calculateBalance(it)}원"
                        }
                    }
                    Jok.MIX -> {
                        binding.mix.apply {
                            root.visibility = View.VISIBLE
                            orderTypeText.text = root.context.getString(R.string.mix_leg)
                            weightText.text = "${it.weight}kg"
                            val price =
                                BigDecimal.valueOf(it.price).multiply(BigDecimal.valueOf(it.weight))
                                    .toLong()
                            totalPriceText.text = "${price}원"
                            balanceText.text = "${calculateBalance(it)}원"
                        }
                    }
                }
            }
            binding.apply {
                totalWeightText.text = "${totalWeight.toDouble()}kg"
                totalPriceText.text = "${totalPrice}원"
                totalBalanceText.text = "${totalBalance}원"
            }
        }
    }

    class FooterViewHolder(binding: FooterDailyItemBinding) : RecyclerView.ViewHolder(binding.root)

    companion object {
        const val ORDER = 0
        const val FOOTER = 1
        const val FOOTER_COUNT = 1
    }
}

