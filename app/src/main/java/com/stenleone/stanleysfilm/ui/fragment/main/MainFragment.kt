package com.stenleone.stanleysfilm.ui.fragment.main

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.stenleone.stanleysfilm.R
import com.stenleone.stanleysfilm.databinding.FragmentMainBinding
import com.stenleone.stanleysfilm.interfaces.ItemClick
import com.stenleone.stanleysfilm.managers.SharedPreferencesSortMainManager
import com.stenleone.stanleysfilm.network.TmdbNetworkConstant
import com.stenleone.stanleysfilm.network.entity.movie.Movie
import com.stenleone.stanleysfilm.ui.adapter.recyclerView.HorizontalListMovie
import com.stenleone.stanleysfilm.ui.fragment.FilmFragmentDirections
import com.stenleone.stanleysfilm.ui.fragment.MoreMovieFragmentDirections
import com.stenleone.stanleysfilm.ui.fragment.base.BaseFragment
import com.stenleone.stanleysfilm.util.constant.BindingConstant
import com.stenleone.stanleysfilm.util.extencial.throttleFirst
import com.stenleone.stanleysfilm.viewModel.network.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.ldralighieri.corbind.view.clicks
import javax.inject.Inject

@AndroidEntryPoint
class MainFragment : BaseFragment() {

    companion object {
        const val VERTICAL_SCROLL_POSITION = "vertical_scroll"
    }

    private lateinit var binding: FragmentMainBinding
    private val viewModel: MainViewModel by viewModels()

    @Inject lateinit var sharedPreferencesSortMainManager: SharedPreferencesSortMainManager
    @Inject lateinit var nowPlayingAdapter: HorizontalListMovie
    @Inject lateinit var popularAdapter: HorizontalListMovie
    @Inject lateinit var topRatedAdapter: HorizontalListMovie
    @Inject lateinit var upComingAdapter: HorizontalListMovie

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
                    sharedPreferencesSortMainManager.nowPlayingSortSmall = true
                    nowPlayingAdapter.typeHolder = HorizontalListMovie.TYPE_SMALL
                    nowPlayingAdapter.notifyDataSetChanged()
                }
                .launchIn(lifecycleScope)
            nowPlayingSortBig.clicks()
                .throttleFirst(BindingConstant.SMALL_THROTTLE)
                .onEach {
                    sharedPreferencesSortMainManager.nowPlayingSortSmall = false
                    nowPlayingAdapter.typeHolder = HorizontalListMovie.TYPE_LARGE
                    nowPlayingAdapter.notifyDataSetChanged()
                }
                .launchIn(lifecycleScope)

            popularSortSmall.clicks()
                .throttleFirst(BindingConstant.SMALL_THROTTLE)
                .onEach {
                    sharedPreferencesSortMainManager.popularSmallSort = true
                    popularAdapter.typeHolder = HorizontalListMovie.TYPE_SMALL
                    popularAdapter.notifyDataSetChanged()
                }
                .launchIn(lifecycleScope)
            popularSortBig.clicks()
                .throttleFirst(BindingConstant.SMALL_THROTTLE)
                .onEach {
                    sharedPreferencesSortMainManager.popularSmallSort = false
                    popularAdapter.typeHolder = HorizontalListMovie.TYPE_LARGE
                    popularAdapter.notifyDataSetChanged()
                }
                .launchIn(lifecycleScope)

            topRatedSortSmall.clicks()
                .throttleFirst(BindingConstant.SMALL_THROTTLE)
                .onEach {
                    sharedPreferencesSortMainManager.topMovieSortSmall = true
                    topRatedAdapter.typeHolder = HorizontalListMovie.TYPE_SMALL
                    topRatedAdapter.notifyDataSetChanged()
                }
                .launchIn(lifecycleScope)
            topRatedSortBig.clicks()
                .throttleFirst(BindingConstant.SMALL_THROTTLE)
                .onEach {
                    sharedPreferencesSortMainManager.topMovieSortSmall = false
                    topRatedAdapter.typeHolder = HorizontalListMovie.TYPE_LARGE
                    topRatedAdapter.notifyDataSetChanged()
                }
                .launchIn(lifecycleScope)

            upcomingSortSmall.clicks()
                .throttleFirst(BindingConstant.SMALL_THROTTLE)
                .onEach {
                    sharedPreferencesSortMainManager.upcomingSortSmall = true
                    upComingAdapter.typeHolder = HorizontalListMovie.TYPE_SMALL
                    upComingAdapter.notifyDataSetChanged()
                }
                .launchIn(lifecycleScope)
            upcomingSortBig.clicks()
                .throttleFirst(BindingConstant.SMALL_THROTTLE)
                .onEach {
                    sharedPreferencesSortMainManager.upcomingSortSmall = false
                    upComingAdapter.typeHolder = HorizontalListMovie.TYPE_LARGE
                    upComingAdapter.notifyDataSetChanged()
                }
                .launchIn(lifecycleScope)

            popularText.clicks()
                .throttleFirst(BindingConstant.SMALL_THROTTLE)
                .onEach {
                    findNavController().navigate(
                        MoreMovieFragmentDirections.actionGlobalMoreMovieFragment(
                            viewModel.moviePopularLiveData.value,
                            TmdbNetworkConstant.LIST_MOVIE_POPULAR,
                            null,
                            (recyclerPopular.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition(),
                            getString(R.string.popular)
                        )
                    )
                }
                .launchIn(lifecycleScope)

            nowPlayingText.clicks()
                .throttleFirst(BindingConstant.SMALL_THROTTLE)
                .onEach {
                    findNavController().navigate(
                        MoreMovieFragmentDirections.actionGlobalMoreMovieFragment(
                            viewModel.movieNowPlayingLiveData.value,
                            TmdbNetworkConstant.LIST_MOVIE_NOW_PLAYING,
                            null,
                            (recyclerNowPlaying.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition(),
                            getString(R.string.now_playing)
                        )
                    )
                }
                .launchIn(lifecycleScope)

            topRatedText.clicks()
                .throttleFirst(BindingConstant.SMALL_THROTTLE)
                .onEach {
                    findNavController().navigate(
                        MoreMovieFragmentDirections.actionGlobalMoreMovieFragment(
                            viewModel.movieTopRatedLiveData.value,
                            TmdbNetworkConstant.LIST_MOVIE_TOP_RATED,
                            null,
                            (recyclerTopRated.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition(),
                            getString(R.string.top_rated)
                        )
                    )
                }
                .launchIn(lifecycleScope)

            upcomingText.clicks()
                .throttleFirst(BindingConstant.SMALL_THROTTLE)
                .onEach {
                    findNavController().navigate(
                        MoreMovieFragmentDirections.actionGlobalMoreMovieFragment(
                            viewModel.movieUpcomingLiveData.value,
                            TmdbNetworkConstant.LIST_MOVIE_TOP_UPCOMING,
                            null,
                            (recyclerUpcoming.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition(),
                            getString(R.string.upcoming)
                        )
                    )
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

            nowPlayingAdapter.typeHolder = if (sharedPreferencesSortMainManager.nowPlayingSortSmall) {
                HorizontalListMovie.TYPE_SMALL
            } else {
                HorizontalListMovie.TYPE_LARGE
            }
            popularAdapter.typeHolder = if (sharedPreferencesSortMainManager.popularSmallSort) {
                HorizontalListMovie.TYPE_SMALL
            } else {
                HorizontalListMovie.TYPE_LARGE
            }
            topRatedAdapter.typeHolder = if (sharedPreferencesSortMainManager.topMovieSortSmall) {
                HorizontalListMovie.TYPE_SMALL
            } else {
                HorizontalListMovie.TYPE_LARGE
            }
            upComingAdapter.typeHolder = if (sharedPreferencesSortMainManager.upcomingSortSmall) {
                HorizontalListMovie.TYPE_SMALL
            } else {
                HorizontalListMovie.TYPE_LARGE
            }
        }
    }

    private fun setupViewModelCallBack() {
        binding.apply {
            viewModel.apply {
                movieLatesLiveData.observe(viewLifecycleOwner) {
                    swipeToRefresh.isRefreshing = false
                    latestShimmerViewContainer.hideShimmer()
                    latestEpisode = it
                }
                movieNowPlayingLiveData.observe(viewLifecycleOwner) {
                    swipeToRefresh.isRefreshing = false
                    nowPlayingAdapter.itemList.clear()
                    nowPlayingAdapter.itemList.addAll(it.movies)
                    nowPlayingAdapter.notifyDataSetChanged()
                }
                moviePopularLiveData.observe(viewLifecycleOwner) {
                    swipeToRefresh.isRefreshing = false
                    popularAdapter.itemList.clear()
                    popularAdapter.itemList.addAll(it.movies)
                    popularAdapter.notifyDataSetChanged()
                }
                movieTopRatedLiveData.observe(viewLifecycleOwner) {
                    swipeToRefresh.isRefreshing = false
                    topRatedAdapter.itemList.clear()
                    topRatedAdapter.itemList.addAll(it.movies)
                    topRatedAdapter.notifyDataSetChanged()
                }
                movieUpcomingLiveData.observe(viewLifecycleOwner) {
                    swipeToRefresh.isRefreshing = false
                    upComingAdapter.itemList.clear()
                    upComingAdapter.itemList.addAll(it.movies)
                    upComingAdapter.notifyDataSetChanged()
                }
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (this::binding.isInitialized) {
            outState.putInt(VERTICAL_SCROLL_POSITION, binding.scrollContainerLay.verticalScrollbarPosition)
        }
    }
}