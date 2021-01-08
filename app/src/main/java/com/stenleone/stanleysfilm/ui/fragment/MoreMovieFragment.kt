package com.stenleone.stanleysfilm.ui.fragment

import android.content.res.Configuration
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.stenleone.stanleysfilm.R
import com.stenleone.stanleysfilm.databinding.FragmentMoreMovieBinding
import com.stenleone.stanleysfilm.interfaces.ItemClick
import com.stenleone.stanleysfilm.network.entity.movie.Movie
import com.stenleone.stanleysfilm.ui.adapter.recyclerView.HorizontalListMovie
import com.stenleone.stanleysfilm.ui.fragment.base.BaseFragment
import com.stenleone.stanleysfilm.util.constant.BindingConstant
import com.stenleone.stanleysfilm.util.extencial.throttleFirst
import com.stenleone.stanleysfilm.viewModel.network.MoreMovieViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.ldralighieri.corbind.view.clicks

class MoreMovieFragment : BaseFragment() {

    companion object {
        const val RECYCLER_SAVE_FIRST_VISIBLE_POSITION = "recycler_position"
        const val PORTRAIT_SPAN_COUNT = 3
        const val LANDSCAPE_SPAN_COUNT = 5
        const val PAGINATION_SIZE = 20
    }

    private lateinit var binding: FragmentMoreMovieBinding
    private val navArgs: MoreMovieFragmentArgs by navArgs()
    private val viewModel: MoreMovieViewModel by viewModel()
    private val adapter: HorizontalListMovie by inject()

    private var lastLoadedPage = 1

    override fun setupBinding(inflater: LayoutInflater, container: ViewGroup?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_more_movie, container, false)
        return binding.root
    }

    override fun setup(savedInstanceState: Bundle?) {
        setupNavArgs()
        setupRecyclerView()
        setupViewModelCallBack()
        setupClicks()

        if (savedInstanceState != null) {
            binding.apply {
                recycler.scrollToPosition(savedInstanceState.getInt(RECYCLER_SAVE_FIRST_VISIBLE_POSITION, 0))
            }
        } else {
            binding.apply {
                recycler.scrollToPosition(navArgs.lastVisiblePosition)
            }
        }
    }

    private fun setupNavArgs() {
        navArgs.title?.let {
            binding.apply {
                title = it
            }
        }
    }

    private fun setupViewModelCallBack() {
        viewModel.getPage(navArgs.searchType ?: "", 2)
        lastLoadedPage = 2

        viewModel.movieList.observe(viewLifecycleOwner, {
            val oldLastPosition = adapter.itemList.size
            adapter.itemList.addAll(it.movies)
            binding.recycler.adapter?.notifyItemRangeInserted(oldLastPosition, adapter.itemList.size - 1)
        })
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
            val spanCount: Int = if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                LANDSCAPE_SPAN_COUNT
            } else {
                PORTRAIT_SPAN_COUNT
            }
            adapter.itemList.addAll(navArgs.movieEntity?.movies ?: arrayListOf())
            recycler.layoutManager = GridLayoutManager(requireContext(), spanCount)
            recycler.adapter = adapter
            adapter.listener = filmClickListener

            recycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {

                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if ((recycler.layoutManager as LinearLayoutManager).findLastVisibleItemPosition() > adapter.itemList.size - PAGINATION_SIZE && viewModel.inProgress.value != true) {
                        if (lastLoadedPage + 1 <= navArgs.movieEntity?.totalPages ?: 0) {
                            lastLoadedPage++
                            viewModel.getPage(navArgs.searchType ?: "", lastLoadedPage)
                        }
                    }
                }
            })
        }
    }

    private fun setupClicks() {
        binding.apply {
            backButton.clicks()
                .throttleFirst(BindingConstant.SMALL_THROTTLE)
                .onEach {
                    requireActivity().onBackPressed()
                }
                .launchIn(lifecycleScope)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(
            RECYCLER_SAVE_FIRST_VISIBLE_POSITION,
            (binding.recycler.layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition()
        )
    }
}