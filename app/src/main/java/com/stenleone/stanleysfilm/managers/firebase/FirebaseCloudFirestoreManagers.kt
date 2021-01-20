package com.stenleone.stanleysfilm.managers.firebase

import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class FirebaseCloudFirestoreManagers @Inject constructor() {

    companion object {
        const val FILMIX_MOVIE = "filmix-movie"
        const val MOVIE = "movie"
    }

    private val store by lazy { FirebaseFirestore.getInstance() }

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

}