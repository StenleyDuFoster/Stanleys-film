package com.stenleone.stanleysfilm.viewModel.network

import androidx.lifecycle.MutableLiveData
import com.stenleone.stanleysfilm.managers.ConnectionManager
import com.stenleone.stanleysfilm.managers.firebase.FirebaseCloudFirestoreManagers
import com.stenleone.stanleysfilm.managers.sharedPrefs.SharedPreferencesManager
import com.stenleone.stanleysfilm.model.entity.RequestError
import com.stenleone.stanleysfilm.network.ApiService
import com.stenleone.stanleysfilm.network.entity.movie.MovieDetailsEntityUI
import com.stenleone.stanleysfilm.network.entity.movie.MoviesEntityUI
import com.stenleone.stanleysfilm.util.extencial.successOrError
import com.stenleone.stanleysfilm.managers.controllers.filmFinders.FindFilmFilmixController
import com.stenleone.stanleysfilm.model.entity.DataState
import com.stenleone.stanleysfilm.network.entity.images.ImagesEntityUI
import com.stenleone.stanleysfilm.network.repository.MovieAdditionallyRepository
import com.stenleone.stanleysfilm.ui.model.VideosUI
import com.stenleone.stanleysfilm.viewModel.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import lampa.test.tmdblib.model.viewmodel.repository.internet.parser.callBack.CallBackVideoFromParser
import javax.inject.Inject

@HiltViewModel
class FilmViewModel @Inject constructor(
    apiService: ApiService,
    sharedPreferencesManager: SharedPreferencesManager,
    connectionManager: ConnectionManager,
    val additionallyRepository: MovieAdditionallyRepository,
    val findFilmFilmixController: FindFilmFilmixController,
    val firestoreManager: FirebaseCloudFirestoreManagers
) : BaseViewModel(apiService, sharedPreferencesManager, connectionManager) {

    val movieUrl = MutableLiveData<String?>()
    val movieDetails = MutableLiveData<MovieDetailsEntityUI>()
    val imageList = MutableLiveData<ImagesEntityUI>()
    val recomendedMovieList = MutableLiveData<MoviesEntityUI>()
    val moviesVideos = MutableLiveData<VideosUI>()

    fun getPageData(id: Int) {
        doAsyncRequests(
            arrayListOf(
                {
                    apiService.getMovieDetails(
                        language = sharedPreferencesManager.language,
                        movieId = id
                    )
                        .await()
                        .successOrError(
                            success = {
                                movieDetails.postValue(it)
                            }, {
                                isFailure(RequestError.UNSUCCESS_STATUS, it)
                            }
                        )
                }, {
                    apiService.getRecomendedList(
                        language = sharedPreferencesManager.language,
                        movieId = id
                    )
                        .await()
                        .successOrError(
                            success = {
                                recomendedMovieList.postValue(it)
                            }, {
                                isFailure(RequestError.UNSUCCESS_STATUS, it)
                            }
                        )
                }, {
                    apiService.getImageList(
                        language = sharedPreferencesManager.language,
                        movieId = id
                    )
                        .await()
                        .successOrError(
                            success = {
                                imageList.postValue(it)
                            }, {
                                isFailure(RequestError.UNSUCCESS_STATUS, it)
                            }
                        )
                },
                {
                    additionallyRepository.getVideos(id).collect {
                        when (it) {
                            is DataState.Success -> {
                                moviesVideos.postValue(it.data)
                            }
                            is DataState.Error -> {

                            }
                        }
                    }
                }
            )
        )
    }

    fun startFindFilmUrl(title: String, date: String?) {
        findByWebView(title, date)
        findByFireStore(title, date)
    }

    private fun findByWebView(title: String, date: String?) {
        findFilmFilmixController.start(
            title,
            date ?: "0000",
            object : CallBackVideoFromParser {
                override fun onVideoFind(link: String) {
                    movieUrl.postValue(link)
                    firestoreManager.setMovieUrl(title, date ?: "0000", link)
                }
            })
    }

    private fun findByFireStore(title: String, date: String?) {
        firestoreManager.getMovieUrl(title, date ?: "0000") {

            if (movieUrl.value == null) {
                movieUrl.postValue(it)
            }
        }
    }

    private fun getMovieDetails(id: Int) {
        doAsyncRequest {
            apiService.getMovieDetails(
                language = sharedPreferencesManager.language,
                movieId = id
            )
                .await()
                .successOrError(
                    success = {
                        movieDetails.postValue(it)
                    }, {
                        isFailure(RequestError.UNSUCCESS_STATUS, it)
                    }
                )
        }
    }

    private fun getRecomendedMovieList(id: Int) {
        doAsyncRequest {
            apiService.getRecomendedList(
                language = sharedPreferencesManager.language,
                movieId = id
            )
                .await()
                .successOrError(
                    success = {
                        recomendedMovieList.postValue(it)
                    }, {
                        isFailure(RequestError.UNSUCCESS_STATUS, it)
                    }
                )
        }
    }

    override fun onCleared() {
        findFilmFilmixController.onDestroy()
        super.onCleared()
    }
}