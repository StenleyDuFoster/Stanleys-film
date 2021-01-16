package com.stenleone.stanleysfilm.managers.firebase

import javax.inject.Singleton
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.ktx.Firebase
import javax.inject.Inject

@Singleton
class FirebaseAnalyticsManagers @Inject constructor() {

    companion object {
        const val OPEN_FILM = "open_film"
        const val FILM_NAME = "film_name"
    }

    private val analytics by lazy { Firebase.analytics }

    fun openFilm(title: String) {
        analytics.logEvent(OPEN_FILM) {
            param(FILM_NAME, title)
        }
    }

}