package com.stenleone.stanleysfilm.ui.fragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.stenleone.stanleysfilm.R
import com.stenleone.stanleysfilm.databinding.FragmentMainBinding
import com.stenleone.stanleysfilm.ui.fragment.base.BaseFragment

class MainFragment : BaseFragment() {

    private lateinit var binding: FragmentMainBinding

    override fun setupBinding(inflater: LayoutInflater, container: ViewGroup?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false)
        return binding.root
    }

    override fun setup() {

    }

}