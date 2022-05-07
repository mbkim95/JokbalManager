package com.mbkim.jokbalmanager.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.mbkim.jokbalmanager.databinding.FragmentDayPickerBinding
import java.util.*

class DayPickerFragment(
    private val year: Int,
    private val month: Int,
    private val day: Int,
    private val okButtonClickListener: (String) -> Unit
) :
    DialogFragment() {
    private var _binding: FragmentDayPickerBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDayPickerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val cal = Calendar.getInstance()
        binding.apply {
            yearPicker.apply {
                minValue = 2015
                maxValue = 2099
                value = year
            }
            monthPicker.apply {
                minValue = 1
                maxValue = 12
                value = month
            }
            monthPicker.setOnValueChangedListener { picker, oldVal, newVal ->
                dayPicker.apply {
                    minValue = 1
                    cal.set(Calendar.MONTH, newVal - 1)
                    maxValue = cal.getActualMaximum(Calendar.DAY_OF_MONTH)
                }
            }
            dayPicker.apply {
                minValue = 1
                maxValue = cal.getActualMaximum(Calendar.DAY_OF_MONTH)
                value = day
            }
            cancelButton.setOnClickListener {
                dismiss()
            }
            okButton.setOnClickListener {
                val year = yearPicker.value
                var month = monthPicker.value.toString()
                if (month.length == 1) {
                    month = "0$month"
                }
                var day = dayPicker.value.toString()
                if (day.length == 1) {
                    day = "0$day"
                }
                okButtonClickListener("${year}-${month}-${day}")
                dismiss()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}