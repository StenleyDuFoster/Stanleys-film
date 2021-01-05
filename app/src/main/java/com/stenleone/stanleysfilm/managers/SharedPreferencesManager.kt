package com.stenleone.stanleysfilm.managers

import android.content.Context
import androidx.core.content.edit

class SharedPreferencesManager(context: Context) {

    companion object {
        const val SHARED_PREFERENCES_NAME = "stanley`s_film.sPref"

        const val LANGUAGE = "language"
    }

    val sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)

    var language: String
        get() {
            return sharedPreferences.getString(LANGUAGE, "ru").toString()
        }
        set(value) {
            sharedPreferences.edit {
                putString(LANGUAGE, value)
            }
        }
}