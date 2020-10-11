package com.stenleone.stanleysfilm.util.bind

import androidx.viewpager.widget.ViewPager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.stenleone.stanleysfilm.R

class BindViewPager(private val pager: ViewPager) {

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
    }

    private fun converterViewPagerAndBottomNavPosition(positionOrId: Int): Int {
        return when (positionOrId) {
            R.id.navigation_dashboard -> 1
            R.id.navigation_notifications -> 2
            R.id.navigation_home -> 0
            1 -> R.id.navigation_dashboard
            2 -> R.id.navigation_notifications
            else -> R.id.navigation_home
        }
    }
}