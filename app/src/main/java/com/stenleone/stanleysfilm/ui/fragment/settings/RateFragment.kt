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
import com.stenleone.stanleysfilm.databinding.FragmentRateBinding
import com.stenleone.stanleysfilm.interfaces.ItemClickParcelable
import com.stenleone.stanleysfilm.managers.sharedPrefs.SharedPreferencesManager
import com.stenleone.stanleysfilm.managers.sharedPrefs.SharedPreferencesMessageManager
import com.stenleone.stanleysfilm.network.entity.movie.MovieUI
import com.stenleone.stanleysfilm.ui.adapter.recyclerView.ListMovieAdapter
import com.stenleone.stanleysfilm.ui.dialog.infoDialog.InfoDialog
import com.stenleone.stanleysfilm.ui.fragment.FilmFragmentDirections
import com.stenleone.stanleysfilm.ui.fragment.MoreMovieFragment
import com.stenleone.stanleysfilm.ui.fragment.base.BaseFragment
import com.stenleone.stanleysfilm.util.extencial.throttleClicks
import com.stenleone.stanleysfilm.viewModel.network.RateListViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class RateFragment : BaseFragment() {

    private lateinit var binding: FragmentRateBinding
    private val viewModel: RateListViewModel by viewModels()

    @Inject
    lateinit var favoriteMovieAdapter: ListMovieAdapter

    @Inject
    lateinit var messageShowsManager: SharedPreferencesMessageManager

    @Inject
    lateinit var sharedPreferencesMessageManager: SharedPreferencesManager

    override fun setupBinding(inflater: LayoutInflater, container: ViewGroup?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_rate, container, false)
        return binding.root
    }

    override fun setup(savedInstanceState: Bundle?) {

        if (!messageShowsManager.rateMessageGuestSessionShows) {
            val expaireDateText = sharedPreferencesMessageManager.expiresGuestTokenAt
            val expaireDateFormat =  SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
            val expaireDate = expaireDateFormat.parse(expaireDateText)
            val calendarDate = Calendar.getInstance()
            calendarDate.time = expaireDate

            InfoDialog.show(
                childFragmentManager,
                getString(R.string.guest_title_message_list),
                getString(R.string.guest_sub_title_message_list, expaireDateText),
                getString(R.string.ok)
            )
            messageShowsManager.rateMessageGuestSessionShows = true
        }

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
        viewModel.movie.observe(viewLifecycleOwner) {
            favoriteMovieAdapter.itemList.clear()
            favoriteMovieAdapter.itemList.addAll(it)
            favoriteMovieAdapter.notifyDataSetChanged()
        }
    }

    override fun onResume() {
        viewModel.getMovies()
        super.onResume()
    }
}