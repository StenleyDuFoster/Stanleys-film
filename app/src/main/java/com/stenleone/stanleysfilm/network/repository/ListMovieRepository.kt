package com.stenleone.stanleysfilm.network.repository

import com.stenleone.stanleysfilm.managers.ConnectionManager
import com.stenleone.stanleysfilm.managers.sharedPrefs.SharedPreferencesManager
import com.stenleone.stanleysfilm.model.entity.DataState
import com.stenleone.stanleysfilm.model.entity.RequestError
import com.stenleone.stanleysfilm.network.ApiService
import com.stenleone.stanleysfilm.network.entity.movie.MoviesEntityUI
import com.stenleone.stanleysfilm.network.mapper.MoviesEntityMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ListMovieRepository @Inject constructor(
    val apiService: ApiService,
    val sharedPreferencesManager: SharedPreferencesManager,
    val connectionManager: ConnectionManager,
    val listMovieMapper: MoviesEntityMapper
) {

    suspend fun getMovieList(typeList: String, page: Int = 1): DataState<MoviesEntityUI> {
        try {
            val response = apiService.getListMovieAsync(
                typeList,
                language = sharedPreferencesManager.language,
                page = page
            ).await()

            val data = response.body()
            if (response.isSuccessful && data != null) {
                return (DataState.Success(listMovieMapper.mapFromEntity(data)))
            } else {
                return if (connectionManager.isConnected.value == true) {
                    (DataState.Error(RequestError(RequestError.REQUEST_ERROR, message = response.errorBody().toString())))
                } else {
                    (DataState.Error(RequestError(RequestError.CONNECTION_ERROR)))
                }
            }
        } catch (e: Exception) {
            return if (connectionManager.isConnected.value == true) {
                (DataState.Error(RequestError(RequestError.REQUEST_ERROR, message = e.message.toString())))
            } else {
                (DataState.Error(RequestError(RequestError.CONNECTION_ERROR)))
            }
        }
    }

    suspend fun getRateMovieList(guestSessionId: String?, page: Int = 1): DataState<MoviesEntityUI> {
        try {
            val response = apiService.getListRateMovieAsync(
                guestSessionId,
                language = sharedPreferencesManager.language,
                page = page
            ).await()

            val data = response.body()
            if (response.isSuccessful && data != null) {
                return (DataState.Success(listMovieMapper.mapFromEntity(data)))
            } else {
                return if (connectionManager.isConnected.value == true) {
                    (DataState.Error(RequestError(RequestError.REQUEST_ERROR, message = response.errorBody().toString())))
                } else {
                    (DataState.Error(RequestError(RequestError.CONNECTION_ERROR)))
                }
            }
        } catch (e: Exception) {
            return if (connectionManager.isConnected.value == true) {
                (DataState.Error(RequestError(RequestError.REQUEST_ERROR, message = e.message.toString())))
            } else {
                (DataState.Error(RequestError(RequestError.CONNECTION_ERROR)))
            }
        }
    }
}