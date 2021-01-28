package com.stenleone.stanleysfilm.network.repository

import com.stenleone.stanleysfilm.managers.ConnectionManager
import com.stenleone.stanleysfilm.managers.sharedPrefs.SharedPreferencesManager
import com.stenleone.stanleysfilm.model.entity.DataState
import com.stenleone.stanleysfilm.network.ApiService
import com.stenleone.stanleysfilm.network.entity.movie.MoviesEntityUI
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ListMovieRepository @Inject constructor(
    val apiService: ApiService,
    val sharedPreferencesManager: SharedPreferencesManager,
    val connectionManager: ConnectionManager,
    val mapper: ListMovieMapper
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
                    emit(DataState.Succes(mapper.mapFromEntity(data)))
                }
            } else {
                emit(DataState.Error(Exception()))
            }
        } catch (e: Exception) {
            emit(DataState.Loading(false))
            emit(DataState.Error(e))
        }
    }

    suspend fun getMoviesListsParallel(dataList: ArrayList<GetListsObject>): Flow<DataState<ListsObject>> = flow {
        emit(DataState.Loading(true))

        val errors = ArrayList<Exception>()
        val successList = ArrayList<ListsObject>()

        dataList.forEachIndexed { index, getListsObject ->
            try {
                val response = apiService.getListMovieAsync(
                    getListsObject.typeList,
                    language = sharedPreferencesManager.language,
                    page = getListsObject.page
                ).await()

                if (response.isSuccessful) {
                    response.body()?.let { data ->
                        successList.add(ListsObject(getListsObject.typeList, mapper.mapFromEntity(data)))
                    }
                } else {
                    errors.add(Exception())
                }
            } catch (e: Exception) {
                errors.add(e)
            }
        }
        if (errors.size > 0) {
            emit(DataState.Errors(errors))
        }
        if (successList.size > 0) {
            emit(DataState.Success(successList))
        }
        emit(DataState.Loading(false))
    }

    suspend fun getMoviesListsSuccessively(dataList: ArrayList<GetListsObject>): Flow<DataState<ListsObject>> = flow {
        emit(DataState.Loading(true))

        val errors = ArrayList<Exception>()

        dataList.forEachIndexed { index, getListsObject ->
            try {
                val response = apiService.getListMovieAsync(
                    getListsObject.typeList,
                    language = sharedPreferencesManager.language,
                    page = getListsObject.page
                ).await()

                if (response.isSuccessful) {
                    response.body()?.let { data ->
                        emit(DataState.Succes(ListsObject(getListsObject.typeList, mapper.mapFromEntity(data))))
                    }
                } else {
                    errors.add(Exception())
                }
            } catch (e: Exception) {
                errors.add(e)
            }
        }
        if (errors.size > 0) {
            emit(DataState.Errors(errors))
        }
        emit(DataState.Loading(false))
    }

    data class GetListsObject(
        val typeList: String,
        val page: Int = 1,
    )

    data class ListsObject(
        val typeList: String,
        val moviesEntity: MoviesEntityUI
    )
}