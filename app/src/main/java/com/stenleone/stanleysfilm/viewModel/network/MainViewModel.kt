package com.stenleone.stanleysfilm.viewModel.network

import androidx.lifecycle.MutableLiveData
import com.stenleone.stanleysfilm.managers.SharedPreferencesManager
import com.stenleone.stanleysfilm.model.entity.RequestError
import com.stenleone.stanleysfilm.network.ApiService
import com.stenleone.stanleysfilm.network.TmdbNetworkConstant.LIST_MOVIE_NOW_PLAYING
import com.stenleone.stanleysfilm.network.TmdbNetworkConstant.LIST_MOVIE_POPULAR
import com.stenleone.stanleysfilm.network.TmdbNetworkConstant.LIST_MOVIE_TOP_RATED
import com.stenleone.stanleysfilm.network.TmdbNetworkConstant.LIST_MOVIE_TOP_UPCOMING
import com.stenleone.stanleysfilm.network.entity.lates.LatesEntity
import com.stenleone.stanleysfilm.network.entity.movie.MoviesEntity
import com.stenleone.stanleysfilm.viewModel.base.BaseViewModel

class MainViewModel(
    private val apiService: ApiService,
    private val sharedPreferencesManager: SharedPreferencesManager
) : BaseViewModel() {

    val movieLatesLiveData = MutableLiveData<LatesEntity>()
    val movieNowPlayingLiveData = MutableLiveData<MoviesEntity>()
    val moviePopularLiveData = MutableLiveData<MoviesEntity>()
    val movieTopRatedLiveData = MutableLiveData<MoviesEntity>()
    val movieUpcomingLiveData = MutableLiveData<MoviesEntity>()

    val errorLiveData = MutableLiveData<RequestError>()

    val isFailure: (errorMessage: String, type: String) -> Unit = { errorMessage: String, type: String ->
        errorLiveData.postValue(RequestError(type = type, message = errorMessage))
    }

    init {
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
        doWork {
            try {
                val response = apiService.getLatesMovieAsync(
                    language = sharedPreferencesManager.language,
                    page = page
                )
                    .await()

                if (response.isSuccessful) {
                    response.body()?.let {
                        movieLatesLiveData.postValue(it)
                    }
                } else {
                    isFailure(RequestError.UNSUCCESS_STATUS, response.message())
                }
            } catch (e: Exception) {
                isFailure(RequestError.REQUEST_ERROR, e.toString())
            }
        }
    }

    private fun getListMovie(typeList: String, page: Int = 1) {
        doWork {
            try {
                val response = apiService.getListMovieAsync(
                    typeList,
                    language = sharedPreferencesManager.language,
                    page = page
                )
                    .await()

                if (response.isSuccessful) {
                    response.body()?.let {
                        postValue(typeList, it)
                    }
                } else {
                    isFailure(RequestError.UNSUCCESS_STATUS, response.message())
                }
            } catch (e: Exception) {
                isFailure(RequestError.REQUEST_ERROR, e.toString())
            }
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
}