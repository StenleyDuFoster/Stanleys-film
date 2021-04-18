package com.stenleone.stanleysfilm.managers.firebase

import android.content.Context
import android.provider.Settings
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.Source
import com.google.firebase.firestore.core.UserData
import com.stenleone.stanleysfilm.R
import com.stenleone.stanleysfilm.managers.filmControllers.FilmControllerEnum
import com.stenleone.stanleysfilm.model.entity.FilmUrlData
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseCloudFirestoreManagers @Inject constructor(@ApplicationContext val context: Context) {

    companion object {
        const val FILMIX_MOVIE = "filmix-movie"
        const val HD_REZKA_MOVIE = "hd_rezka-movie"
        const val MOVIE = "movie"
        const val MOVIE_360 = "movie_360"
        const val MOVIE_480 = "movie_480"
        const val MOVIE_720 = "movie_720"
        const val MOVIE_1080 = "movie_1080"
        const val MOVIE_2060 = "movie_2060"
        const val USERS = "users"
    }

    private val store by lazy { FirebaseFirestore.getInstance() }
    private val userId by lazy { Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID) }

    fun getMovieUrl(title: String, date: String, provider: FilmControllerEnum, successGet: (String) -> Unit) {

        val urlProvider = getUrlNameByProviderType(provider)

        store.collection(urlProvider).document(title + date)
            .get()
            .addOnSuccessListener {
                if (it.getString(MOVIE) != null) {
                    successGet((it.data?.get(MOVIE) as? String?) ?: "")
                }
            }
    }

    private fun getUrlNameByProviderType(provider: FilmControllerEnum): String {
        return when (provider) {
            FilmControllerEnum.FILMIX -> FILMIX_MOVIE
            FilmControllerEnum.HD_REZKA -> HD_REZKA_MOVIE
        }
    }

    fun setMovieUrl(title: String, date: String, filmUrlData: FilmUrlData) {
        val hashMap = HashMap<String, String>()
            .also {
                filmUrlData.getNonNullUrlOrNull()?.let { url ->
                    it[MOVIE] = url
                }
            }

        val urlProvider = getUrlNameByProviderType(filmUrlData.provider)

        store.collection(urlProvider).document(title + date)
            .set(hashMap)
    }

    fun getFavorite(success: (userData: HashMap<String, ArrayList<String>>) -> Unit, failure: () -> Unit) {
        store.collection(USERS).document(userId).get().addOnSuccessListener {
            if (it.data != null) {
                success.invoke((it.data as? HashMap<String, ArrayList<String>>? ?: hashMapOf()))
            } else {
                success.invoke((it.data as? HashMap<String, ArrayList<String>>? ?: hashMapOf()))
            }
        }
            .addOnFailureListener {
                failure.invoke()
            }
    }

    fun removeFromFavorite(
        movieId: String,
        userMovies: HashMap<String, ArrayList<String>>,
        success: (HashMap<String, ArrayList<String>>) -> Unit,
        failure: () -> Unit
    ) {
        val moviesList = userMovies.get(MOVIE) ?: arrayListOf()

        var keyRemove: String? = null
        moviesList.forEach {
            if (movieId == it) {
                keyRemove = it
            }
        }

        keyRemove?.let {
            moviesList.remove(it)
            val map = HashMap<String, ArrayList<String>>()
            map.put(MOVIE, moviesList)

            if (userMovies.size > 0) {
                store.collection(USERS).document(userId).set(map)
                    .addOnSuccessListener {
                        success.invoke(map)
                    }
                    .addOnFailureListener {
                        failure.invoke()
                    }
                    .addOnCanceledListener(failure)
            } else {
                store.collection(USERS).document(userId).set(map)
                    .addOnSuccessListener {
                        success.invoke(map)
                    }
                    .addOnFailureListener {
                        failure.invoke()
                    }
                    .addOnCanceledListener(failure)
            }
        }
    }

    fun addToFavorite(
        movieId: String,
        userData: HashMap<String, ArrayList<String>>,
        success: (HashMap<String, ArrayList<String>>) -> Unit,
        failure: () -> Unit
    ) {
        val moviesList = userData.get(MOVIE) ?: arrayListOf()

        if (userData.size > 0) {
            moviesList.add(movieId)  // add new movie to list
            val map = HashMap<String, ArrayList<String>>()
            map.put(MOVIE, moviesList)
            store.collection(USERS).document(userId).update(map as Map<String, ArrayList<String>>)
                .addOnSuccessListener {
                    success.invoke(map)
                }
                .addOnFailureListener {
                    failure.invoke()
                }
                .addOnCanceledListener(failure)
        } else {
            moviesList.add(movieId)  // add new movie to list
            val map = HashMap<String, ArrayList<String>>()
            map.put(MOVIE, moviesList)
            moviesList.add(movieId) // add new movie to list
            store.collection(USERS).document(userId).set(map)
                .addOnSuccessListener {
                    success.invoke(map)
                }
                .addOnFailureListener {
                    failure.invoke()
                }
                .addOnCanceledListener(failure)
        }
    }

}