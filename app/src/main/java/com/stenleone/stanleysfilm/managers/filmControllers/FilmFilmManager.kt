package com.stenleone.stanleysfilm.managers.filmControllers.filmix

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.stenleone.stanleysfilm.managers.filmControllers.FilmControllerEnum
import com.stenleone.stanleysfilm.managers.filmControllers.hdRezka.FindFilmHdRezkaController
import com.stenleone.stanleysfilm.managers.firebase.FirebaseRemoteConfigManager
import com.stenleone.stanleysfilm.managers.sharedPrefs.SharedPreferencesFilmControllerManager
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class FilmFilmManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val firebaseRemoteConfigManager: FirebaseRemoteConfigManager,
    private val sharedPreferencesFilmControllerManager: SharedPreferencesFilmControllerManager
) {

    private lateinit var findFilmController: FindFilmController

    companion object {
        const val FAILED_FIND = -1
        const val USER_AGENT_WEB_VIEW = "Chrome/41.0.2228.0 Safari/537.36"
        const val MAX_FOUND_WITH_DATE_TRY = 3
        const val MAX_TRY = 6
    }

    fun getFilmUrl(
        titleMovie: String,
        dateMovie: String,
        loadVideoCallBack: (url: String?) -> Unit
    ) {

        if (!this::findFilmController.isInitialized) {
            setupFilmController()
        }

        findFilmController.start(titleMovie, dateMovie, loadVideoCallBack)
    }

    fun getProgress(): MutableLiveData<Int> {
        return findFilmController.progress
    }

    fun getStatus(): MutableLiveData<String> {
        return findFilmController.status
    }

    fun onDestroy() {
        findFilmController.onDestroy()
    }

    private fun setupFilmController() {

        when (sharedPreferencesFilmControllerManager.selectedFilmController) {
            FilmControllerEnum.FILMIX -> {
                findFilmController = FindFilmFilmixController(context, firebaseRemoteConfigManager)
            }
            FilmControllerEnum.HD_REZKA -> {
                findFilmController = FindFilmHdRezkaController(context, firebaseRemoteConfigManager)
            }
        }

    }
}