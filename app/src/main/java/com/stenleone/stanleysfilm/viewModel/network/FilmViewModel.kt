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
import com.stenleone.stanleysfilm.ui.model.general.FavoriteState
import com.stenleone.stanleysfilm.viewModel.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.collect
import lampa.test.tmdblib.model.viewmodel.repository.internet.parser.callBack.CallBackVideoFromParser
import okhttp3.internal.wait
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
    val favoriteIdList = MutableLiveData<HashMap<String, ArrayList<String>>>()
    val updateFavoriteStatus = MutableLiveData<FavoriteState>()
    val movieDetails = MutableLiveData<MovieDetailsEntityUI>()
    val imageList = MutableLiveData<ImagesEntityUI>()
    val recomendedMovieList = MutableLiveData<MoviesEntityUI>()
    val moviesVideos = MutableLiveData<VideosUI>()

    fun getPageData(id: Int) {
        doAsyncRequests(
            arrayListOf(
                {
                    val details = additionallyRepository.getDetailsMovie(id)
                    when (details) {
                        is DataState.Success -> {
                            movieDetails.postValue(details.data)
                        }
                        is DataState.Error -> {
                            isFailure(RequestError.UNSUCCESS_STATUS, details.exception.message ?: "")
                        }
                    }

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
                    val video = additionallyRepository.getVideos(id)
                    when (video) {
                        is DataState.Success -> {
                            moviesVideos.postValue(video.data)
                        }
                        is DataState.Error -> {
                            isFailure(RequestError.UNSUCCESS_STATUS, video.exception.message ?: "")
                        }
                    }

                }
            )
        )
        getFavoriteList()
    }

    fun startFindFilmUrl(title: String, date: String?) {
        findByWebView(title, date)
        findByFireStore(title, date)
    }

    private fun getFavoriteList() {
        firestoreManager.getFavorite({
            favoriteIdList.postValue(it)
        }, {

        })
    }

    fun addToFavorite(movieId: String) {
        val movieMap = favoriteIdList.value ?: hashMapOf()

        if (!(movieMap.get(FirebaseCloudFirestoreManagers.MOVIE)?.contains(movieId) ?: false)) {
            firestoreManager.addToFavorite(movieId, movieMap, {
                favoriteIdList.postValue(it)
                updateFavoriteStatus.postValue((FavoriteState(true, true)))
            }, {
                updateFavoriteStatus.postValue((FavoriteState(false, false)))
            })
        }
    }

    fun removeFromFavorite(movieId: String) {
        val movieMap = favoriteIdList.value ?: hashMapOf()

        if (movieMap.get(FirebaseCloudFirestoreManagers.MOVIE)?.contains(movieId) ?: false) {
            firestoreManager.removeFromFavorite(movieId, movieMap, {
                favoriteIdList.postValue(it)
                updateFavoriteStatus.postValue((FavoriteState(true, false)))
            }, {
                updateFavoriteStatus.postValue((FavoriteState(false, true)))
            })
        }
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

                override fun onVideoNotFind() {

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

    override fun onCleared() {
        findFilmFilmixController.onDestroy()
        super.onCleared()
    }
}