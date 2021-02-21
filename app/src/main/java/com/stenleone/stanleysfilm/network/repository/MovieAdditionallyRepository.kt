package com.stenleone.stanleysfilm.network.repository

import com.stenleone.stanleysfilm.managers.ConnectionManager
import com.stenleone.stanleysfilm.managers.sharedPrefs.SharedPreferencesManager
import com.stenleone.stanleysfilm.model.entity.DataState
import com.stenleone.stanleysfilm.model.entity.RequestError
import com.stenleone.stanleysfilm.network.ApiService
import com.stenleone.stanleysfilm.network.entity.movie.MovieDetailsEntityUI
import com.stenleone.stanleysfilm.network.mapper.MoviesDetailsEntityMapper
import com.stenleone.stanleysfilm.network.mapper.MoviesEntityMapper_Factory
import com.stenleone.stanleysfilm.network.mapper.VideosMapper
import com.stenleone.stanleysfilm.ui.model.VideosUI
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MovieAdditionallyRepository @Inject constructor(
    val apiService: ApiService,
    val sharedPreferencesManager: SharedPreferencesManager,
    val connectionManager: ConnectionManager,
    val videosMapper: VideosMapper,
    val moviesDetailsEntityMapper: MoviesDetailsEntityMapper
) {

    suspend fun getVideos(movieId: Int): DataState<VideosUI> {
        try {
            val response = apiService.getVideos(
                movieId,
                language = sharedPreferencesManager.language
            ).await()

            val data = response.body()
            if (response.isSuccessful && data != null) {
                return (DataState.Success(videosMapper.mapFromEntity(data)))
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

    suspend fun getDetailsMovie(movieId: Int): DataState<MovieDetailsEntityUI> {
        try {
            val response = apiService.getMovieDetails(
                movieId,
                language = sharedPreferencesManager.language
            ).await()

            val data = response.body()
            if (response.isSuccessful && data != null) {
                return (DataState.Success(moviesDetailsEntityMapper.mapFromEntity(data)))
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

    suspend fun getDetailsMovieFlow(movieId: Int): Flow<DataState<MovieDetailsEntityUI>> = flow {
        try {
            val response = apiService.getMovieDetails(
                movieId,
                language = sharedPreferencesManager.language
            ).await()

            val data = response.body()
            if (response.isSuccessful && data != null) {
                emit(DataState.Success(moviesDetailsEntityMapper.mapFromEntity(data)))
            } else {
                 if (connectionManager.isConnected.value == true) {
                    emit(DataState.Error(RequestError(RequestError.REQUEST_ERROR, message = response.errorBody().toString())))
                } else {
                     emit(DataState.Error(RequestError(RequestError.CONNECTION_ERROR)))
                }
            }
        } catch (e: Exception) {
             if (connectionManager.isConnected.value == true) {
                 emit(DataState.Error(RequestError(RequestError.REQUEST_ERROR, message = e.message.toString())))
            } else {
                 emit(DataState.Error(RequestError(RequestError.CONNECTION_ERROR)))
            }
        }
    }
}