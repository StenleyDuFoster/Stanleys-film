package com.stenleone.stanleysfilm.ui.fragment.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.stenleone.stanleysfilm.R
import com.stenleone.stanleysfilm.databinding.FragmentMainNavHostBinding
import com.stenleone.stanleysfilm.interfaces.FragmentWithNavController

class MainNavHostFragment : BaseFragment(), FragmentWithNavController {

    private lateinit var binding: FragmentMainNavHostBinding
    override lateinit var navController: NavController

    override fun setupBinding(inflater: LayoutInflater, container: ViewGroup?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main_nav_host, container, false)
        return binding.root
    }

    override fun setup(savedInstanceState: Bundle?) {
        binding.apply {
            navController = Navigation.findNavController(requireActivity(), R.id.navHostMainFragment)
        }
    }
}