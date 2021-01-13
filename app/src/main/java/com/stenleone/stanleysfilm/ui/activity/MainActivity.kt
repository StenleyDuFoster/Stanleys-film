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
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity() {

    lateinit var binding: ActivityMainBinding
    private lateinit var viewPagerAdapter: FragmentViewPagerAdapter

    private var videoFragment: VideoFragment? = null

    override fun setup() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        setupViewPager()
        setupVideoFragment()
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

    private fun setupVideoFragment() {

    }

    fun openVideoFragment(videoUrl: String) {
        if (videoFragment != null) {
            videoFragment?.updateVideoUrl(videoUrl)
        } else {
            videoFragment = VideoFragment.newInstance(videoUrl).also {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainer, it)
                    .commit()
            }
        }
    }

    fun closeVideoFragment() {
        videoFragment?.let {
            supportFragmentManager.beginTransaction()
                .remove(it)
                .commit()
        }
        videoFragment = null
        binding.mainMotionLayout.progress = 0f
    }
}