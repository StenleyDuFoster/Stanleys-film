package com.stenleone.stanleysfilm.managers.sharedPrefs

import android.content.Context
import android.provider.Settings
import androidx.core.content.edit
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SharedPreferencesManager @Inject constructor(@ApplicationContext context: Context) {

    companion object {
        private const val SHARED_PREFERENCES_NAME = "stanley`s_film.main_sPref"

        private const val LANGUAGE = "language"
        private const val GUEST_SESSION_TOKEN = "guest_session_token"
        private const val GUEST_EXPIRES_TOKEN = "guest_expires_token"
    }

    private val sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)

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