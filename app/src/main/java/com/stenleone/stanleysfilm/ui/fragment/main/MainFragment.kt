package com.stenleone.stanleysfilm.ui.fragment.main

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.stenleone.stanleysfilm.R
import com.stenleone.stanleysfilm.databinding.FragmentMainBinding
import com.stenleone.stanleysfilm.interfaces.ItemClick
import com.stenleone.stanleysfilm.managers.SharedPreferencesSortMainManager
import com.stenleone.stanleysfilm.network.entity.movie.Movie
import com.stenleone.stanleysfilm.network.entity.movie.MoviesEntity
import com.stenleone.stanleysfilm.ui.adapter.recyclerView.HorizontalListRecycler
import com.stenleone.stanleysfilm.ui.fragment.FilmFragmentDirections
import com.stenleone.stanleysfilm.ui.fragment.base.BaseFragment
import com.stenleone.stanleysfilm.util.constant.BindingConstant
import com.stenleone.stanleysfilm.util.extencial.throttleFirst
import com.stenleone.stanleysfilm.viewModel.network.MainViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.ldralighieri.corbind.view.clicks

class MainFragment : BaseFragment() {

    companion object {
        const val VERTICAL_SCROLL_POSITION = "vertical_scroll"
    }

    private lateinit var binding: FragmentMainBinding
    private val viewModel: MainViewModel by viewModel()

    private val sharedPreferencesSortMainManager: SharedPreferencesSortMainManager by inject()
    private val nowPlayingAdapter: HorizontalListRecycler by inject()
    private val popularAdapter: HorizontalListRecycler by inject()
    private val topRatedAdapter: HorizontalListRecycler by inject()
    private val upComingAdapter: HorizontalListRecycler by inject()

    override fun setupBinding(inflater: LayoutInflater, container: ViewGroup?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false)
        return binding.root
    }

    override fun setup(savedInstanceState: Bundle?) {
        setupRecyclerView()
        setupViewModelCallBack()
        setupSwipeToRefresh()
        setupSortButtons()

        savedInstanceState?.getInt(VERTICAL_SCROLL_POSITION)?.let {
            binding.scrollContainerLay.scrollTo(0, it)
        }
    }

    private fun setupSwipeToRefresh() {
        binding.apply {
            swipeToRefresh.setOnRefreshListener {
                viewModel.loadContent()
            }
        }
    }

    private fun setupSortButtons() {
        binding.apply {
            nowPlayingSortSmall.clicks()
                .throttleFirst(BindingConstant.SMALL_THROTTLE)
                .onEach {
                    sharedPreferencesSortMainManager.nowPlayiningSortSmall = true
                    nowPlayingAdapter.typeHolder = HorizontalListRecycler.TYPE_SMALL
                    nowPlayingAdapter.notifyDataSetChanged()
                }
                .launchIn(lifecycleScope)
            nowPlayingSortBig.clicks()
                .throttleFirst(BindingConstant.SMALL_THROTTLE)
                .onEach {
                    sharedPreferencesSortMainManager.nowPlayiningSortSmall = false
                    nowPlayingAdapter.typeHolder = HorizontalListRecycler.TYPE_LARGE
                    nowPlayingAdapter.notifyDataSetChanged()
                }
                .launchIn(lifecycleScope)

            popularSortSmall.clicks()
                .throttleFirst(BindingConstant.SMALL_THROTTLE)
                .onEach {
                    sharedPreferencesSortMainManager.popularSmallSort = true
                    popularAdapter.typeHolder = HorizontalListRecycler.TYPE_SMALL
                    popularAdapter.notifyDataSetChanged()
                }
                .launchIn(lifecycleScope)
            popularSortBig.clicks()
                .throttleFirst(BindingConstant.SMALL_THROTTLE)
                .onEach {
                    sharedPreferencesSortMainManager.popularSmallSort = false
                    popularAdapter.typeHolder = HorizontalListRecycler.TYPE_LARGE
                    popularAdapter.notifyDataSetChanged()
                }
                .launchIn(lifecycleScope)

            topRatedSortSmall.clicks()
                .throttleFirst(BindingConstant.SMALL_THROTTLE)
                .onEach {
                    sharedPreferencesSortMainManager.topMovieSortSmall = true
                    topRatedAdapter.typeHolder = HorizontalListRecycler.TYPE_SMALL
                    topRatedAdapter.notifyDataSetChanged()
                }
                .launchIn(lifecycleScope)
            topRatedSortBig.clicks()
                .throttleFirst(BindingConstant.SMALL_THROTTLE)
                .onEach {
                    sharedPreferencesSortMainManager.topMovieSortSmall = false
                    topRatedAdapter.typeHolder = HorizontalListRecycler.TYPE_LARGE
                    topRatedAdapter.notifyDataSetChanged()
                }
                .launchIn(lifecycleScope)

            upcomingSortSmall.clicks()
                .throttleFirst(BindingConstant.SMALL_THROTTLE)
                .onEach {
                    sharedPreferencesSortMainManager.upcomingSortSmall = true
                    upComingAdapter.typeHolder = HorizontalListRecycler.TYPE_SMALL
                    upComingAdapter.notifyDataSetChanged()
                }
                .launchIn(lifecycleScope)
            upcomingSortBig.clicks()
                .throttleFirst(BindingConstant.SMALL_THROTTLE)
                .onEach {
                    sharedPreferencesSortMainManager.upcomingSortSmall = false
                    upComingAdapter.typeHolder = HorizontalListRecycler.TYPE_LARGE
                    upComingAdapter.notifyDataSetChanged()
                }
                .launchIn(lifecycleScope)
        }
    }

    private fun setupRecyclerView() {
        binding.apply {
            recyclerNowPlaying.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
            recyclerPopular.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
            recyclerTopRated.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
            recyclerUpcoming.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
            recyclerNowPlaying.adapter = nowPlayingAdapter
            recyclerPopular.adapter = popularAdapter
            recyclerTopRated.adapter = topRatedAdapter
            recyclerUpcoming.adapter = upComingAdapter

            val clickListener = object : ItemClick {
                override fun click(item: Parcelable) {
                    if (item is Movie) {
                        findNavController().navigate(
                            FilmFragmentDirections.actionGlobalFilmFragment(item)
                        )
                    }
                }
            }

            nowPlayingAdapter.listener = clickListener
            popularAdapter.listener = clickListener
            topRatedAdapter.listener = clickListener
            upComingAdapter.listener = clickListener

            nowPlayingAdapter.typeHolder = if (sharedPreferencesSortMainManager.nowPlayiningSortSmall) {
                HorizontalListRecycler.TYPE_SMALL
            } else {
                HorizontalListRecycler.TYPE_LARGE
            }
            popularAdapter.typeHolder = if (sharedPreferencesSortMainManager.popularSmallSort) {
                HorizontalListRecycler.TYPE_SMALL
            } else {
                HorizontalListRecycler.TYPE_LARGE
            }
            topRatedAdapter.typeHolder = if (sharedPreferencesSortMainManager.topMovieSortSmall) {
                HorizontalListRecycler.TYPE_SMALL
            } else {
                HorizontalListRecycler.TYPE_LARGE
            }
            upComingAdapter.typeHolder = if (sharedPreferencesSortMainManager.upcomingSortSmall) {
                HorizontalListRecycler.TYPE_SMALL
            } else {
                HorizontalListRecycler.TYPE_LARGE
            }
        }
    }

    private fun setupViewModelCallBack() {
        binding.apply {
            viewModel.apply {
                movieLatesLiveData.observe(viewLifecycleOwner, {
                    swipeToRefresh.isRefreshing = false
                    latestShimmerViewContainer.hideShimmer()
                    latestEpisode = it
                })
                movieNowPlayingLiveData.observe(viewLifecycleOwner, {
                    swipeToRefresh.isRefreshing = false
                    nowPlayingAdapter.itemList.clear()
                    nowPlayingAdapter.itemList.addAll(it.movies)
                    nowPlayingAdapter.notifyDataSetChanged()
                })
                moviePopularLiveData.observe(viewLifecycleOwner, {
                    swipeToRefresh.isRefreshing = false
                    popularAdapter.itemList.clear()
                    popularAdapter.itemList.addAll(it.movies)
                    popularAdapter.notifyDataSetChanged()
                })
                movieTopRatedLiveData.observe(viewLifecycleOwner, {
                    swipeToRefresh.isRefreshing = false
                    topRatedAdapter.itemList.clear()
                    topRatedAdapter.itemList.addAll(it.movies)
                    topRatedAdapter.notifyDataSetChanged()
                })
                movieUpcomingLiveData.observe(viewLifecycleOwner, {
                    swipeToRefresh.isRefreshing = false
                    upComingAdapter.itemList.clear()
                    upComingAdapter.itemList.addAll(it.movies)
                    upComingAdapter.notifyDataSetChanged()
                })
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(VERTICAL_SCROLL_POSITION, binding.scrollContainerLay.verticalScrollbarPosition)
    }
}