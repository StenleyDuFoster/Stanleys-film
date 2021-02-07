package com.stenleone.stanleysfilm.viewModel.network

import androidx.lifecycle.MutableLiveData
import com.stenleone.stanleysfilm.managers.ConnectionManager
import com.stenleone.stanleysfilm.managers.sharedPrefs.SharedPreferencesManager
import com.stenleone.stanleysfilm.model.entity.DataState
import com.stenleone.stanleysfilm.model.entity.RequestError
import com.stenleone.stanleysfilm.network.ApiService
import com.stenleone.stanleysfilm.network.TmdbNetworkConstant
import com.stenleone.stanleysfilm.network.entity.movie.MoviesEntityUI
import com.stenleone.stanleysfilm.network.repository.ListMovieRepository
import com.stenleone.stanleysfilm.util.extencial.successOrError
import com.stenleone.stanleysfilm.viewModel.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@HiltViewModel
class MoreMovieViewModel @Inject constructor(
    apiService: ApiService,
    sharedPreferencesManager: SharedPreferencesManager,
    connectionManager: ConnectionManager,
    val movieRepository: ListMovieRepository
) : BaseViewModel(apiService, sharedPreferencesManager, connectionManager) {

    val movieList = MutableLiveData<MoviesEntityUI>()
    var pageCurrent: Int? = null

    fun getPage(typeList: String, movieId: String? = null) {
        val page = (pageCurrent ?: 0) + 1 // load next page
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
            movieRepository.getMovieList(typeList, page).collect {
                when (it) {
                    is DataState.Success -> {
                        movieList.postValue(it.data)
                        pageCurrent = (it.data.page)
                    }
                    is DataState.Error -> {

                    }
                }
            }
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
                        pageCurrent = (it.page)
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
                        pageCurrent = (it.page)
                    }, {
                        isFailure(RequestError.UNSUCCESS_STATUS, it)
                    }
                )
        }
    }
}