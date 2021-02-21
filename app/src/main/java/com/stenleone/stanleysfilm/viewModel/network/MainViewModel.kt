package com.stenleone.stanleysfilm.viewModel.network

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.stenleone.stanleysfilm.managers.ConnectionManager
import com.stenleone.stanleysfilm.managers.sharedPrefs.SharedPreferencesManager
import com.stenleone.stanleysfilm.managers.sharedPrefs.SharedPreferencesMessageManager
import com.stenleone.stanleysfilm.model.entity.DataState
import com.stenleone.stanleysfilm.model.entity.RequestError
import com.stenleone.stanleysfilm.network.ApiService
import com.stenleone.stanleysfilm.network.TmdbNetworkConstant.LIST_MOVIE_NOW_PLAYING
import com.stenleone.stanleysfilm.network.TmdbNetworkConstant.LIST_MOVIE_POPULAR
import com.stenleone.stanleysfilm.network.TmdbNetworkConstant.LIST_MOVIE_TOP_RATED
import com.stenleone.stanleysfilm.network.TmdbNetworkConstant.LIST_MOVIE_TOP_UPCOMING
import com.stenleone.stanleysfilm.network.entity.lates.LatesEntityUI
import com.stenleone.stanleysfilm.network.entity.movie.MoviesEntityUI
import com.stenleone.stanleysfilm.network.repository.ListMovieRepository
import com.stenleone.stanleysfilm.util.extencial.successOrError
import com.stenleone.stanleysfilm.viewModel.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    apiService: ApiService,
    sharedPreferencesManager: SharedPreferencesManager,
    connectionManager: ConnectionManager,
    val movieRepository: ListMovieRepository,
    val messageShowsManager: SharedPreferencesMessageManager
) : BaseViewModel(apiService, sharedPreferencesManager, connectionManager) {

    val movieLatesLiveData = MutableLiveData<LatesEntityUI>()
    val movieNowPlayingLiveData = MutableLiveData<MoviesEntityUI>()
    val moviePopularLiveData = MutableLiveData<MoviesEntityUI>()
    val movieTopRatedLiveData = MutableLiveData<MoviesEntityUI>()
    val movieUpcomingLiveData = MutableLiveData<MoviesEntityUI>()

    init {
        setupGuestSessionToken()
        loadContent()
    }

    fun loadContent() {
        viewModelScope.launch {
            inProgress.postValue(true)
            async {
                val nowPlaying = movieRepository.getMovieList(LIST_MOVIE_NOW_PLAYING)
                when (nowPlaying) {
                    is DataState.Success -> {
                        postValue(LIST_MOVIE_NOW_PLAYING, nowPlaying.data)
                    }
                    is DataState.Error -> {

                    }
                }
                val popular = movieRepository.getMovieList(LIST_MOVIE_POPULAR)
                when (popular) {
                    is DataState.Success -> {
                        postValue(LIST_MOVIE_POPULAR, popular.data)
                    }
                    is DataState.Error -> {

                    }
                }
                val topRated = movieRepository.getMovieList(LIST_MOVIE_TOP_RATED)
                when (topRated) {
                    is DataState.Success -> {
                        postValue(LIST_MOVIE_TOP_RATED, topRated.data)
                    }
                    is DataState.Error -> {

                    }
                }
                val upComing = movieRepository.getMovieList(LIST_MOVIE_TOP_UPCOMING)
                when (upComing) {
                    is DataState.Success -> {
                        postValue(LIST_MOVIE_TOP_UPCOMING, upComing.data)
                    }
                    is DataState.Error -> {

                    }
                }
            }
            inProgress.postValue(false)
        }
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

    private fun postValue(
        type: String,
        moviesUI: MoviesEntityUI
    ) {
        when (type) {
            LIST_MOVIE_NOW_PLAYING -> {
                movieNowPlayingLiveData.postValue(moviesUI)
            }
            LIST_MOVIE_POPULAR -> {
                moviePopularLiveData.postValue(moviesUI)
            }
            LIST_MOVIE_TOP_RATED -> {
                movieTopRatedLiveData.postValue(moviesUI)
            }
            LIST_MOVIE_TOP_UPCOMING -> {
                movieUpcomingLiveData.postValue(moviesUI)
            }
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun setupGuestSessionToken() { // 2021-02-22 11:13:21 UTC

        val sdf = SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
        val currentDate = sdf.format(Date())

        if (sharedPreferencesManager.expiresGuestTokenAt != null) {
            val tokenDate = sdf.format(sdf.parse(sharedPreferencesManager.expiresGuestTokenAt ?: "0"))
// 2021-30-21 11:30:31 2021-15-22 11:15:16
            if (currentDate > tokenDate) {
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
                            messageShowsManager.rateMessageGuestSessionShows = false
                        }
                    }, {
                        isFailure(RequestError.UNSUCCESS_STATUS, it)
                    }
                )
        }
    }
}