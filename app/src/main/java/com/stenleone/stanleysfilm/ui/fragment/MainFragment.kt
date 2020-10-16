package com.stenleone.stanleysfilm.ui.fragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.stenleone.stanleysfilm.R
import com.stenleone.stanleysfilm.databinding.FragmentMainBinding
import com.stenleone.stanleysfilm.ui.adapter.recyclerView.HorizontalListRecycler
import com.stenleone.stanleysfilm.ui.fragment.base.BaseFragment
import com.stenleone.stanleysfilm.viewModel.network.MainViewModel
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainFragment : BaseFragment() {

    private lateinit var binding: FragmentMainBinding
    private val viewModel: MainViewModel by viewModel()

    private val nowPlayingAdapter: HorizontalListRecycler by inject()
    private val popularAdapter: HorizontalListRecycler by inject()
    private val topRatedAdapter: HorizontalListRecycler by inject()
    private val upComingAdapter: HorizontalListRecycler by inject()

    override fun setupBinding(inflater: LayoutInflater, container: ViewGroup?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false)
        return binding.root
    }

    override fun setup() {
        setupRecyclerView()
        setupViewModelCallBack()
    }

    private fun setupRecyclerView() {
        binding.apply {
            recyclerNowPlaying.layoutManager =
                LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
            recyclerPopular.layoutManager =
                LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
            recyclerTopRated.layoutManager =
                LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
            recyclerUpcoming.layoutManager =
                LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
            recyclerNowPlaying.adapter = nowPlayingAdapter
            recyclerPopular.adapter = popularAdapter
            recyclerTopRated.adapter = topRatedAdapter
            recyclerUpcoming.adapter = upComingAdapter
        }
    }

    private fun setupViewModelCallBack() {
        binding.apply {
            viewModel.apply {
                movieLatesLiveData.observe(viewLifecycleOwner, {

                })
                movieNowPlayingLiveData.observe(viewLifecycleOwner, {
                    nowPlayingAdapter.itemList.clear()
                    nowPlayingAdapter.itemList.addAll(it.movies)
                    nowPlayingAdapter.notifyDataSetChanged()
                })
                moviePopularLiveData.observe(viewLifecycleOwner, {
                    popularAdapter.itemList.clear()
                    popularAdapter.itemList.addAll(it.movies)
                    popularAdapter.notifyDataSetChanged()
                })
                movieTopRatedLiveData.observe(viewLifecycleOwner, {
                    topRatedAdapter.itemList.clear()
                    topRatedAdapter.itemList.addAll(it.movies)
                    topRatedAdapter.notifyDataSetChanged()
                })
                movieUpcomingLiveData.observe(viewLifecycleOwner, {
                    upComingAdapter.itemList.clear()
                    upComingAdapter.itemList.addAll(it.movies)
                    upComingAdapter.notifyDataSetChanged()
                })
            }
        }
    }

}