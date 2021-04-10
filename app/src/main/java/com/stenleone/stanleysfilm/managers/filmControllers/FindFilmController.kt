package com.stenleone.stanleysfilm.managers.filmControllers.filmix

import androidx.lifecycle.MutableLiveData

interface FindFilmController {

    val progress: MutableLiveData<Int>
    val status: MutableLiveData<String>

    fun start(
        titleMovie: String,
        dateMovie: String,
        loadVideoCallBack: (url: String?) -> Unit
    )

    fun onDestroy()
}