package com.stenleone.stanleysfilm.ui.activity.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

abstract class BaseActivity : AppCompatActivity() {

    companion object {
        const val COPY_LABEL = "label"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setup()
    }

    protected abstract fun setup()

    override fun onDestroy() {
        super.onDestroy()
    }
}