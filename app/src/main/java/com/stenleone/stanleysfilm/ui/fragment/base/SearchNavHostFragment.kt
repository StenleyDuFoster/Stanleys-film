package com.stenleone.stanleysfilm.ui.fragment.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.stenleone.stanleysfilm.R
import com.stenleone.stanleysfilm.databinding.FragmentSearchNavHostBinding
import com.stenleone.stanleysfilm.interfaces.FragmentWithNavController

class SearchNavHostFragment : BaseFragment(), FragmentWithNavController {

    companion object {
        lateinit var navController: NavController
    }

    override fun getNavController(): NavController {
        return navController
    }

    private lateinit var binding: FragmentSearchNavHostBinding

    override fun setupBinding(inflater: LayoutInflater, container: ViewGroup?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search_nav_host, container, false)
        return binding.root
    }

    override fun setup(savedInstanceState: Bundle?) {
        binding.apply {
            navController = Navigation.findNavController(requireActivity(), R.id.navHostSearchFragment)
        }
    }
}