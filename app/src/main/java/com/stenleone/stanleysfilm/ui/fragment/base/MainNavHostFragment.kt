package com.stenleone.stanleysfilm.ui.fragment.base

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import com.stenleone.stanleysfilm.R
import com.stenleone.stanleysfilm.databinding.FragmentMainNavHostBinding
import com.stenleone.stanleysfilm.interfaces.FragmentWithNavController
import com.stenleone.stanleysfilm.network.entity.movie.MovieUI
import com.stenleone.stanleysfilm.ui.fragment.FilmFragmentDirections

class MainNavHostFragment : BaseFragment(), FragmentWithNavController {

    override fun getNavControllerId(): Int {
        return R.id.navHostMainFragment
    }

    override fun popToStart() {
        val navOptions = NavOptions.Builder().setPopUpTo(R.id.mainFragment, true).build()
            activity?.let {
            Navigation.findNavController(it, getNavControllerId()).navigate(R.id.mainFragment, null, navOptions)
        }
    }

    private lateinit var binding: FragmentMainNavHostBinding

    override fun setupBinding(inflater: LayoutInflater, container: ViewGroup?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main_nav_host, container, false)
        return binding.root
    }

    override fun setup(savedInstanceState: Bundle?) {  }

    fun openFilm(movie: MovieUI, activity: Activity) {

        Navigation.findNavController(activity, getNavControllerId()).navigate(
            FilmFragmentDirections.actionGlobalFilmFragment(
                movie
            )
        )
    }
}