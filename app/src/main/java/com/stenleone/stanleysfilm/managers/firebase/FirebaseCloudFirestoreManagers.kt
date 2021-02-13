package com.stenleone.stanleysfilm.managers.firebase

import android.content.Context
import android.provider.Settings
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.Source
import com.google.firebase.firestore.core.UserData
import com.stenleone.stanleysfilm.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseCloudFirestoreManagers @Inject constructor(@ApplicationContext val context: Context) {

    companion object {
        const val FILMIX_MOVIE = "filmix-movie"
        const val MOVIE = "movie"
        const val USERS = "users"
    }

    private val store by lazy { FirebaseFirestore.getInstance() }
    private val userId by lazy { Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID) }

    fun getMovieUrl(title: String, date: String, successGet: (String) -> Unit) {
        store.collection(FILMIX_MOVIE).document(title + date)
            .get()
            .addOnSuccessListener {
                if (it.getString(MOVIE) != null) {
                    successGet((it.data?.get(MOVIE) as? String?) ?: "")
                }
            }
    }

    fun setMovieUrl(title: String, date: String, url: String) {
        val hashMap = HashMap<String, String>()
            .also {
                it[MOVIE] = url
            }
        store.collection(FILMIX_MOVIE).document(title + date)
            .set(hashMap)
    }

    fun getFavorite(movieId: String, success: (movieIdList: HashMap<String, String>) -> Unit, failure: () -> Unit) {
        store.collection(USERS).document(userId).get().addOnSuccessListener {
            success.invoke(it.data as? HashMap<String, String>? ?: hashMapOf())
        }
            .addOnFailureListener {
                failure.invoke()
            }
    }

    fun removeFromFavorite(movieId: String, userMovies: HashMap<String, String>, success: () -> Unit, failure: () -> Unit) {
        val hashMap: Map<String, String> = userMovies
            (hashMap as HashMap<String, String>).remove("$MOVIE${(userMovies.size)}") // add new movie to list

            if (hashMap.size > 0) {
                store.collection(USERS).document(userId).update(hashMap)
                    .addOnSuccessListener {
                        success.invoke()
                    }
                    .addOnFailureListener {
                        failure.invoke()
                    }
            } else {
                store.collection(USERS).document(userId).set(hashMap)
                    .addOnSuccessListener {
                        success.invoke()
                    }
                    .addOnFailureListener {
                        failure.invoke()
                    }
            }

    }

    fun addToFavorite(movieId: String, userMovies: HashMap<String, String>, success: () -> Unit, failure: () -> Unit) {
        val hashMap: Map<String, String> = userMovies
        if (!userMovies.values.contains(movieId)) {
            (hashMap as HashMap<String, String>)["$MOVIE${(userMovies.size)}"] = movieId // add new movie to list

            if (userMovies.size > 0) {
                store.collection(USERS).document(userId).update(hashMap)
                    .addOnSuccessListener {
                        success.invoke()
                    }
                    .addOnFailureListener {
                        failure.invoke()
                    }
            } else {
                store.collection(USERS).document(userId).set(hashMap)
                    .addOnSuccessListener {
                        success.invoke()
                    }
                    .addOnFailureListener {
                        failure.invoke()
                    }
            }
        }
    }

}