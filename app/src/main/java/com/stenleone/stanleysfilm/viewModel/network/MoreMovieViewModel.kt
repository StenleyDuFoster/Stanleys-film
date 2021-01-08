package com.stenleone.stanleysfilm.viewModel.network

import androidx.lifecycle.MutableLiveData
import com.stenleone.stanleysfilm.managers.SharedPreferencesManager
import com.stenleone.stanleysfilm.model.entity.RequestError
import com.stenleone.stanleysfilm.network.ApiService
import com.stenleone.stanleysfilm.network.TmdbNetworkConstant
import com.stenleone.stanleysfilm.network.entity.movie.MovieDetailsEntity
import com.stenleone.stanleysfilm.network.entity.movie.MoviesEntity
import com.stenleone.stanleysfilm.viewModel.base.BaseViewModel

class MoreMovieViewModel(
    private val apiService: ApiService,
    private val sharedPreferencesManager: SharedPreferencesManager
) : BaseViewModel() {

    val movieList = MutableLiveData<MoviesEntity>()

    val inProgress = MutableLiveData<Boolean>()
    val errorLiveData = MutableLiveData<RequestError>()

    val isFailure: (errorMessage: String, type: String) -> Unit = { errorMessage: String, type: String ->
        errorLiveData.postValue(RequestError(type = type, message = errorMessage))
    }

    fun getPage(typeList: String, page: Int, movieId: String? = null) {
        when (typeList) {
            TmdbNetworkConstant.LIST_RECOMENDED -> {
                getRecomendedMovieList(movieId?.toInt() ?: 0, page)
            }
            TmdbNetworkConstant.LIST_MOVIE_POPULAR -> {
                getMovieList(typeList, page)
            }
            TmdbNetworkConstant.LIST_MOVIE_TOP_RATED -> {
                getMovieList(typeList, page)
            }
            TmdbNetworkConstant.LIST_MOVIE_NOW_PLAYING -> {
                getMovieList(typeList, page)
            }
            TmdbNetworkConstant.LIST_MOVIE_TOP_UPCOMING -> {
                getMovieList(typeList, page)
            }
            else -> {
                isFailure(RequestError.UNSUCCESS_STATUS, "TMDB CONSTANT FAIL")
            }
        }
    }

    private fun getMovieList(typeList: String, page: Int) {
        doWork {
            inProgress.postValue(true)
            try {
                val response = apiService.getListMovieAsync(
                    typeList,
                    language = sharedPreferencesManager.language,
                    page = page
                )
                    .await()

                if (response.isSuccessful) {
                    inProgress.postValue(false)
                    response.body()?.let {
                        movieList.postValue(it)
                    }
                } else {
                    inProgress.postValue(false)
                    isFailure(RequestError.UNSUCCESS_STATUS, response.message())
                }
            } catch (e: Exception) {
                inProgress.postValue(false)
                isFailure(RequestError.REQUEST_ERROR, e.toString())
            }
        }
    }

    private fun getRecomendedMovieList(id: Int, page: Int) {
        doWork {
            inProgress.postValue(true)
            try {
                val response = apiService.getRecomendedList(
                    language = sharedPreferencesManager.language,
                    movieId = id,
                    page = page
                )
                    .await()

                if (response.isSuccessful) {
                    inProgress.postValue(false)
                    response.body()?.let {
                        movieList.postValue(it)
                    }
                } else {
                    inProgress.postValue(false)
                    isFailure(RequestError.UNSUCCESS_STATUS, response.message())
                }
            } catch (e: Exception) {
                inProgress.postValue(false)
                isFailure(RequestError.REQUEST_ERROR, e.toString())
            }
        }
    }
}