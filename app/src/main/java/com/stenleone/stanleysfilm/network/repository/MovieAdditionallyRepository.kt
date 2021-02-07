package com.stenleone.stanleysfilm.network.repository

import com.stenleone.stanleysfilm.managers.ConnectionManager
import com.stenleone.stanleysfilm.managers.sharedPrefs.SharedPreferencesManager
import com.stenleone.stanleysfilm.model.entity.DataState
import com.stenleone.stanleysfilm.network.ApiService
import com.stenleone.stanleysfilm.network.mapper.VideosMapper
import com.stenleone.stanleysfilm.ui.model.VideosUI
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MovieAdditionallyRepository @Inject constructor(
    val apiService: ApiService,
    val sharedPreferencesManager: SharedPreferencesManager,
    val connectionManager: ConnectionManager,
    val videosMapper: VideosMapper
) {

    suspend fun getVideos(movieId: Int): Flow<DataState<VideosUI>> = flow {
        emit(DataState.Loading(true))
        try {
            val response = apiService.getVideos(
                movieId,
                language = sharedPreferencesManager.language
            ).await()
            emit(DataState.Loading(false))

            if (response.isSuccessful) {
                response.body()?.let { data ->
                    emit(DataState.Success(videosMapper.mapFromEntity(data)))
                }
            } else {
                emit(DataState.Error(Exception()))
            }
        } catch (e: Exception) {
            emit(DataState.Loading(false))
            emit(DataState.Error(e))
        }
    }
}