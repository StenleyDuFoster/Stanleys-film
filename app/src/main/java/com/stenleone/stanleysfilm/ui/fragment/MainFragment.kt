package com.stenleone.stanleysfilm.ui.fragment

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.stenleone.stanleysfilm.R
import com.stenleone.stanleysfilm.databinding.FragmentMainBinding
import com.stenleone.stanleysfilm.ui.fragment.base.BaseFragment
import com.stenleone.stanleysfilm.viewModel.network.MainViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainFragment : BaseFragment() {

    private lateinit var binding: FragmentMainBinding
    private val viewModel: MainViewModel by viewModel()

    override fun setupBinding(inflater: LayoutInflater, container: ViewGroup?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false)
        return binding.root
    }

    override fun setup() {
        setupViewModelCallBack()
    }

    private fun setupViewModelCallBack() {
        binding.apply {
            viewModel.apply {
                movieLatesLiveData.observe(viewLifecycleOwner, {
                    Log.v("112233", it.toString())
                })
                movieNowPlayingLiveData.observe(viewLifecycleOwner, {

                })
                moviePopularLiveData.observe(viewLifecycleOwner, {

                })
                movieTopRatedLiveData.observe(viewLifecycleOwner, {

                })
                movieUpcomingLiveData.observe(viewLifecycleOwner, {

                })
            }
        }
    }

}