package com.stenleone.stanleysfilm.ui.fragment.search

import android.os.Bundle
import android.os.Parcelable
import android.text.SpannableString
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.stenleone.stanleysfilm.R
import com.stenleone.stanleysfilm.databinding.FragmentSearchBinding
import com.stenleone.stanleysfilm.interfaces.ItemClick
import com.stenleone.stanleysfilm.network.TmdbNetworkConstant
import com.stenleone.stanleysfilm.network.entity.movie.Movie
import com.stenleone.stanleysfilm.ui.adapter.recyclerView.HorizontalListMovie
import com.stenleone.stanleysfilm.ui.fragment.FilmFragmentDirections
import com.stenleone.stanleysfilm.ui.fragment.MoreMovieFragmentDirections
import com.stenleone.stanleysfilm.ui.fragment.base.BaseFragment
import com.stenleone.stanleysfilm.util.constant.BindingConstant
import com.stenleone.stanleysfilm.util.extencial.throttleFirst
import com.stenleone.stanleysfilm.viewModel.network.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.ldralighieri.corbind.view.clicks
import javax.inject.Inject

@AndroidEntryPoint
class SearchFragment : BaseFragment() {

    private lateinit var binding: FragmentSearchBinding
    private val searchViewModel: SearchViewModel by viewModels()

    @Inject
    lateinit var movieAdapter: HorizontalListMovie

    override fun setupBinding(inflater: LayoutInflater, container: ViewGroup?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false)
        return binding.root
    }

    override fun setup(savedInstanceState: Bundle?) {

        setupEditText()
        setupRecycler()
        setupViewModelCallBacks()
        setupClicks()
    }

    private fun setupClicks() {
        binding.apply {
            searchMovieCard.clicks()
                .throttleFirst(BindingConstant.SMALL_THROTTLE)
                .onEach {
                    findNavController().navigate(
                        MoreMovieFragmentDirections.actionGlobalMoreMovieFragment(
                            searchViewModel.searchData.value,
                            TmdbNetworkConstant.LIST_MOVIE_POPULAR,
                            null,
                            (recycler.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition(),
                            SpannableString(getString(R.string.search_movie_by_text, searchEditText.text.toString())).toString()
                        )
                    )
                }
                .launchIn(lifecycleScope)
        }
    }

    private fun setupEditText() {
        binding.apply {
            searchEditText.doOnTextChanged { text, start, before, count ->
                if (text.toString().isNotBlank()) {
                    searchViewModel.search(text.toString())
                }
            }
        }
    }

    private fun setupRecycler() {
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
            movieAdapter.listener = filmClickListener
            recycler.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
            recycler.adapter = movieAdapter
        }
    }

    private fun setupViewModelCallBacks() {
        searchViewModel.searchData.observe(viewLifecycleOwner) {
            movieAdapter.itemList.clear()
            movieAdapter.itemList.addAll(it.movies)
            binding.recycler.adapter?.notifyDataSetChanged()
        }
    }

}