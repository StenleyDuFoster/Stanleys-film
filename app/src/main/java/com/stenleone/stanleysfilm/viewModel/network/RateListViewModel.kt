package com.stenleone.stanleysfilm.viewModel.network

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.stenleone.stanleysfilm.managers.ConnectionManager
import com.stenleone.stanleysfilm.managers.sharedPrefs.SharedPreferencesManager
import com.stenleone.stanleysfilm.model.entity.DataState
import com.stenleone.stanleysfilm.network.ApiService
import com.stenleone.stanleysfilm.network.entity.movie.MovieUI
import com.stenleone.stanleysfilm.network.repository.ListMovieRepository
import com.stenleone.stanleysfilm.viewModel.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.Exception

@HiltViewModel
class RateListViewModel @Inject constructor(
    apiService: ApiService,
    sharedPreferencesManager: SharedPreferencesManager,
    connectionManager: ConnectionManager,
    val movieRepository: ListMovieRepository
) : BaseViewModel(apiService, sharedPreferencesManager, connectionManager) {

    val movie = MutableLiveData<ArrayList<MovieUI>>()

    init {
        getMovies()
    }

    fun getMovies() {
        viewModelScope.launch {
            val data = movieRepository.getRateMovieList(
                sharedPreferencesManager.guestSessionToken,
            )
            when (data) {
                is DataState.Success -> {
                    movie.postValue(data.data.movies)
                }
                is DataState.Error -> {
                    Log.v("112233", data.exception.message.toString())
                }
            }
        }
    }
}