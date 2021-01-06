package com.stenleone.stanleysfilm.ui.fragment.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.stenleone.stanleysfilm.R
import com.stenleone.stanleysfilm.databinding.FragmentSearchBinding
import com.stenleone.stanleysfilm.ui.fragment.base.BaseFragment

class SearchFragment : BaseFragment() {

    private lateinit var binding: FragmentSearchBinding

    override fun setupBinding(inflater: LayoutInflater, container: ViewGroup?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false)
        return binding.root
    }

    override fun setup(savedInstanceState: Bundle?) {

    }

}