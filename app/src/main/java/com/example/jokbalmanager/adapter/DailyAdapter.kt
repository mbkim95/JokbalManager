package com.example.jokbalmanager.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.jokbalmanager.databinding.DailyItemBinding

class DailyAdapter(private var dates: List<String>) :
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

    fun setDates(dates: List<String>) {
        this.dates = dates
        notifyDataSetChanged()
    }

    class DailyViewHolder(private val binding: DailyItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(date: String) {
            binding.apply {
                dateText.text = date
                root.setOnClickListener {
                    orderDetailLayout.visibility = when (orderDetailLayout.visibility) {
                        View.VISIBLE -> View.GONE
                        else -> View.VISIBLE
                    }
                }
            }
        }
    }
}

