package com.stenleone.stanleysfilm.ui.activity

import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import com.stenleone.stanleysfilm.BuildConfig
import com.stenleone.stanleysfilm.R
import com.stenleone.stanleysfilm.databinding.ActivityMainBinding
import com.stenleone.stanleysfilm.interfaces.FragmentWithNavController
import com.stenleone.stanleysfilm.managers.firebase.FirebaseRemoteConfigManager
import com.stenleone.stanleysfilm.model.entity.FirebaseConfigsEnum
import com.stenleone.stanleysfilm.ui.activity.base.BaseActivity
import com.stenleone.stanleysfilm.ui.adapter.viewPager.FragmentViewPagerAdapter
import com.stenleone.stanleysfilm.ui.fragment.VideoFragment
import com.stenleone.stanleysfilm.util.bind.BindViewPager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : BaseActivity() {

    lateinit var binding: ActivityMainBinding
    private lateinit var viewPagerAdapter: FragmentViewPagerAdapter
    @Inject
    lateinit var firebaseConfig: FirebaseRemoteConfigManager

    override fun setup() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        setupViewPager()
        checkAppVersionFromConfig()
    }

    private fun setupViewPager() {
        viewPagerAdapter = FragmentViewPagerAdapter(supportFragmentManager)
        binding.apply {
            fragmentPager.adapter = viewPagerAdapter
            fragmentPager.offscreenPageLimit = 3

            BindViewPager(fragmentPager).withBottomNav(bottomNavView)
        }
    }

    fun openVideoFragment(videoUrl: String) {
        if (supportFragmentManager.backStackEntryCount > 0) {
            (supportFragmentManager.findFragmentByTag(VideoFragment.TAG) as VideoFragment).updateVideoUrl(videoUrl)
        } else {
            VideoFragment.newInstance(videoUrl).also {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainer, it, VideoFragment.TAG)
                    .addToBackStack(VideoFragment.TAG)
                    .commit()
            }
        }
    }

    fun closeVideoFragment() {
        supportFragmentManager.popBackStack()
    }

    private fun checkAppVersionFromConfig() {
        firebaseConfig.getIntAsync(FirebaseConfigsEnum.APP_VERSION_CODE,
            success = {

            },
            failure = {

            })
    }

    override fun onBackPressed() {
        binding.apply {
            if (supportFragmentManager.backStackEntryCount == 0) {
                if (!Navigation.findNavController(
                        this@MainActivity,
                        (viewPagerAdapter.listFragments[fragmentPager.currentItem] as FragmentWithNavController).getNavControllerId()
                    ).navigateUp()
                ) {
                    if (fragmentPager.currentItem == 0) {
                        super.onBackPressed()
                    } else {
                        fragmentPager.currentItem = 0
                    }
                }
            } else {
                supportFragmentManager.popBackStack()
            }
        }
    }
}