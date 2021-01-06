package com.stenleone.stanleysfilm.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.stenleone.stanleysfilm.R
import com.stenleone.stanleysfilm.databinding.FragmentFilmBinding
import com.stenleone.stanleysfilm.ui.fragment.base.BaseFragment
import com.stenleone.stanleysfilm.util.constant.BindingConstant
import com.stenleone.stanleysfilm.util.extencial.throttleFirst
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.ldralighieri.corbind.view.clicks


class FilmFragment : BaseFragment() {

    private lateinit var binding: FragmentFilmBinding
    private val navArgs: FilmFragmentArgs by navArgs()

    override fun setupBinding(inflater: LayoutInflater, container: ViewGroup?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_film, container, false)
        return binding.root
    }

    override fun setup(savedInstanceState: Bundle?) {

        setupArgs()
        setupClicks()
    }

    private fun setupArgs() {
        binding.apply {
            movie = navArgs.movie
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
        }
    }

}