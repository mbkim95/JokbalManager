package com.mbkim.jokbalmanager.view

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.mbkim.jokbalmanager.R
import com.mbkim.jokbalmanager.adapter.TabAdapter
import com.mbkim.jokbalmanager.databinding.FragmentMainBinding

class MainFragment : Fragment() {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private val tabNames = listOf("일별 보기", "합계 보기")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(layoutInflater, container, false)
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.title_menu, menu)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            pager.adapter = TabAdapter(this@MainFragment)
            TabLayoutMediator(binding.tab, binding.pager) { tab, position ->
                tab.text = tabNames[position]
            }.attach()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.backup -> {
                Toast.makeText(requireContext(), "백업하기", Toast.LENGTH_SHORT).show()
            }
            R.id.restore -> {
                Toast.makeText(requireContext(), "복구하기", Toast.LENGTH_SHORT).show()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}