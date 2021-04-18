package com.stenleone.stanleysfilm.managers.filmControllers.filmix

import androidx.lifecycle.MutableLiveData
import com.stenleone.stanleysfilm.model.entity.FilmUrlData

interface FindFilmController {

    val progress: MutableLiveData<Int>
    val status: MutableLiveData<String>

    fun start(
        titleMovie: String,
        dateMovie: String,
        loadVideoCallBack: (url: FilmUrlData?) -> Unit
    )

    fun onDestroy()
}