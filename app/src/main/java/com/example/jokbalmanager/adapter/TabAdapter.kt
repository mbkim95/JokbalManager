package com.example.jokbalmanager.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.jokbalmanager.view.DailyFragment
import com.example.jokbalmanager.view.TotalFragment

class TabAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> DailyFragment()
            else -> TotalFragment()
        }
    }
}