package com.stenleone.stanleysfilm.managers.sharedPrefs

import android.content.Context
import androidx.core.content.edit
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SharedPreferencesSortMainManager @Inject constructor(@ApplicationContext context: Context) {

    companion object {
        private const val SHARED_PREFERENCES_NAME = "stanley`s_film.sort_sPref"

        private const val TOP_RATED = "top_rated_sort"
        private const val POPULAR = "popular_sort"
        private const val UPCOMING = "upcoming_sort"
        private const val NOW_PLAYING = "now_playing_sort"
        private const val RECOMENDED = "recomended_sort"
    }

    private val sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)

    var topMovieSortSmall: Boolean
        get() {
            return sharedPreferences.getBoolean(TOP_RATED, true)
        }
        set(value) {
            sharedPreferences.edit {
                putBoolean(TOP_RATED, value)
            }
        }

    var popularSmallSort: Boolean
        get() {
            return sharedPreferences.getBoolean(POPULAR, true)
        }
        set(value) {
            sharedPreferences.edit {
                putBoolean(POPULAR, value)
            }
        }

    var upcomingSortSmall: Boolean
        get() {
            return sharedPreferences.getBoolean(UPCOMING, true)
        }
        set(value) {
            sharedPreferences.edit {
                putBoolean(UPCOMING, value)
            }
        }

    var nowPlayingSortSmall: Boolean
        get() {
            return sharedPreferences.getBoolean(NOW_PLAYING, true)
        }
        set(value) {
            sharedPreferences.edit {
                putBoolean(NOW_PLAYING, value)
            }
        }

    var recomendedSortSmall: Boolean
        get() {
            return sharedPreferences.getBoolean(RECOMENDED, true)
        }
        set(value) {
            sharedPreferences.edit {
                putBoolean(RECOMENDED, value)
            }
        }
}