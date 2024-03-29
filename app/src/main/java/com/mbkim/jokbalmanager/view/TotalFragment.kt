package com.mbkim.jokbalmanager.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import com.mbkim.jokbalmanager.adapter.TotalAdapter
import com.mbkim.jokbalmanager.databinding.FragmentTotalBinding
import com.mbkim.jokbalmanager.util.generateYearDummyData
import com.mbkim.jokbalmanager.util.getPreviousYear
import com.mbkim.jokbalmanager.viewmodel.TotalViewModel

class TotalFragment : Fragment() {
    private var _binding: FragmentTotalBinding? = null
    private val binding get() = _binding!!
    private val viewModel: TotalViewModel by viewModels()
    private val totalAdapter: TotalAdapter by lazy {
        TotalAdapter(generateYearDummyData())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTotalBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getYearOrderData()
        initializeView()
        observeData()
    }

    override fun onResume() {
        super.onResume()
        viewModel.getYearOrderData()
    }

    private fun initializeView() {
        binding.apply {
            prevYearButton.setOnClickListener {
                viewModel.movePrevYear()
            }
            nextYearButton.setOnClickListener {
                viewModel.moveNextYear()
            }
            yearDataRv.apply {
                adapter = totalAdapter
                addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
            }
            currentYearText.setOnClickListener {
                YearPickerFragment(currentYearText.text.toString()) { date ->
                    currentYearText.text = date
                    viewModel.setYear(date)
                }.show(childFragmentManager, YearPickerFragment::class.java.simpleName)
            }
        }
    }

    private fun observeData() {
        viewModel.apply {
            count.observe(viewLifecycleOwner) {
                binding.currentYearText.text = getPreviousYear(it)
            }
            yearOrder.observe(viewLifecycleOwner) {
                totalAdapter.setOrders(it)
            }
            totalWeight.observe(viewLifecycleOwner) {
                binding.totalYearWeightTv.text = "${it}kg"
            }
            totalPrice.observe(viewLifecycleOwner) {
                binding.totalYearPriceTv.text = "${it}원"
            }
            totalBalance.observe(viewLifecycleOwner) {
                binding.totalYearBalanceTv.text = "${it}원"
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}