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
import com.stenleone.stanleysfilm.managers.filmControllers.FilmControllerEnum
import com.stenleone.stanleysfilm.managers.sharedPrefs.SharedPreferencesFilmControllerManager
import com.stenleone.stanleysfilm.ui.adapter.recyclerView.ListMovieAdapter
import com.stenleone.stanleysfilm.ui.fragment.base.BaseFragment
import com.stenleone.stanleysfilm.util.anim.ButtonTextColorAnimator
import com.stenleone.stanleysfilm.util.extencial.throttleClicks
import com.stenleone.stanleysfilm.viewModel.network.FavoriteViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SettingsFragment : BaseFragment() {

    private lateinit var binding: FragmentSettingsBinding
    private val viewModel: FavoriteViewModel by viewModels()

    @Inject
    lateinit var sharedPreferencesFilmControllerManager: SharedPreferencesFilmControllerManager

    private lateinit var buttonColorAnim: ButtonTextColorAnimator

    override fun setupBinding(inflater: LayoutInflater, container: ViewGroup?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_settings, container, false)
        return binding.root
    }

    override fun setup(savedInstanceState: Bundle?) {
        setupDefaultValues()
        setupClicks()
    }

    private fun setupDefaultValues() {
        binding.apply {

            buttonColorAnim = ButtonTextColorAnimator(requireContext(), arrayListOf())

            when (sharedPreferencesFilmControllerManager.selectedFilmController) {
                FilmControllerEnum.HD_REZKA -> {
                    buttonColorAnim.click(hdRezkaProviderText, false)
                }
                FilmControllerEnum.FILMIX -> {
                    buttonColorAnim.click(filmixProviderText, false)
                }
            }
        }
    }

    private fun setupClicks() {
        binding.apply {
            favoriteButton.throttleClicks(
                { findNavController().navigate(R.id.favoriteFragment) }, lifecycleScope
            )
            rateButton.throttleClicks(
                { findNavController().navigate(R.id.rateFragment) }, lifecycleScope
            )
            hdRezkaProviderButton.throttleClicks(
                {
                    buttonColorAnim.clickAndInActiveOther(hdRezkaProviderText, false)
                    sharedPreferencesFilmControllerManager.selectedFilmController = FilmControllerEnum.HD_REZKA
                }, lifecycleScope
            )
            filmixProviderButton.throttleClicks(
                {
                    buttonColorAnim.clickAndInActiveOther(filmixProviderText, false)
                    sharedPreferencesFilmControllerManager.selectedFilmController = FilmControllerEnum.FILMIX
                }, lifecycleScope
            )
        }
    }
}