package com.example.jokbalmanager.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.jokbalmanager.databinding.FragmentMonthPickerBinding
import java.util.*

class MonthPickerFragment(private val okButtonClickListener: (Int, Int) -> Unit) :
    DialogFragment() {
    private var _binding: FragmentMonthPickerBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMonthPickerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val cal = Calendar.getInstance()
        binding.apply {
            yearPicker.apply {
                minValue = 2015
                maxValue = 2099
                value = cal.get(Calendar.YEAR)
            }
            monthPicker.apply {
                minValue = 1
                maxValue = 12
                value = cal.get(Calendar.MONTH) + 1
            }
            cancelButton.setOnClickListener {
                dismiss()
            }
            okButton.setOnClickListener {
                okButtonClickListener(yearPicker.value, monthPicker.value)
                dismiss()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}