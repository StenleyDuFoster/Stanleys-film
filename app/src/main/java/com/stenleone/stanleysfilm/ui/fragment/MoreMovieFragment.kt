package com.stenleone.stanleysfilm.ui.fragment

import android.content.res.Configuration
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
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.stenleone.stanleysfilm.R
import com.stenleone.stanleysfilm.databinding.FragmentMoreMovieBinding
import com.stenleone.stanleysfilm.interfaces.ItemClickParcelable
import com.stenleone.stanleysfilm.network.entity.movie.MovieUI
import com.stenleone.stanleysfilm.ui.adapter.recyclerView.ListMovieAdapter
import com.stenleone.stanleysfilm.ui.fragment.base.BaseFragment
import com.stenleone.stanleysfilm.util.constant.BindingConstant
import com.stenleone.stanleysfilm.util.extencial.throttleFirst
import com.stenleone.stanleysfilm.viewModel.network.MoreMovieViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.ldralighieri.corbind.view.clicks
import javax.inject.Inject

@AndroidEntryPoint
class MoreMovieFragment : BaseFragment() {

    companion object {
        const val RECYCLER_SAVE_FIRST_VISIBLE_POSITION = "recycler_position"
        const val PORTRAIT_SPAN_COUNT = 3
        const val LANDSCAPE_SPAN_COUNT = 5
        const val PAGINATION_SIZE = 20
    }

    private lateinit var binding: FragmentMoreMovieBinding
    private val navArgs: MoreMovieFragmentArgs by navArgs()
    private val viewModel: MoreMovieViewModel by viewModels()
    @Inject
    lateinit var adapter: ListMovieAdapter

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
        if (viewModel.pageCurrent == null) {
            viewModel.pageCurrent = (navArgs.movieEntity?.page)
        }
        viewModel.getPage(navArgs.searchType ?: "", navArgs.movieRecomendedId)

        viewModel.movieList.observe(viewLifecycleOwner) {
            val oldLastPosition = adapter.itemList.size
            adapter.itemList.addAll(it.movies)
            binding.recycler.adapter?.notifyItemRangeInserted(oldLastPosition, adapter.itemList.size - 1)
        }
    }

    private fun setupRecyclerView() {

        val filmClickListener = object : ItemClickParcelable {
            override fun click(item: Parcelable) {
                if (item is MovieUI) {
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
                    if ((recycler.layoutManager as LinearLayoutManager).findLastVisibleItemPosition() > adapter.itemList.size - PAGINATION_SIZE && viewModel.valLocalProgress == false) {
                        if (viewModel.pageCurrent ?: 0 + 1 < navArgs.movieEntity?.totalPages ?: 0) {
                            viewModel.getPage(navArgs.searchType ?: "", navArgs.movieRecomendedId)
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