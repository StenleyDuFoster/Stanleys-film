package com.stenleone.stanleysfilm.ui.fragment.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.stenleone.stanleysfilm.util.extencial.hideKeyboard

abstract class BaseFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return setupBinding(inflater, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setup(savedInstanceState)
        requireActivity().hideKeyboard()
    }

    protected abstract fun setupBinding(inflater: LayoutInflater, container: ViewGroup?): View
    protected abstract fun setup(savedInstanceState: Bundle?)
}