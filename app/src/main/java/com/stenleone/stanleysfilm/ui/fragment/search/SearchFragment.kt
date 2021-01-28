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
import com.stenleone.stanleysfilm.interfaces.ItemClickParcelable
import com.stenleone.stanleysfilm.network.TmdbNetworkConstant
import com.stenleone.stanleysfilm.network.entity.movie.MovieUI
import com.stenleone.stanleysfilm.ui.adapter.recyclerView.ListMovieAdapter
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
    lateinit var movieAdapterAdapter: ListMovieAdapter

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
                            TmdbNetworkConstant.SEARCH_MOVIE,
                            searchEditText.text.toString(),
                            (recycler.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition(),
                            SpannableString(getString(R.string.search_movie_by_text, searchEditText.text.toString())).toString()
                        )
                    )
                }
                .launchIn(lifecycleScope)

            clearEditText.clicks()
                .throttleFirst(BindingConstant.SMALL_THROTTLE)
                .onEach {
                    searchEditText.text.clear()
                }
                .launchIn(lifecycleScope)
        }
    }

    private fun setupEditText() {
        binding.apply {
            searchEditText.doOnTextChanged { text, start, before, count ->
                if (text.toString().isNotBlank()) {
                    clearEditText.visibility = View.VISIBLE
                    searchViewModel.search(text.toString())
                } else {
                    clearEditText.visibility = View.GONE
                    searchMovieCard.visibility = View.GONE
                }
            }
        }
    }

    private fun setupRecycler() {
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
            movieAdapterAdapter.listener = filmClickListener
            recycler.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
            recycler.adapter = movieAdapterAdapter
        }
    }

    private fun setupViewModelCallBacks() {
        searchViewModel.searchData.observe(viewLifecycleOwner) {

            binding.apply {
                if (it.movies.size > 0) {
                    searchNull.visibility = View.GONE
                    searchMovieCard.visibility = View.VISIBLE
                } else {
                    searchNull.visibility = View.VISIBLE
                    searchMovieCard.visibility = View.GONE
                }
            }

            movieAdapterAdapter.itemList.clear()
            movieAdapterAdapter.itemList.addAll(it.movies)
            binding.recycler.adapter?.notifyDataSetChanged()
        }
    }

}