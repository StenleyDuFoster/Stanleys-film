package com.stenleone.stanleysfilm.ui.fragment.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.stenleone.stanleysfilm.R
import com.stenleone.stanleysfilm.databinding.FragmentSettingsBinding
import com.stenleone.stanleysfilm.ui.adapter.recyclerView.ListMovieAdapter
import com.stenleone.stanleysfilm.ui.fragment.base.BaseFragment
import com.stenleone.stanleysfilm.util.extencial.throttleClicks
import com.stenleone.stanleysfilm.viewModel.network.FavoriteViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SettingsFragment : BaseFragment() {

    private lateinit var binding: FragmentSettingsBinding
    private val viewModel: FavoriteViewModel by viewModels()

    @Inject
    lateinit var favoriteMovieAdapter: ListMovieAdapter

    override fun setupBinding(inflater: LayoutInflater, container: ViewGroup?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_settings, container, false)
        return binding.root
    }

    override fun setup(savedInstanceState: Bundle?) {
        setupClicks()
    }

    private fun setupClicks() {
        binding.apply {
            favoriteButton.throttleClicks(
                { findNavController().navigate(R.id.favoriteFragment) }, lifecycleScope
            )
            rateButton.throttleClicks(
                { findNavController().navigate(R.id.rateFragment) }, lifecycleScope
            )
        }
    }
}