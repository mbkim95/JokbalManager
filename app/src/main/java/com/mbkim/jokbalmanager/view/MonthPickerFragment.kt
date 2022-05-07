package com.mbkim.jokbalmanager.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.mbkim.jokbalmanager.databinding.FragmentMonthPickerBinding

class MonthPickerFragment(
    private val date: String,
    private val okButtonClickListener: (String) -> Unit
) :
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
        binding.apply {
            yearPicker.apply {
                minValue = 2015
                maxValue = 2099
                value = date.substring(0, 4).toInt()
            }
            monthPicker.apply {
                minValue = 1
                maxValue = 12
                value = date.substring(5, 7).toInt()
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
                okButtonClickListener("${year}-${month}")
                dismiss()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}