package com.stenleone.stanleysfilm.ui.fragment.settings

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.stenleone.stanleysfilm.R
import com.stenleone.stanleysfilm.databinding.FragmentSettingsBinding
import com.stenleone.stanleysfilm.ui.fragment.base.BaseFragment

class SettingsFragment : BaseFragment() {

    private lateinit var binding: FragmentSettingsBinding

    override fun setupBinding(inflater: LayoutInflater, container: ViewGroup?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_settings, container, false)
        return binding.root
    }

    override fun setup() {

    }

}