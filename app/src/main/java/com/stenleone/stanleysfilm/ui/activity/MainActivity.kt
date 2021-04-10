package com.stenleone.stanleysfilm.ui.activity

import androidx.core.view.get
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.findFragment
import androidx.navigation.Navigation
import com.stenleone.stanleysfilm.BuildConfig
import com.stenleone.stanleysfilm.R
import com.stenleone.stanleysfilm.databinding.ActivityMainBinding
import com.stenleone.stanleysfilm.interfaces.FragmentWithNavController
import com.stenleone.stanleysfilm.managers.firebase.FirebaseRemoteConfigManager
import com.stenleone.stanleysfilm.model.entity.FirebaseConfigsEnum
import com.stenleone.stanleysfilm.network.entity.movie.MovieUI
import com.stenleone.stanleysfilm.ui.activity.base.BaseActivity
import com.stenleone.stanleysfilm.ui.adapter.viewPager.FragmentViewPagerAdapter
import com.stenleone.stanleysfilm.ui.dialog.UnSupportVersionDialog
import com.stenleone.stanleysfilm.ui.fragment.ResizeVideoFragment
import com.stenleone.stanleysfilm.ui.fragment.base.MainNavHostFragment
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

    fun openFilmFromResizeFragment(movie: MovieUI) {
        binding.fragmentPager.currentItem = 0
        ((binding.fragmentPager.adapter as FragmentViewPagerAdapter).listFragments.firstOrNull() as? MainNavHostFragment?)?.openFilm(movie, this)
    }

    fun openVideoFragment(videoUrl: String, movieUI: MovieUI) {
        if (supportFragmentManager.backStackEntryCount > 0) {
            (supportFragmentManager.findFragmentByTag(ResizeVideoFragment.TAG) as ResizeVideoFragment).updateVideoUrl(videoUrl, movieUI)
        } else {
            ResizeVideoFragment.newInstance(videoUrl, movieUI).also {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainer, it, ResizeVideoFragment.TAG)
                    .addToBackStack(ResizeVideoFragment.TAG)
                    .commit()
            }
        }
    }

    fun closeVideoFragment() {
        supportFragmentManager.popBackStack()
    }

    private fun checkAppVersionFromConfig() {
        firebaseConfig.getIntAsync(FirebaseConfigsEnum.MIN_SUPPORT_VERSION_CODE,
            success = {
                if (it > BuildConfig.VERSION_CODE) {
                    UnSupportVersionDialog.show(supportFragmentManager)
                }
            },
            failure = {

            })
    }

    override fun onBackPressed() {
        binding.apply {
            if ((supportFragmentManager.findFragmentByTag(ResizeVideoFragment.TAG) as? ResizeVideoFragment?)?.fullscreen != true) {
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
                (supportFragmentManager.findFragmentByTag(ResizeVideoFragment.TAG) as ResizeVideoFragment).collapseDown()
            }
        }
    }
}