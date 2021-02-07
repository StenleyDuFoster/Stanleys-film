package com.stenleone.stanleysfilm.network.repository

import com.stenleone.stanleysfilm.managers.ConnectionManager
import com.stenleone.stanleysfilm.managers.sharedPrefs.SharedPreferencesManager
import com.stenleone.stanleysfilm.model.entity.DataState
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

    suspend fun getMovieList(typeList: String, page: Int = 1): Flow<DataState<MoviesEntityUI>> = flow {
        emit(DataState.Loading(true))
        try {
            val response = apiService.getListMovieAsync(
                typeList,
                language = sharedPreferencesManager.language,
                page = page
            ).await()
            emit(DataState.Loading(false))

            if (response.isSuccessful) {
                response.body()?.let { data ->
                    emit(DataState.Success(listMovieMapper.mapFromEntity(data)))
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