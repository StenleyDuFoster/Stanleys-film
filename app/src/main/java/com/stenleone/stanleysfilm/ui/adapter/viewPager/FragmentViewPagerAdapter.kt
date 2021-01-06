package com.stenleone.stanleysfilm.ui.adapter.viewPager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.stenleone.stanleysfilm.ui.fragment.base.MainNavHostFragment
import com.stenleone.stanleysfilm.ui.fragment.base.SearchNavHostFragment
import com.stenleone.stanleysfilm.ui.fragment.base.SettingsNavHostFragment

class FragmentViewPagerAdapter(
    supportFragmentManager: FragmentManager
) : FragmentStatePagerAdapter(supportFragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    companion object {
        const val MAIN_FRAGMENT = 0
        const val SEARCH_FRAGMENT = 1
        const val SETTINGS_FRAGMENT = 2
    }

    private val listFragments = arrayListOf(
        MainNavHostFragment(),
        SearchNavHostFragment(),
        SettingsNavHostFragment()
    )

    override fun getItem(position: Int): Fragment {
        return listFragments[position]
    }

    override fun getCount(): Int = listFragments.size

}