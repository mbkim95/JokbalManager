package com.mbkim.jokbalmanager.view

import android.content.Context
import android.graphics.Point
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import com.mbkim.jokbalmanager.databinding.DetailOrderLayoutBinding
import com.mbkim.jokbalmanager.model.Order
import com.mbkim.jokbalmanager.model.db.OrderEntity
import java.math.BigDecimal

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
            pickDateTv.apply {
                text = date
                setOnClickListener {
                    val year = date.substring(0, 4).toInt()
                    val month = date.substring(5, 7).toInt()
                    val day = date.substring(8, 10).toInt()
                    DayPickerFragment(year, month, day) {
                        text = it
                    }.show(childFragmentManager, DayPickerFragment::class.java.simpleName)
                }
            }
            val price = (BigDecimal.valueOf(order.weight)
                .multiply(BigDecimal.valueOf(order.price))).toLong()
            weightEt.setText(order.weight.toString())
            priceEt.setText(order.price.toString())
            totalPriceTv.text = price.toString()
            depositEt.setText(order.deposit.toString())
            totalBalanceTv.text = (price - order.deposit).toString()

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
                if (!isAllValueNotNull()) {
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
            if (weightEt.text.isNullOrEmpty() || depositEt.text.isNullOrEmpty()) {
                showToast("값을 입력해주세요")
                return false
            }
        }
        return true
    }

    private fun enableEdit() {
        binding.apply {
            pickDateTv.isEnabled = true
            weightEt.isFocusableInTouchMode = true
            priceEt.isFocusableInTouchMode = true
            depositEt.isFocusableInTouchMode = true
        }
    }

    private fun disableEdit() {
        binding.apply {
            pickDateTv.isEnabled = false
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