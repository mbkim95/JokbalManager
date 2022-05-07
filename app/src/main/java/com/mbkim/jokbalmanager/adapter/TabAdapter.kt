package com.mbkim.jokbalmanager.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.mbkim.jokbalmanager.view.DailyFragment
import com.mbkim.jokbalmanager.view.TotalFragment

class TabAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> DailyFragment()
            else -> TotalFragment()
        }
    }
}