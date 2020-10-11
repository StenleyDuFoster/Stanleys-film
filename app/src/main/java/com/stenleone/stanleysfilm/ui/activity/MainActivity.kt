package com.stenleone.stanleysfilm.ui.activity

import androidx.databinding.DataBindingUtil
import com.stenleone.stanleysfilm.R
import com.stenleone.stanleysfilm.databinding.ActivityMainBinding
import com.stenleone.stanleysfilm.ui.activity.base.BaseActivity
import com.stenleone.stanleysfilm.ui.adapter.viewPager.FragmentViewPagerAdapter
import com.stenleone.stanleysfilm.ui.fragment.VideoFragment
import com.stenleone.stanleysfilm.util.bind.BindViewPager

class MainActivity : BaseActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewPagerAdapter: FragmentViewPagerAdapter

    private lateinit var videoFragment: VideoFragment

    override fun setup() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        videoFragment = VideoFragment()

        setupViewPager()
    }

    private fun setupViewPager() {
        viewPagerAdapter = FragmentViewPagerAdapter(supportFragmentManager)
        binding.apply {
            fragmentPager.adapter = viewPagerAdapter
            fragmentPager.offscreenPageLimit = 3
            BindViewPager(fragmentPager).withBottomNav(bottomNavView)
        }
    }

    override fun onBackPressed() {
        if (binding.fragmentPager.currentItem == 0) {
            super.onBackPressed()
        } else {
            binding.fragmentPager.currentItem = 0
        }
    }
}