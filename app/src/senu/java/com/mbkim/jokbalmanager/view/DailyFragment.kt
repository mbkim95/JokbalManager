package com.mbkim.jokbalmanager.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import com.mbkim.jokbalmanager.viewmodel.DailyViewModel
import com.mbkim.jokbalmanager.adapter.DailyAdapter
import com.mbkim.jokbalmanager.databinding.FragmentDailyBinding
import com.mbkim.jokbalmanager.util.generateDummyData
import com.mbkim.jokbalmanager.util.getPreviousMonth
import com.mbkim.jokbalmanager.util.getTodayMonth

class DailyFragment : Fragment() {
    private var _binding: FragmentDailyBinding? = null
    private val binding get() = _binding!!
    private val viewModel: DailyViewModel by viewModels()
    private val dailyAdapter by lazy {
        DailyAdapter(generateDummyData()) { date, order ->
            DetailOrderDialogFragment(date, order, fixButtonClickListener = {
                viewModel.updateOrder(date, it)
            }, deleteButtonClickListener = {
                viewModel.deleteOrder(it)
            }).show(
                childFragmentManager,
                DetailOrderDialogFragment::class.java.simpleName
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDailyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getMonthOrderData()
        initializeView()
        observeData()
    }

    private fun observeData() {
        viewModel.apply {
            count.observe(viewLifecycleOwner) {
                binding.currentMonthText.text = getPreviousMonth(it)
            }
            monthOrders.observe(viewLifecycleOwner) {
                dailyAdapter.setDates(it)
            }
            totalWeight.observe(viewLifecycleOwner) {
                binding.totalMonthWeightTv.text = "${it}kg"
            }
            totalPrice.observe(viewLifecycleOwner) {
                binding.totalMonthPriceTv.text = "${it}원"
            }
            totalBalance.observe(viewLifecycleOwner) {
                binding.totalMonthBalanceTv.text = "${it}원"
            }
        }
    }

    private fun initializeView() {
        binding.apply {
            currentMonthText.text = getTodayMonth()
            prevMonthButton.setOnClickListener {
                viewModel.movePrevMonth()
            }
            nextMonthButton.setOnClickListener {
                viewModel.moveNextMonth()
            }
            dailyRv.apply {
                adapter = dailyAdapter
                addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
            }
            addButton.setOnClickListener {
                AddOrderDialogFragment { order ->
                    viewModel.addOrderData(order)
                }.show(
                    childFragmentManager,
                    AddOrderDialogFragment::class.java.simpleName
                )
            }
            currentMonthText.setOnClickListener {
                MonthPickerFragment(currentMonthText.text.toString()) { date ->
                    currentMonthText.text = date
                    viewModel.setDate(date)
                }.show(childFragmentManager, MonthPickerFragment::class.java.simpleName)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}