package com.stenleone.stanleysfilm.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.stenleone.stanleysfilm.R
import com.stenleone.stanleysfilm.databinding.FragmentActorBinding
import com.stenleone.stanleysfilm.ui.fragment.base.BaseFragment

class ActorFragment : BaseFragment() {

    private lateinit var binding: FragmentActorBinding

    override fun setupBinding(inflater: LayoutInflater, container: ViewGroup?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_actor, container, false)
        return binding.root
    }

    override fun setup(savedInstanceState: Bundle?) {

    }

}