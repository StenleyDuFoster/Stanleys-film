package com.stenleone.stanleysfilm.managers.firebase

import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class FirebaseCloudFirestoreManagers @Inject constructor() {

    companion object {
        const val FILMIX = "filmix"
        const val MOVIE = "movie"
    }

    private val store by lazy { FirebaseFirestore.getInstance() }

    fun getMovieUrl(title: String, successGet: (String) -> Unit) {
        store.collection(FILMIX).document(title)
            .get()
            .addOnSuccessListener {
                if (it.getString(MOVIE) != null) {
                    successGet((it.data?.get(MOVIE) as? String?) ?: "")
                }
            }
    }

    fun setMovieUrl(title: String, url: String) {
        val hashMap = HashMap<String, String>()
            .also {
                it.put(MOVIE, url)
            }
        store.collection(FILMIX).document(title)
            .set(hashMap)
    }

}