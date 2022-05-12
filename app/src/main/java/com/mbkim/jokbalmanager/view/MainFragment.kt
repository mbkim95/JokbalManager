package com.mbkim.jokbalmanager.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.tabs.TabLayoutMediator
import com.mbkim.jokbalmanager.R
import com.mbkim.jokbalmanager.adapter.TabAdapter
import com.mbkim.jokbalmanager.databinding.FragmentMainBinding
import com.mbkim.jokbalmanager.viewmodel.MainViewModel

class MainFragment : Fragment() {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private val tabNames = listOf("일별 보기", "합계 보기")
    private val viewModel: MainViewModel by viewModels()

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

        viewModel.errorMessage.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                Toast.makeText(requireContext(), "에러가 발생했습니다.\n$it", Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.isCompleted.observe(viewLifecycleOwner) {
            if (it) {
                Toast.makeText(requireContext(), "작업이 완료되었습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.backup -> {
                backup()
            }
            R.id.restore -> {
                val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                    addCategory(Intent.CATEGORY_OPENABLE)
                    type = "*/*"
                }
                startActivityForResult(intent, REQUEST_BACKUP_FILE)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun backup() {
        viewModel.saveDatabase()
        restartApp()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_BACKUP_FILE && resultCode == Activity.RESULT_OK) {
            data?.data?.let {
                viewModel.restoreDatabase(requireContext().contentResolver, it)
                restartApp()
            }
        }
    }

    private fun restartApp() {
        val intent = Intent(requireActivity(), MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        requireContext().startActivity(intent)
        requireActivity().finish()
        Runtime.getRuntime().exit(0)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val REQUEST_BACKUP_FILE = 100
    }
}