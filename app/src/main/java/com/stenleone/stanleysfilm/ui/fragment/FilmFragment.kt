package com.stenleone.stanleysfilm.ui.fragment

import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.stenleone.stanleysfilm.R
import com.stenleone.stanleysfilm.databinding.FragmentFilmBinding
import com.stenleone.stanleysfilm.interfaces.ItemClick
import com.stenleone.stanleysfilm.managers.firebase.FirebaseAnalyticsManagers
import com.stenleone.stanleysfilm.managers.sharedPrefs.SharedPreferencesSortMainManager
import com.stenleone.stanleysfilm.network.TmdbNetworkConstant
import com.stenleone.stanleysfilm.network.entity.movie.Movie
import com.stenleone.stanleysfilm.ui.activity.MainActivity
import com.stenleone.stanleysfilm.ui.adapter.recyclerView.GenreListRecycler
import com.stenleone.stanleysfilm.ui.adapter.recyclerView.HorizontalListMovie
import com.stenleone.stanleysfilm.ui.adapter.recyclerView.StudiosListRecycler
import com.stenleone.stanleysfilm.ui.fragment.base.BaseFragment
import com.stenleone.stanleysfilm.util.anim.ButtonTextColorAnimator
import com.stenleone.stanleysfilm.util.constant.BindingConstant
import com.stenleone.stanleysfilm.util.extencial.copyToClipBoard
import com.stenleone.stanleysfilm.util.extencial.throttleClicks
import com.stenleone.stanleysfilm.util.extencial.throttleFirst
import com.stenleone.stanleysfilm.viewModel.masterDetails.DialogControllerViewModel
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
    private val dialogControllerViewModel: DialogControllerViewModel by activityViewModels()
    private lateinit var buttonColorAnim: ButtonTextColorAnimator

    @Inject
    lateinit var genreAdapter: GenreListRecycler

    @Inject
    lateinit var recomendedMovieAdapter: HorizontalListMovie

    @Inject
    lateinit var studioAdapter: StudiosListRecycler

    @Inject
    lateinit var sharedPreferencesSortMainManager: SharedPreferencesSortMainManager

    @Inject
    lateinit var analyticsManagers: FirebaseAnalyticsManagers

    override fun setupBinding(inflater: LayoutInflater, container: ViewGroup?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_film, container, false)
        return binding.root
    }

    override fun setup(savedInstanceState: Bundle?) {

        setupArgs()
        setupClicks()
        setupViewModelCallBack()
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

    private fun setupRecyclerView() {

        val filmClickListener = object : ItemClick {
            override fun click(item: Parcelable) {
                if (item is Movie) {
                    findNavController().navigate(
                        FilmFragmentDirections.actionGlobalFilmFragment(item)
                    )
                }
            }
        }

        binding.apply {
            genreRecycler.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
            genreRecycler.adapter = genreAdapter
            recomendedMovieAdapter.listener = filmClickListener
            recyclerRecomendedList.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
            recyclerRecomendedList.adapter = recomendedMovieAdapter
            recyclerStudioList.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
            recyclerStudioList.adapter = studioAdapter

            recomendedMovieAdapter.typeHolder = if (sharedPreferencesSortMainManager.recomendedSortSmall) {
                HorizontalListMovie.TYPE_SMALL
            } else {
                HorizontalListMovie.TYPE_LARGE
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
                if (watchButtonText.text != getString(R.string.watch_online)) {
                    buttonColorAnim.toActive(watchButtonText)
                    watchButtonText.text = getString(R.string.watch_online)
                }
            }
        })
        viewModel.findFilmFilmixController.status.observe(viewLifecycleOwner, {
            if (viewModel.movieUrl.value == null) {
                binding.watchButtonText.text = it
            }
        })
        viewModel.movieDetails.observe(viewLifecycleOwner) {
            binding.movieDetails = it
            genreAdapter.itemList.clear()
            genreAdapter.itemList.addAll(it.genres)

            studioAdapter.itemList.clear()
            studioAdapter.itemList.addAll(it.productionCompanies)

            binding.genreRecycler.adapter?.notifyDataSetChanged()
            binding.recyclerStudioList.adapter?.notifyDataSetChanged()
            binding.genreRecycler.visibility = View.VISIBLE
        }
        viewModel.recomendedMovieList.observe(viewLifecycleOwner) {

            recomendedMovieAdapter.itemList.clear()
            recomendedMovieAdapter.itemList.addAll(it.movies)
            binding.recyclerRecomendedList.adapter?.notifyDataSetChanged()
            binding.genreRecycler.visibility = View.VISIBLE
        }
        viewModel.movieUrl.observe(viewLifecycleOwner) {
            Log.v("112233", it.toString())
        }
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
                    viewModel.movieUrl.value?.let {
                        analyticsManagers.openFilm(navArgs.movie?.title ?: "")
                        (requireActivity() as MainActivity).openVideoFragment(it)
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
                    recomendedMovieAdapter.typeHolder = HorizontalListMovie.TYPE_SMALL
                    recomendedMovieAdapter.notifyDataSetChanged()
                }
                .launchIn(lifecycleScope)

            recomendedSortBig.clicks()
                .throttleFirst(BindingConstant.SMALL_THROTTLE)
                .onEach {
                    sharedPreferencesSortMainManager.recomendedSortSmall = false
                    recomendedMovieAdapter.typeHolder = HorizontalListMovie.TYPE_LARGE
                    recomendedMovieAdapter.notifyDataSetChanged()
                }
                .launchIn(lifecycleScope)

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

            rateMovie.throttleClicks(
                {
                    dialogControllerViewModel.startUnSupportDialog( // TODO: HARDCODE
                        "test", "test", "test",
                        {
                            it?.dismiss()
                        },
                        childFragmentManager
                    )
                }, lifecycleScope
            )
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (this::binding.isInitialized) {
            outState.putInt(VERTICAL_SCROLL_POSITION, binding.nestedScrollContainer.verticalScrollbarPosition)
        }
    }
}