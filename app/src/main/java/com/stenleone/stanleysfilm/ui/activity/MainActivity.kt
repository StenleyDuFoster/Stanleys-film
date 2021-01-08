package com.stenleone.stanleysfilm.ui.activity

import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.stenleone.stanleysfilm.R
import com.stenleone.stanleysfilm.databinding.ActivityMainBinding
import com.stenleone.stanleysfilm.interfaces.FragmentWithNavController
import com.stenleone.stanleysfilm.ui.activity.base.BaseActivity
import com.stenleone.stanleysfilm.ui.adapter.viewPager.FragmentViewPagerAdapter
import com.stenleone.stanleysfilm.ui.fragment.VideoFragment
import com.stenleone.stanleysfilm.util.bind.BindViewPager

class MainActivity : BaseActivity() {

    lateinit var binding: ActivityMainBinding
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
        binding.apply {
            if (!(viewPagerAdapter.listFragments[fragmentPager.currentItem] as FragmentWithNavController).getNavController().navigateUp()) {
                if (fragmentPager.currentItem == 0) {
                    super.onBackPressed()
                } else {
                    fragmentPager.currentItem = 0
                }
            }
        }
    }

    fun openVideoFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, VideoFragment.newInstance(""))
            .commit()
    }

}