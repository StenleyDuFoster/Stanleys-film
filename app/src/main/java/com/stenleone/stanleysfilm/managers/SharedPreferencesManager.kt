package com.stenleone.stanleysfilm.managers

import android.content.Context
import androidx.core.content.edit

class SharedPreferencesManager(context: Context) {

    companion object {
        const val SHARED_PREFERENCES_NAME = "stanley`s_film.sPref"

        const val LANGUAGE = "language"
        const val GUEST_SESSION_TOKEN = "guest_session_token"
        const val GUEST_EXPIRES_TOKEN = "guest_expires_token"
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

    var guestSessionToken: String?
        get() {
            return sharedPreferences.getString(GUEST_SESSION_TOKEN, null)
        }
        set(value) {
            sharedPreferences.edit {
                putString(GUEST_SESSION_TOKEN, value)
            }
        }

    var expiresGuestTokenAt: String?
        get() {
            return sharedPreferences.getString(GUEST_EXPIRES_TOKEN, null)
        }
        set(value) {
            sharedPreferences.edit {
                putString(GUEST_EXPIRES_TOKEN, value)
            }
        }
}