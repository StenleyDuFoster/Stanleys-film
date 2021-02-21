package com.stenleone.stanleysfilm.ui.fragment

import android.animation.ValueAnimator
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayoutMediator
import com.stenleone.stanleysfilm.R
import com.stenleone.stanleysfilm.databinding.FragmentFilmBinding
import com.stenleone.stanleysfilm.interfaces.ItemClick
import com.stenleone.stanleysfilm.interfaces.ItemClickParcelable
import com.stenleone.stanleysfilm.managers.controllers.filmFinders.FindFilmFilmixController
import com.stenleone.stanleysfilm.managers.firebase.FirebaseAnalyticsManagers
import com.stenleone.stanleysfilm.managers.firebase.FirebaseCloudFirestoreManagers
import com.stenleone.stanleysfilm.managers.sharedPrefs.SharedPreferencesSortMainManager
import com.stenleone.stanleysfilm.network.TmdbNetworkConstant
import com.stenleone.stanleysfilm.network.entity.movie.MovieUI
import com.stenleone.stanleysfilm.ui.activity.MainActivity
import com.stenleone.stanleysfilm.ui.adapter.recyclerView.GenreListRecycler
import com.stenleone.stanleysfilm.ui.adapter.recyclerView.ListMovieAdapter
import com.stenleone.stanleysfilm.ui.adapter.recyclerView.StudiosListRecycler
import com.stenleone.stanleysfilm.ui.adapter.viewPager.ImageViewPager
import com.stenleone.stanleysfilm.ui.adapter.viewPager.YouTubeTrailersAdapter
import com.stenleone.stanleysfilm.ui.dialog.RateMovieDialog
import com.stenleone.stanleysfilm.ui.dialog.UnSupportVersionDialog
import com.stenleone.stanleysfilm.ui.fragment.base.BaseFragment
import com.stenleone.stanleysfilm.util.anim.ButtonTextColorAnimator
import com.stenleone.stanleysfilm.util.constant.BindingConstant
import com.stenleone.stanleysfilm.util.extencial.copyToClipBoard
import com.stenleone.stanleysfilm.util.extencial.throttleClicks
import com.stenleone.stanleysfilm.util.extencial.throttleFirst
import com.stenleone.stanleysfilm.viewModel.network.FilmViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.ldralighieri.corbind.view.clicks
import javax.inject.Inject

@AndroidEntryPoint
class FilmFragment : BaseFragment() {

    companion object {
        const val VERTICAL_SCROLL_POSITION = "vertical_scroll"
    }

    private lateinit var binding: FragmentFilmBinding
    private val navArgs: FilmFragmentArgs by navArgs()
    private val viewModel: FilmViewModel by viewModels()
    private lateinit var buttonColorAnim: ButtonTextColorAnimator

    @Inject
    lateinit var genreAdapter: GenreListRecycler

    @Inject
    lateinit var recomendedMovieAdapterAdapter: ListMovieAdapter

    @Inject
    lateinit var studioAdapter: StudiosListRecycler

    @Inject
    lateinit var sharedPreferencesSortMainManager: SharedPreferencesSortMainManager

    @Inject
    lateinit var analyticsManagers: FirebaseAnalyticsManagers

    @Inject
    lateinit var imageViewPager: ImageViewPager

    lateinit var youTubePlayersAdapter: YouTubeTrailersAdapter

    override fun setupBinding(inflater: LayoutInflater, container: ViewGroup?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_film, container, false)
        return binding.root
    }

    override fun setup(savedInstanceState: Bundle?) {

        setupArgs()
        setupToolbarViewPager()
        setupClicks()
        setupViewModelCallBack()
        setupYouTubePager()
        setupRecyclerView()

        savedInstanceState?.getInt(VERTICAL_SCROLL_POSITION)?.let {
            binding.nestedScrollContainer.scrollTo(0, it)
        }
    }

    private fun setupArgs() {
        binding.apply {
            movie = navArgs.movie
            navArgs.movie?.voteAverage?.let {
                donutProgressRate.progress = it * 10
                donutProgressRate.text = (it * 10).toInt().toString()
            }
        }
        buttonColorAnim = ButtonTextColorAnimator(requireContext())
    }

    private fun setupToolbarViewPager() {
        binding.apply {
            navArgs.movie?.backdropPath?.let {
                if (imageViewPager.listItems.size <= 1) {
                    navArgs.movie?.posterPath?.let {
                        imageViewPager.listItems.add(it)
                    }
                    imageViewPager.listItems.add(it)
                }
            }

            imageViewPager.title = navArgs.movie?.title ?: navArgs.movie?.originalTitle ?: ""
            imageViewPager.moveListener = object : ItemClick {
                override fun click(position: Int) {
                    titlePager.setCurrentItem(position, false)
                }
            }

            titlePager.adapter = imageViewPager
            TabLayoutMediator(tabLayout, titlePager, { tabs, pager -> }).attach()
        }
    }

    private fun setupYouTubePager() {
        youTubePlayersAdapter = YouTubeTrailersAdapter(this)
        binding.apply {
            toolbarLay.youTubeVideoPager.adapter = youTubePlayersAdapter
            TabLayoutMediator(toolbarLay.tabLayoutYouTubeVideoPager, toolbarLay.youTubeVideoPager, { tab, pager -> }).attach()
        }
    }

    private fun setupRecyclerView() {

        val filmClickListener = object : ItemClickParcelable {
            override fun click(item: Parcelable) {
                if (item is MovieUI) {
                    findNavController().navigate(FilmFragmentDirections.actionGlobalFilmFragment(item))
                }
            }
        }

        binding.apply {
            genreRecycler.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
            genreRecycler.adapter = genreAdapter
            recomendedMovieAdapterAdapter.listener = filmClickListener
            recyclerRecomendedList.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
            recyclerRecomendedList.adapter = recomendedMovieAdapterAdapter
            recyclerStudioList.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
            recyclerStudioList.adapter = studioAdapter

            recomendedMovieAdapterAdapter.typeHolder = if (sharedPreferencesSortMainManager.recomendedSortSmall) {
                ListMovieAdapter.TYPE_SMALL
            } else {
                ListMovieAdapter.TYPE_LARGE
            }
        }
    }

    private fun setupViewModelCallBack() {
        navArgs.movie?.id?.let {
            viewModel.getPageData(it)
        }
        viewModel.startFindFilmUrl(navArgs.movie?.title ?: navArgs.movie?.originalTitle ?: "", navArgs.movie?.releaseDate)
        viewModel.movieUrl.observe(viewLifecycleOwner, {
            binding.apply {
                if (progressLoadUrl.visibility == View.VISIBLE) {
                    progressLoadUrl.visibility = View.GONE
                    buttonColorAnim.toActive(watchButtonText)
                    watchButtonText.text = getString(R.string.watch_online)
                }
            }
        })
        viewModel.imageList.observe(viewLifecycleOwner) {
            binding.apply {
                if (it.posters.size > 0) {
                    tabLayout.visibility = View.VISIBLE
                }

                val oldItemCount = imageViewPager.listItems.size
                if (imageViewPager.listItems.size <= 2) {
                    it.posters.forEach {
                        imageViewPager.listItems.add(it.file_path)
                    }
                    titlePager.adapter?.notifyItemChanged(oldItemCount, imageViewPager.listItems.size - 1)
                }
            }
        }
        viewModel.moviesVideos.observe(viewLifecycleOwner, {
            if (it.results.size > 0) {
                binding.toolbarLay.tabLayoutYouTubeVideoPager.visibility = View.VISIBLE
                if (youTubePlayersAdapter.itemList.size == 0) {
                    youTubePlayersAdapter.itemList.addAll(it.results)
                    youTubePlayersAdapter.notifyDataSetChanged()
                }
            }
        })
        viewModel.findFilmFilmixController.status.observe(viewLifecycleOwner, {
            if (viewModel.movieUrl.value == null) {
                binding.watchButtonText.text = it
            }
        })
        viewModel.findFilmFilmixController.progress.observe(viewLifecycleOwner, {
            if (viewModel.movieUrl.value == null && it == FindFilmFilmixController.FAILED_FIND) {
                binding.progressLoadUrl.visibility = View.GONE
            }
        })
        viewModel.movieDetails.observe(viewLifecycleOwner) {
            binding.movieDetails = it
            genreAdapter.itemList.clear()
            genreAdapter.itemList.addAll(it.genres)

            studioAdapter.itemList.clear()
            studioAdapter.itemList.addAll(it.productionCompaniesUI)

            binding.genreRecycler.adapter?.notifyDataSetChanged()
            binding.recyclerStudioList.adapter?.notifyDataSetChanged()
            binding.genreRecycler.visibility = View.VISIBLE
        }
        viewModel.recomendedMovieList.observe(viewLifecycleOwner) {

            recomendedMovieAdapterAdapter.itemList.clear()
            recomendedMovieAdapterAdapter.itemList.addAll(it.movies)
            binding.recyclerRecomendedList.adapter?.notifyDataSetChanged()
            binding.genreRecycler.visibility = View.VISIBLE
        }
        viewModel.favoriteIdList.observe(viewLifecycleOwner) {
            binding.apply {
                toolbarLay.addToFavorite.visibility = View.VISIBLE

                if (it.get(FirebaseCloudFirestoreManagers.MOVIE)?.contains(navArgs.movie?.id?.toString()) ?: false) {
                    toolbarLay.addToFavorite.progress = 1f
                }
            }
        }
        viewModel.updateFavoriteStatus.observe(viewLifecycleOwner, {
            binding.apply {

                if (it.updateSuccess) {
                    Toast.makeText(requireContext(), getString(R.string.added_to_favorite), Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), getString(R.string.error_added_to_favorite), Toast.LENGTH_SHORT).show()
                }

                if (it.isFavorite) {
                    toolbarLay.addToFavorite.progress = 1f
                } else {
                    toolbarLay.addToFavorite.progress = 0f
                }
            }
        })
    }

    private fun setupClicks() {
        binding.apply {
            toolbarLay.backButton.clicks()
                .throttleFirst(BindingConstant.SMALL_THROTTLE)
                .onEach {
                    requireActivity().onBackPressed()
                }
                .launchIn(lifecycleScope)

            watchButton.clicks()
                .throttleFirst(BindingConstant.SMALL_THROTTLE)
                .onEach {
                    viewModel.movieUrl.value?.let { url ->
                        analyticsManagers.openFilm(navArgs.movie?.title ?: "")
                        navArgs.movie?.let { movie ->
                            (requireActivity() as MainActivity).openVideoFragment(url, movie)
                        }
                    }
                }
                .launchIn(lifecycleScope)

            textOriginalTitle.clicks()
                .throttleFirst(BindingConstant.SMALL_THROTTLE)
                .onEach {
                    requireActivity().copyToClipBoard(navArgs.movie?.originalTitle)
                }
                .launchIn(lifecycleScope)

            textTitle.clicks()
                .throttleFirst(BindingConstant.SMALL_THROTTLE)
                .onEach {
                    requireActivity().copyToClipBoard(navArgs.movie?.title)
                }
                .launchIn(lifecycleScope)

            recomendedSortSmall.clicks()
                .throttleFirst(BindingConstant.SMALL_THROTTLE)
                .onEach {
                    sharedPreferencesSortMainManager.recomendedSortSmall = true
                    recomendedMovieAdapterAdapter.typeHolder = ListMovieAdapter.TYPE_SMALL
                    recomendedMovieAdapterAdapter.notifyDataSetChanged()
                }
                .launchIn(lifecycleScope)

            recomendedSortBig.clicks()
                .throttleFirst(BindingConstant.SMALL_THROTTLE)
                .onEach {
                    sharedPreferencesSortMainManager.recomendedSortSmall = false
                    recomendedMovieAdapterAdapter.typeHolder = ListMovieAdapter.TYPE_LARGE
                    recomendedMovieAdapterAdapter.notifyDataSetChanged()
                }
                .launchIn(lifecycleScope)

            rateMovie.throttleClicks(
                {
                    RateMovieDialog.show(childFragmentManager, navArgs.movie?.title ?: "", navArgs.movie?.id ?: 0)
                }, lifecycleScope
            )

            throttleClicks(
                toolbarLay.addToFavorite, {
                    if (toolbarLay.addToFavorite.progress == 0f) {
                        viewModel.addToFavorite((navArgs.movie?.id ?: 0).toString())
                        val animator = ValueAnimator.ofFloat(0f, 1f)
                        animator.duration = 1000
                        animator.addUpdateListener {
                            toolbarLay.addToFavorite.progress = (Math.abs(it.getAnimatedValue() as Float))
                        }
                        animator.start()
                    } else {
                        viewModel.removeFromFavorite((navArgs.movie?.id ?: 0).toString())
                        val animator = ValueAnimator.ofFloat(1f, 0f)
                        animator.duration = 1000
                        animator.addUpdateListener {
                            toolbarLay.addToFavorite.progress = (Math.abs(it.getAnimatedValue() as Float))
                        }
                        animator.start()
                    }
                }
            )

            recomendedText.clicks()
                .throttleFirst(BindingConstant.SMALL_THROTTLE)
                .onEach {
                    findNavController().navigate(
                        MoreMovieFragmentDirections.actionGlobalMoreMovieFragment(
                            viewModel.recomendedMovieList.value,
                            TmdbNetworkConstant.LIST_RECOMENDED,
                            navArgs.movie?.id.toString(),
                            (recyclerRecomendedList.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition(),
                            "${getString(R.string.recomended_listtitle)} \"${navArgs.movie?.title ?: navArgs.movie?.originalTitle}\""
                        )
                    )
                }
                .launchIn(lifecycleScope)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (this::binding.isInitialized) {
            outState.putInt(VERTICAL_SCROLL_POSITION, binding.nestedScrollContainer.verticalScrollbarPosition)
        }
    }
}