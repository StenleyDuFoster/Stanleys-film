package com.stenleone.stanleysfilm.viewModel.network

import androidx.lifecycle.MutableLiveData
import com.stenleone.stanleysfilm.managers.SharedPreferencesManager
import com.stenleone.stanleysfilm.model.entity.RequestError
import com.stenleone.stanleysfilm.network.ApiService
import com.stenleone.stanleysfilm.network.entity.movie.MovieDetailsEntity
import com.stenleone.stanleysfilm.network.entity.movie.MoviesEntity
import com.stenleone.stanleysfilm.viewModel.base.BaseViewModel

class FilmViewModel(
    private val apiService: ApiService,
    private val sharedPreferencesManager: SharedPreferencesManager
) : BaseViewModel() {

    val movieDetails = MutableLiveData<MovieDetailsEntity>()
    val recomendedMovieList = MutableLiveData<MoviesEntity>()

    val errorLiveData = MutableLiveData<RequestError>()

    val isFailure: (errorMessage: String, type: String) -> Unit = { errorMessage: String, type: String ->
        errorLiveData.postValue(RequestError(type = type, message = errorMessage))
    }

    fun getPageData(id: Int) {
        getMovieDetails(id)
        getRecomendedMovieList(id)
    }

    private fun getMovieDetails(id: Int) {
        doWork {
            try {
                val response = apiService.getMovieDetails(
                    language = sharedPreferencesManager.language,
                    movieId = id
                )
                    .await()

                if (response.isSuccessful) {
                    response.body()?.let {
                        movieDetails.postValue(it)
                    }
                } else {
                    isFailure(RequestError.UNSUCCESS_STATUS, response.message())
                }
            } catch (e: Exception) {
                isFailure(RequestError.REQUEST_ERROR, e.toString())
            }
        }
    }

    private fun getRecomendedMovieList(id: Int) {
        doWork {
            try {
                val response = apiService.getRecomendedList(
                    language = sharedPreferencesManager.language,
                    movieId = id
                )
                    .await()

                if (response.isSuccessful) {
                    response.body()?.let {
                        recomendedMovieList.postValue(it)
                    }
                } else {
                    isFailure(RequestError.UNSUCCESS_STATUS, response.message())
                }
            } catch (e: Exception) {
                isFailure(RequestError.REQUEST_ERROR, e.toString())
            }
        }
    }
}