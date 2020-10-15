package com.stenleone.stanleysfilm.application

import android.content.Context
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import com.stenleone.stanleysfilm.di.applicationComponent
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : MultiDexApplication() {

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(applicationContext)
    }

    override fun onCreate() {
        super.onCreate()

        setupKoin()
    }

    private fun setupKoin() {
        startKoin {
            androidContext(applicationContext)
            modules(applicationComponent)
        }
    }
}