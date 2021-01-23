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

class VideoViewModel @ViewModelInject constructor(
    apiService: ApiService,
    sharedPreferencesManager: SharedPreferencesManager,
    connectionManager: ConnectionManager
) : BaseViewModel(apiService, sharedPreferencesManager, connectionManager) {

    val movieList = MutableLiveData<MoviesEntity>()

    fun getSimilarMovieList(id: Int, page: Int) {
        doAsyncRequest {
            apiService.getSimilarList(
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


}