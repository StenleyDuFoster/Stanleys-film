package com.stenleone.stanleysfilm.viewModel.network

import android.annotation.SuppressLint
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import com.stenleone.stanleysfilm.managers.ConnectionManager
import com.stenleone.stanleysfilm.managers.sharedPrefs.SharedPreferencesManager
import com.stenleone.stanleysfilm.model.entity.RequestError
import com.stenleone.stanleysfilm.network.ApiService
import com.stenleone.stanleysfilm.network.TmdbNetworkConstant.LIST_MOVIE_NOW_PLAYING
import com.stenleone.stanleysfilm.network.TmdbNetworkConstant.LIST_MOVIE_POPULAR
import com.stenleone.stanleysfilm.network.TmdbNetworkConstant.LIST_MOVIE_TOP_RATED
import com.stenleone.stanleysfilm.network.TmdbNetworkConstant.LIST_MOVIE_TOP_UPCOMING
import com.stenleone.stanleysfilm.network.entity.lates.LatesEntity
import com.stenleone.stanleysfilm.network.entity.movie.MoviesEntity
import com.stenleone.stanleysfilm.util.extencial.successOrError
import com.stenleone.stanleysfilm.viewModel.base.BaseViewModel
import java.text.SimpleDateFormat
import java.util.*


class MainViewModel @ViewModelInject constructor(
    apiService: ApiService,
    sharedPreferencesManager: SharedPreferencesManager,
    connectionManager: ConnectionManager
) : BaseViewModel(apiService, sharedPreferencesManager, connectionManager) {

    val movieLatesLiveData = MutableLiveData<LatesEntity>()
    val movieNowPlayingLiveData = MutableLiveData<MoviesEntity>()
    val moviePopularLiveData = MutableLiveData<MoviesEntity>()
    val movieTopRatedLiveData = MutableLiveData<MoviesEntity>()
    val movieUpcomingLiveData = MutableLiveData<MoviesEntity>()

    init {
        setupGuestSessionToken()
        loadContent()
    }

    fun loadContent() {
        getLatesMovie()
        getListMovie(LIST_MOVIE_NOW_PLAYING)
        getListMovie(LIST_MOVIE_POPULAR)
        getListMovie(LIST_MOVIE_TOP_RATED)
        getListMovie(LIST_MOVIE_TOP_UPCOMING)
    }

    private fun getLatesMovie(page: Int = 1) {
        doAsyncRequest {
            apiService.getLatesMovieAsync(
                language = sharedPreferencesManager.language,
                page = page
            )
                .await()
                .successOrError(
                    success = {
                        movieLatesLiveData.postValue(it)
                    }, {
                        isFailure(RequestError.UNSUCCESS_STATUS, it)
                    }
                )
        }
    }

    private fun getListMovie(typeList: String, page: Int = 1) {
        doAsyncRequest {
            apiService.getListMovieAsync(
                typeList,
                language = sharedPreferencesManager.language,
                page = page
            )
                .await()
                .successOrError(
                    success = {
                        postValue(typeList, it)
                    }, {
                        isFailure(RequestError.UNSUCCESS_STATUS, it)
                    }
                )
        }
    }

    private fun postValue(
        type: String,
        movies: MoviesEntity
    ) {
        when (type) {
            LIST_MOVIE_NOW_PLAYING -> {
                movieNowPlayingLiveData.postValue(movies)
            }
            LIST_MOVIE_POPULAR -> {
                moviePopularLiveData.postValue(movies)
            }
            LIST_MOVIE_TOP_RATED -> {
                movieTopRatedLiveData.postValue(movies)
            }
            LIST_MOVIE_TOP_UPCOMING -> {
                movieUpcomingLiveData.postValue(movies)
            }
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun setupGuestSessionToken() {

        val sdf = SimpleDateFormat("yyyy-mm-dd hh:mm:ss")
        val currentDate = sdf.format(Date())

        if (sharedPreferencesManager.expiresGuestTokenAt != null) {
            val tokenDate = sdf.format(sdf.parse(sharedPreferencesManager.expiresGuestTokenAt ?: "0"))

            if (currentDate < tokenDate) {
                getNewGuestToken()
            }
        } else {
            getNewGuestToken()
        }

    }

    private fun getNewGuestToken() {
        doAsyncRequest {
            apiService.getSessionAsync()
                .await()
                .successOrError(
                    success = {
                        if (it.success) {
                            sharedPreferencesManager.guestSessionToken = it.guestSessionId //2021-01-08 17:53:54 UTC
                            sharedPreferencesManager.expiresGuestTokenAt = it.expiresAt
                        }
                    }, {
                        isFailure(RequestError.UNSUCCESS_STATUS, it)
                    }
                )
        }
    }
}