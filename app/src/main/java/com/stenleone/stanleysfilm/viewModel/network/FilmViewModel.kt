package com.stenleone.stanleysfilm.viewModel.network

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import com.stenleone.stanleysfilm.managers.ConnectionManager
import com.stenleone.stanleysfilm.managers.sharedPrefs.SharedPreferencesManager
import com.stenleone.stanleysfilm.model.entity.RequestError
import com.stenleone.stanleysfilm.network.ApiService
import com.stenleone.stanleysfilm.network.entity.movie.MovieDetailsEntity
import com.stenleone.stanleysfilm.network.entity.movie.MoviesEntity
import com.stenleone.stanleysfilm.util.extencial.successOrError
import com.stenleone.stanleysfilm.viewModel.base.BaseViewModel

class FilmViewModel @ViewModelInject constructor(
    apiService: ApiService,
    sharedPreferencesManager: SharedPreferencesManager,
    connectionManager: ConnectionManager
) : BaseViewModel(apiService, sharedPreferencesManager, connectionManager) {

    val movieUrl = MutableLiveData<String?>()
    val movieDetails = MutableLiveData<MovieDetailsEntity>()
    val recomendedMovieList = MutableLiveData<MoviesEntity>()

    fun getPageData(id: Int) {
        getMovieDetails(id)
        getRecomendedMovieList(id)
    }

    private fun getMovieDetails(id: Int) {
        doAsyncRequest {
            apiService.getMovieDetails(
                language = sharedPreferencesManager.language,
                movieId = id
            )
                .await()
                .successOrError(
                    success = {
                        movieDetails.postValue(it)
                    }, {
                        isFailure(RequestError.UNSUCCESS_STATUS, it)
                    }
                )
        }
    }

    private fun getRecomendedMovieList(id: Int) {
        doAsyncRequest {
           apiService.getRecomendedList(
                language = sharedPreferencesManager.language,
                movieId = id
            )
                .await()
                .successOrError(
                    success = {
                        recomendedMovieList.postValue(it)
                    }, {
                        isFailure(RequestError.UNSUCCESS_STATUS, it)
                    }
                )
        }
    }
}