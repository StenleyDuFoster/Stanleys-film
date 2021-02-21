package com.stenleone.stanleysfilm.ui.fragment.settings

import android.content.res.Configuration
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.stenleone.stanleysfilm.R
import com.stenleone.stanleysfilm.databinding.FragmentFavoriteBinding
import com.stenleone.stanleysfilm.interfaces.ItemClickParcelable
import com.stenleone.stanleysfilm.network.entity.movie.MovieUI
import com.stenleone.stanleysfilm.ui.adapter.recyclerView.ListMovieAdapter
import com.stenleone.stanleysfilm.ui.fragment.FilmFragmentDirections
import com.stenleone.stanleysfilm.ui.fragment.MoreMovieFragment
import com.stenleone.stanleysfilm.ui.fragment.base.BaseFragment
import com.stenleone.stanleysfilm.util.extencial.throttleClicks
import com.stenleone.stanleysfilm.viewModel.network.FavoriteViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class FavoriteFragment : BaseFragment() {

    private lateinit var binding: FragmentFavoriteBinding
    private val viewModel: FavoriteViewModel by viewModels()

    @Inject
    lateinit var favoriteMovieAdapter: ListMovieAdapter

    override fun setupBinding(inflater: LayoutInflater, container: ViewGroup?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_favorite, container, false)
        return binding.root
    }

    override fun setup(savedInstanceState: Bundle?) {
        setupClicks()
        setupRecycler()
        setupViewModel()
    }

    private fun setupClicks() {
        binding.apply {
            backButton.throttleClicks(
                {
                    requireActivity().onBackPressed()
                }, lifecycleScope
            )
        }
    }

    private fun setupRecycler() {
        val clickListener = object : ItemClickParcelable {
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
                MoreMovieFragment.LANDSCAPE_SPAN_COUNT
            } else {
                MoreMovieFragment.PORTRAIT_SPAN_COUNT
            }
            favoriteList.layoutManager = GridLayoutManager(requireContext(), spanCount)
            favoriteList.adapter = favoriteMovieAdapter
            favoriteMovieAdapter.listener = clickListener
        }
    }

    private fun setupViewModel() {
        viewModel.favoriteMovies.observe(viewLifecycleOwner) {
            favoriteMovieAdapter.itemList.clear()
            favoriteMovieAdapter.itemList.addAll(it)
            favoriteMovieAdapter.notifyDataSetChanged()
        }
    }

    override fun onResume() {
        viewModel.getFavoriteMovies()
        super.onResume()
    }
}