package com.stenleone.stanleysfilm.viewModel.network

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import com.stenleone.stanleysfilm.managers.ConnectionManager
import com.stenleone.stanleysfilm.managers.sharedPrefs.SharedPreferencesManager
import com.stenleone.stanleysfilm.model.entity.RequestError
import com.stenleone.stanleysfilm.network.ApiService
import com.stenleone.stanleysfilm.network.TmdbNetworkConstant
import com.stenleone.stanleysfilm.network.entity.movie.MoviesEntity
import com.stenleone.stanleysfilm.util.extencial.successOrError
import com.stenleone.stanleysfilm.viewModel.base.BaseViewModel

class MoreMovieViewModel @ViewModelInject constructor(
    apiService: ApiService,
    sharedPreferencesManager: SharedPreferencesManager,
    connectionManager: ConnectionManager
) : BaseViewModel(apiService, sharedPreferencesManager, connectionManager) {

    val movieList = MutableLiveData<MoviesEntity>()

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
            TmdbNetworkConstant.SEARCH_MOVIE -> {
                search(movieId ?: "", page)
            }
            else -> {
                isFailure(RequestError.UNSUCCESS_STATUS, "TMDB CONSTANT FAIL")
            }
        }
    }

    private fun getMovieList(typeList: String, page: Int) {
        doAsyncRequest {
            apiService.getListMovieAsync(
                typeList,
                language = sharedPreferencesManager.language,
                page = page
            )
                .await()
                .successOrError(
                    success = {
                        movieList.postValue(it)
                    }, {
                        isFailure(RequestError.UNSUCCESS_STATUS, it)
                    }
                )
        }
    }

    private fun getRecomendedMovieList(id: Int, page: Int) {
        doAsyncRequest {
            apiService.getRecomendedList(
                language = sharedPreferencesManager.language,
                movieId = id,
                page = page
            )
                .await()
                .successOrError(
                    success = {
                        movieList.postValue(it)
                    }, {
                        isFailure(RequestError.UNSUCCESS_STATUS, it)
                    }
                )
        }
    }

    fun search(keywords: String, page: Int) {
        doAsyncRequest {
            apiService.searchMovie(
                search = keywords,
                page = page,
                language = sharedPreferencesManager.language,
            )
                .await()
                .successOrError(
                    success = {
                        movieList.postValue(it)
                    }, {
                        isFailure(RequestError.UNSUCCESS_STATUS, it)
                    }
                )
        }
    }
}