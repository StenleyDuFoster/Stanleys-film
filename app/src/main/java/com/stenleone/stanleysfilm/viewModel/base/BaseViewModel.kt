package com.stenleone.stanleysfilm.viewModel.base

import androidx.lifecycle.ViewModel
import com.stenleone.stanleysfilm.util.extencial.disposeWithCheck
import io.reactivex.disposables.CompositeDisposable

abstract class BaseViewModel : ViewModel() {

    protected val compositeDisposable = CompositeDisposable()

    override fun onCleared() {
        compositeDisposable.disposeWithCheck()
        super.onCleared()
    }
}