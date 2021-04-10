package com.stenleone.stanleysfilm.managers.sharedPrefs

import android.content.Context
import androidx.core.content.edit
import com.stenleone.stanleysfilm.managers.filmControllers.FilmControllerEnum
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SharedPreferencesFilmControllerManager @Inject constructor(@ApplicationContext context: Context) {

    companion object {
        private const val SHARED_PREFERENCES_NAME = "stanley`s_film.filmController_sPref"

        private const val SELECTED_FILM_CONTROLLER = "selected_film_controller"
    }

    private val sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)

    var selectedFilmController: FilmControllerEnum
        get() {
            return when ( sharedPreferences.getString(SELECTED_FILM_CONTROLLER, FilmControllerEnum.HD_REZKA.toString())) {
                FilmControllerEnum.HD_REZKA.toString() -> {
                    FilmControllerEnum.HD_REZKA
                }
                else -> {
                    FilmControllerEnum.FILMIX
                }
            }
        }
        set(value) {
            sharedPreferences.edit {
                putString(SELECTED_FILM_CONTROLLER, value.toString())
            }
        }
}