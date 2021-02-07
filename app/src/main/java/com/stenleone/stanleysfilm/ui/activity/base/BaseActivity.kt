package com.stenleone.stanleysfilm.ui.activity.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint

abstract class BaseActivity : AppCompatActivity() {

    companion object {
        const val COPY_LABEL = "label"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setup()
    }

    protected abstract fun setup()
}