package com.stenleone.stanleysfilm.ui.activity.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.stenleone.stanleysfilm.util.extencial.disposeWithCheck
import io.reactivex.disposables.CompositeDisposable

abstract class BaseActivity : AppCompatActivity() {

    protected val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setup()
    }

    protected abstract fun setup()

    override fun onDestroy() {
        compositeDisposable.disposeWithCheck()
        super.onDestroy()
    }
}