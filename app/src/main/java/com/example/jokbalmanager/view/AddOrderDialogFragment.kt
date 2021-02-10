package com.example.jokbalmanager.view

import android.content.Context
import android.graphics.Point
import android.os.Bundle
import android.view.*
import androidx.fragment.app.DialogFragment
import com.example.jokbalmanager.databinding.AddOrderLayoutBinding
import java.util.*

class AddOrderDialogFragment : DialogFragment() {
    private var _binding: AddOrderLayoutBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = AddOrderLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val cal = Calendar.getInstance()
        binding.apply {
            yearEt.setText(cal.get(Calendar.YEAR).toString())
            monthEt.setText((cal.get(Calendar.MONTH) + 1).toString())
            dayEt.setText(cal.get(Calendar.DATE).toString())
        }
    }

    override fun onResume() {
        super.onResume()

        val windowManager =
            requireActivity().getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display: Display = windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)

        val params: ViewGroup.LayoutParams? = dialog?.window?.attributes
        val deviceWidth = size.x
        params?.width = (deviceWidth * 0.95).toInt()
        dialog?.window?.attributes = params as WindowManager.LayoutParams
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}