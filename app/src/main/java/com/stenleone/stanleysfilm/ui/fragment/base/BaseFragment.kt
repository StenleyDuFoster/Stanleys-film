package com.stenleone.stanleysfilm.ui.fragment.base

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.stenleone.stanleysfilm.util.extencial.disposeWithCheck
import io.reactivex.disposables.CompositeDisposable

abstract class BaseFragment : Fragment() {

    protected val compositeDisposable = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return setupBinding(inflater, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setup()
    }

    protected abstract fun setupBinding(inflater: LayoutInflater, container: ViewGroup?): View
    protected abstract fun setup()

    override fun onDestroy() {
        compositeDisposable.disposeWithCheck()
        super.onDestroy()
    }
}