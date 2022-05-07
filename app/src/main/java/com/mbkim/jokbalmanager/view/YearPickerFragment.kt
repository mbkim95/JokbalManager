package com.mbkim.jokbalmanager.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.mbkim.jokbalmanager.databinding.FragmentYearPickerBinding

class YearPickerFragment(
    private val year: String,
    private val okButtonClickListener: (String) -> Unit
) :
    DialogFragment() {
    private var _binding: FragmentYearPickerBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentYearPickerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            yearPicker.apply {
                minValue = 2015
                maxValue = 2099
                value = year.toInt()
            }
            cancelButton.setOnClickListener {
                dismiss()
            }
            okButton.setOnClickListener {
                okButtonClickListener(yearPicker.value.toString())
                dismiss()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}