package com.stenleone.stanleysfilm.viewModel.network

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.stenleone.stanleysfilm.managers.ConnectionManager
import com.stenleone.stanleysfilm.managers.firebase.FirebaseCloudFirestoreManagers
import com.stenleone.stanleysfilm.managers.sharedPrefs.SharedPreferencesManager
import com.stenleone.stanleysfilm.model.entity.DataState
import com.stenleone.stanleysfilm.network.ApiService
import com.stenleone.stanleysfilm.network.entity.movie.MovieUI
import com.stenleone.stanleysfilm.network.repository.MovieAdditionallyRepository
import com.stenleone.stanleysfilm.viewModel.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    apiService: ApiService,
    sharedPreferencesManager: SharedPreferencesManager,
    connectionManager: ConnectionManager,
    val additionallyRepository: MovieAdditionallyRepository,
    val firestoreManagers: FirebaseCloudFirestoreManagers
) : BaseViewModel(apiService, sharedPreferencesManager, connectionManager) {

    val favoriteMoviesId = ArrayList<String>()
    val favoriteMovies = MutableLiveData<ArrayList<MovieUI>>()

    fun getFavoriteMovies() {
        firestoreManagers.getFavorite({
            it.get(FirebaseCloudFirestoreManagers.MOVIE)?.let {
                favoriteMoviesId.addAll(it)
                getFavoriteMovies(it)
            }
        }, {

        })
    }

    private fun getFavoriteMovies(listId: ArrayList<String>) {
        viewModelScope.launch {

            val movies = ArrayList<MovieUI>()
            var respounseFinishCount = 0

            listId.forEach {
                async {
                    additionallyRepository.getDetailsMovieFlow(it.toInt()).collect {
                        when (it) {
                            is DataState.Success -> {
                                movies.add(it.data.mapToMovie())
                                respounseFinishCount ++
                                if (respounseFinishCount == listId.size) {

                                    favoriteMovies.postValue(sortMovieByAddedDate(listId, movies))
                                }
                            }
                            is DataState.Error -> {
                                respounseFinishCount ++
                                if (respounseFinishCount == listId.size) {

                                    favoriteMovies.postValue(sortMovieByAddedDate(listId, movies))
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun sortMovieByAddedDate(listId: ArrayList<String>, movieList: ArrayList<MovieUI>):  ArrayList<MovieUI> {

        val sortList = ArrayList<MovieUI>()

        listId.forEach {
            findMovieById(it, movieList)?.let {
                sortList.add(it)
            }
        }

        sortList.reverse()

        return sortList
    }

    private fun findMovieById(id: String, movieList: ArrayList<MovieUI>): MovieUI? {
        movieList.forEach {
            if (id.toInt() == it.id) {
                return it
            }
        }
        return null
    }

}