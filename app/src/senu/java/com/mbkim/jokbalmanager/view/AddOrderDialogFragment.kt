package com.mbkim.jokbalmanager.view

import android.content.Context
import android.graphics.Point
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import com.mbkim.jokbalmanager.databinding.AddOrderLayoutBinding
import com.mbkim.jokbalmanager.model.db.OrderEntity
import java.math.BigDecimal
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
            val year = cal.get(Calendar.YEAR)
            var month = (cal.get(Calendar.MONTH) + 1).toString()
            if (month.length == 1) {
                month = "0$month"
            }
            var day = (cal.get(Calendar.DATE)).toString()
            if (day.length == 1) {
                day = "0$day"
            }
            pickDateTv.text = "${year}-${month}-${day}"
            pickDateTv.setOnClickListener {
                DayPickerFragment(year.toInt(), month.toInt(), day.toInt()) {
                    pickDateTv.text = it
                }.show(childFragmentManager, DayPickerFragment::class.java.simpleName)
            }

            cancelButton.setOnClickListener { dismiss() }
            okButton.setOnClickListener {
                if (!isAllValueNotNull()) {
                    return@setOnClickListener
                }
                addButtonClickListener.invoke(makeOrderEntity())
                dismiss()
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
                    totalBalanceTv.text = "0"
                    return@addTextChangedListener
                }
                totalPriceTv.text =
                    "${
                        (BigDecimal.valueOf(priceEt.text.toString().toLong()).multiply(
                            BigDecimal.valueOf(it.toString().toDouble())
                        )).toLong()
                    }"

                if (depositEt.text.isNullOrEmpty()) {
                    totalBalanceTv.text = "${totalPriceTv.text.toString().toLong()}"
                } else {
                    totalBalanceTv.text = "${
                        (totalPriceTv.text.toString().toLong() - depositEt.text.toString().toLong())
                    }"
                }
            }

            priceEt.addTextChangedListener {
                if (it.isNullOrEmpty() || weightEt.text.isNullOrEmpty()) {
                    totalPriceTv.text = "0"
                    return@addTextChangedListener
                }
                totalPriceTv.text =
                    "${
                        (BigDecimal.valueOf(it.toString().toLong()).multiply(
                            BigDecimal.valueOf(weightEt.text.toString().toDouble())
                        )).toLong()
                    }"

                if (depositEt.text.isNullOrEmpty()) {
                    totalBalanceTv.text = "${totalPriceTv.text.toString().toLong()}"
                } else {
                    totalBalanceTv.text = "${
                        (totalPriceTv.text.toString().toLong() - depositEt.text.toString().toLong())
                    }"
                }
            }

            depositEt.addTextChangedListener {
                if (it.isNullOrEmpty()) {
                    totalBalanceTv.text = "0"
                    return@addTextChangedListener
                }
                totalBalanceTv.text =
                    "${(totalPriceTv.text.toString().toLong() - it.toString().toLong())}"
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
            if (weightEt.text.isNullOrEmpty() || depositEt.text.isNullOrEmpty()) {
                showToast("값을 입력해주세요")
                return false
            }
        }
        return true
    }

    private fun makeOrderEntity(): OrderEntity {
        with(binding) {
            val price = priceEt.text.toString().toLong()
            val weight = weightEt.text.toString().toDouble()
            val deposit = depositEt.text.toString().toLong()
            return OrderEntity(pickDateTv.text.toString(), price, weight, deposit)
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