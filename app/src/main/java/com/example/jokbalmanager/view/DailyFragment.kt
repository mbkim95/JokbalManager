package com.example.jokbalmanager.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.jokbalmanager.adapter.DailyAdapter
import com.example.jokbalmanager.databinding.FragmentDailyBinding
import com.example.jokbalmanager.util.getDaysOfPreviousMonth
import com.example.jokbalmanager.util.getPreviousMonth
import com.example.jokbalmanager.util.getTodayMonth
import com.example.jokbalmanager.viewmodel.DailyViewModel

class DailyFragment : Fragment() {
    private var _binding: FragmentDailyBinding? = null
    private val binding get() = _binding!!
    private val adapter by lazy {
        DailyAdapter(getDaysOfPreviousMonth(0))
    }
    private val viewModel: DailyViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDailyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            currentMonthText.text = getTodayMonth()
            prevMonthButton.setOnClickListener {
                viewModel.movePrevMonth()
            }
            nextMonthButton.setOnClickListener {
                viewModel.moveNextMonth()
            }
            dailyRv.adapter = adapter
            dailyRv.addItemDecoration(
                DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
            )
        }
        viewModel.count.observe(viewLifecycleOwner) {
            adapter.setDates(getDaysOfPreviousMonth(it))
            binding.currentMonthText.text = getPreviousMonth(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}