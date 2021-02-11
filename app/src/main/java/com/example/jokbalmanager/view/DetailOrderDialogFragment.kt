package com.example.jokbalmanager.view

import android.content.Context
import android.graphics.Point
import android.os.Bundle
import android.view.*
import android.widget.RadioGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import com.example.jokbalmanager.databinding.DetailOrderLayoutBinding
import com.example.jokbalmanager.model.Jok
import com.example.jokbalmanager.model.Order
import com.example.jokbalmanager.model.db.OrderEntity
import java.util.*

class DetailOrderDialogFragment(
    private var date: String,
    private var order: Order,
    private val fixButtonClickListener: (OrderEntity) -> Unit,
    private val deleteButtonClickListener: (OrderEntity) -> Unit
) :
    DialogFragment() {
    private var _binding: DetailOrderLayoutBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DetailOrderLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindView(date, order)
    }

    private fun bindView(date: String, order: Order) {
        binding.apply {
            yearEt.setText(date.substring(0, 4))
            monthEt.setText(date.substring(5, 7))
            dayEt.setText(date.subSequence(8, 10))
            when (order.type) {
                Jok.FRONT -> frontRadio.isChecked = true
                Jok.BACK -> backRadio.isChecked = true
                else -> mixRadio.isChecked = true
            }
            weightEt.setText(order.weight.toString())
            priceEt.setText(order.price.toString())
            totalPriceTv.text = (order.weight * order.price).toInt().toString()
            depositEt.setText(order.deposit.toString())
            totalBalanceTv.text = ((order.weight * order.price).toInt() - order.deposit).toString()

            okButton.setOnClickListener {
                dismiss()
            }
            deleteButton.setOnClickListener {
                deleteButtonClickListener(makeOrderEntity())
                dismiss()
            }
            fixButton.setOnClickListener {
                detailButtonLayout.visibility = View.GONE
                fixOrderLayout.visibility = View.VISIBLE
                enableEdit()
            }
            fixOkButton.setOnClickListener {
                if (!isAllValueNotNull() || !isDateCorrect()) {
                    return@setOnClickListener
                }
                fixButtonClickListener(makeOrderEntity())
                detailButtonLayout.visibility = View.VISIBLE
                fixOrderLayout.visibility = View.GONE
                disableEdit()
            }
            fixCancelButton.setOnClickListener {
                detailButtonLayout.visibility = View.VISIBLE
                fixOrderLayout.visibility = View.GONE
                setOrder(date, order)
                disableEdit()
            }

            okButton.setOnClickListener {
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
                    "${(priceEt.text.toString().toInt() * it.toString().toDouble()).toInt()}"

                if (depositEt.text.isNullOrEmpty()) {
                    totalBalanceTv.text = "${totalPriceTv.text.toString().toInt()}"
                } else {
                    totalBalanceTv.text = "${
                        (totalPriceTv.text.toString().toInt() - depositEt.text.toString().toInt())
                    }"
                }
            }

            priceEt.addTextChangedListener {
                if (it.isNullOrEmpty() || weightEt.text.isNullOrEmpty()) {
                    totalPriceTv.text = "0"
                    return@addTextChangedListener
                }
                totalPriceTv.text =
                    "${(it.toString().toInt() * weightEt.text.toString().toDouble()).toInt()}"

                if (depositEt.text.isNullOrEmpty()) {
                    totalBalanceTv.text = "${totalPriceTv.text.toString().toInt()}"
                } else {
                    totalBalanceTv.text = "${
                        (totalPriceTv.text.toString().toInt() - depositEt.text.toString().toInt())
                    }"
                }
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

    fun setOrder(date: String, order: Order) {
        this.date = date
        this.order = order
        bindView(date, order)
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

    private fun enableEdit() {
        binding.apply {
            yearEt.isFocusableInTouchMode = true
            monthEt.isFocusableInTouchMode = true
            dayEt.isFocusableInTouchMode = true
            weightEt.isFocusableInTouchMode = true
            priceEt.isFocusableInTouchMode = true
            depositEt.isFocusableInTouchMode = true
        }
    }

    private fun disableEdit() {
        binding.apply {
            yearEt.disableView()
            yearEt.disableView()
            monthEt.disableView()
            dayEt.disableView()
            weightEt.disableView()
            priceEt.disableView()
            depositEt.disableView()
        }
    }

    private fun View.disableView() {
        this.apply {
            isFocusableInTouchMode = false
            isFocusable = false
            isClickable = false
        }
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

            val type = if (frontRadio.isChecked) 0 else if (backRadio.isChecked) 1 else 2
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