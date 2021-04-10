package com.stenleone.stanleysfilm.managers.sharedPrefs

import android.content.Context
import androidx.core.content.edit
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SharedPreferencesMessageManager @Inject constructor(@ApplicationContext context: Context) {

    companion object {
        private const val SHARED_PREFERENCES_NAME = "stanley`s_film.message_sPref"
        private const val SHOW_GUEST_MESSAGE = "SHOW_GUEST_MESSAGE"
    }

    private val sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)

    var rateMessageGuestSessionShows: Boolean
        get() {
            return sharedPreferences.getBoolean(SHOW_GUEST_MESSAGE, false)
        }
        set(value) {
            sharedPreferences.edit {
                putBoolean(SHOW_GUEST_MESSAGE, value)
            }
        }


}