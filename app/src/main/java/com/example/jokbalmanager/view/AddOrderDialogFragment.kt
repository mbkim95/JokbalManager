package com.example.jokbalmanager.view

import android.content.Context
import android.graphics.Point
import android.os.Bundle
import android.view.*
import android.widget.RadioGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import com.example.jokbalmanager.databinding.AddOrderLayoutBinding
import com.example.jokbalmanager.model.db.OrderEntity
import java.util.*

class AddOrderDialogFragment(private val addButtonClickListener: (OrderEntity) -> Unit) :
    DialogFragment() {
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
            cancelButton.setOnClickListener { dismiss() }
            okButton.setOnClickListener {
                if (!isAllValueNotNull() || !isDateCorrect()) {
                    return@setOnClickListener
                }
                addButtonClickListener.invoke(makeOrderEntity())
            }
            weightEt.addTextChangedListener {
                if (it.isNullOrEmpty() || priceEt.text.isNullOrEmpty()) {
                    totalPriceTv.text = "0"
                    return@addTextChangedListener
                }

                try {
                    it.toString().toDouble()
                } catch (e: NumberFormatException) {
                    totalPriceTv.text = "0"
                    return@addTextChangedListener
                }

                totalPriceTv.text =
                    "${(priceEt.text.toString().toInt() * it.toString().toDouble()).toInt()}"
            }

            priceEt.addTextChangedListener {
                if (it.isNullOrEmpty() || weightEt.text.isNullOrEmpty()) {
                    totalPriceTv.text = "0"
                    return@addTextChangedListener
                }
                totalPriceTv.text =
                    "${(it.toString().toInt() * weightEt.text.toString().toDouble()).toInt()}"
            }

            depositEt.addTextChangedListener {
                if (it.isNullOrEmpty()) {
                    totalBalanceTv.text = "0"
                    return@addTextChangedListener
                }
                totalBalanceTv.text =
                    "${(totalPriceTv.text.toString().toInt() - it.toString().toInt())}"
            }
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

    private fun isAllValueNotNull(): Boolean {
        with(binding) {
            if (typeRg.checkedRadioButtonId == RadioGroup.NO_ID) {
                showToast("품목을 선택해주세요")
                return false
            }
            if (yearEt.text.isNullOrEmpty() || monthEt.text.isNullOrEmpty() || dayEt.text.isNullOrEmpty() || weightEt.text.isNullOrEmpty() || depositEt.text.isNullOrEmpty()) {
                showToast("값을 입력해주세요")
                return false
            }
        }
        return true
    }

    private fun isDateCorrect(): Boolean {
        val cal = Calendar.getInstance()
        val year = binding.yearEt.text.toString().toInt()
        val month = binding.monthEt.text.toString().toInt()
        val day = binding.dayEt.text.toString().toInt()
        cal.set(year, month - 1, 1)
        val finalDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH)

        if (year < 1900 || year > 2099) {
            showToast("1900년에서 2099년까지 날짜만 등록 가능합니다.")
            return false
        }
        if (month < 1 || month > 13) {
            showToast("1월에서 12월까지 날짜만 등록 가능합니다.")
            return false
        }
        if (day < 1 || day > finalDay) {
            showToast("1일에서 ${finalDay}일까지 날짜만 등록 가능합니다.")
            return false
        }
        return true
    }

    private fun makeOrderEntity(): OrderEntity {
        with(binding) {
            var month = monthEt.text.toString()
            if (month.length == 1) month = "0$month"
            var day = dayEt.text.toString()
            if (day.length == 1) day = "0$day"

            val type = when (typeRg.id) {
                frontRadio.id -> 0
                backRadio.id -> 1
                else -> 2
            }

            val price = priceEt.text.toString().toInt()
            val weight = weightEt.text.toString().toDouble()
            val deposit = depositEt.text.toString().toInt()

            return OrderEntity("${yearEt.text}-${month}-$day", type, price, weight, deposit)
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}