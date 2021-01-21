package com.stenleone.stanleysfilm.util.bind

import androidx.viewpager.widget.ViewPager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.stenleone.stanleysfilm.R
import com.stenleone.stanleysfilm.interfaces.FragmentWithNavController
import com.stenleone.stanleysfilm.ui.adapter.viewPager.FragmentViewPagerAdapter

class BindViewPager(private val pager: ViewPager) {

    init {
        if (pager.adapter !is FragmentViewPagerAdapter) {
            throw RuntimeException("not support view pager")
        }
    }

    fun withBottomNav(nav: BottomNavigationView) {

        pager.addOnPageChangeListener(object: ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {}

            override fun onPageSelected(position: Int) {
                nav.selectedItemId = converterViewPagerAndBottomNavPosition(position)
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })

        nav.setOnNavigationItemSelectedListener {
            pager.currentItem = converterViewPagerAndBottomNavPosition(it.itemId)
            return@setOnNavigationItemSelectedListener true
        }

        nav.setOnNavigationItemReselectedListener {
           ((pager.adapter as FragmentViewPagerAdapter).listFragments[pager.currentItem] as FragmentWithNavController).popToStart()
        }
    }

    private fun converterViewPagerAndBottomNavPosition(positionOrId: Int): Int {
        return when (positionOrId) {
            R.id.navigation_dashboard -> FragmentViewPagerAdapter.SEARCH_FRAGMENT
            R.id.navigation_notifications -> FragmentViewPagerAdapter.SETTINGS_FRAGMENT
            R.id.navigation_home -> FragmentViewPagerAdapter.MAIN_FRAGMENT
            FragmentViewPagerAdapter.SEARCH_FRAGMENT -> R.id.navigation_dashboard
            FragmentViewPagerAdapter.SETTINGS_FRAGMENT -> R.id.navigation_notifications
            else -> R.id.navigation_home
        }
    }
}