package com.example.jokbalmanager.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.jokbalmanager.databinding.FragmentDailyBinding
import com.example.jokbalmanager.util.getPreviousMonth
import com.example.jokbalmanager.util.getTodayMonth

class DailyFragment : Fragment() {
    private var _binding: FragmentDailyBinding? = null
    private val binding get() = _binding!!
    private var count = 0

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
                currentMonthText.text = getPreviousMonth(--count)
            }
            nextMonthButton.setOnClickListener {
                currentMonthText.text = getPreviousMonth(++count)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}