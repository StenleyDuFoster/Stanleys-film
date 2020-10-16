package com.stenleone.stanleysfilm.viewModel.network

import androidx.lifecycle.MutableLiveData
import com.stenleone.stanleysfilm.managers.SharedPreferencesManager
import com.stenleone.stanleysfilm.model.entity.RequestError
import com.stenleone.stanleysfilm.network.ApiService
import com.stenleone.stanleysfilm.network.TmdbNetworkConstant.LIST_MOVIE_NOW_PLAYING
import com.stenleone.stanleysfilm.network.TmdbNetworkConstant.LIST_MOVIE_POPULAR
import com.stenleone.stanleysfilm.network.TmdbNetworkConstant.LIST_MOVIE_TOP_RATED
import com.stenleone.stanleysfilm.network.TmdbNetworkConstant.LIST_MOVIE_TOP_UPCOMING
import com.stenleone.stanleysfilm.network.entity.lates.LatesEntity
import com.stenleone.stanleysfilm.network.entity.movie.MoviesEntity
import com.stenleone.stanleysfilm.viewModel.base.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers

class MainViewModel(
    val apiService: ApiService,
    val sharedPreferencesManager: SharedPreferencesManager
) : BaseViewModel() {

    val movieLatesLiveData = MutableLiveData<LatesEntity>()
    val movieNowPlayingLiveData = MutableLiveData<MoviesEntity>()
    val moviePopularLiveData = MutableLiveData<MoviesEntity>()
    val movieTopRatedLiveData = MutableLiveData<MoviesEntity>()
    val movieUpcomingLiveData = MutableLiveData<MoviesEntity>()

    val errorLiveData = MutableLiveData<RequestError>()

    val isFailure: (it: String) -> Unit = {
        errorLiveData.postValue(RequestError(RequestError.REQUEST_ERROR, message = it))
    }

    init {
        loadContent()
    }

    fun loadContent() {
        getLatesMovie()
        getListMovie(LIST_MOVIE_NOW_PLAYING)
        getListMovie(LIST_MOVIE_POPULAR)
        getListMovie(LIST_MOVIE_TOP_RATED)
        getListMovie(LIST_MOVIE_TOP_UPCOMING)
    }

    fun getLatesMovie(page: Int = 1) {
        apiService.getLatesMovie(
            language = sharedPreferencesManager.language,
            page = page
        )
            .observeOn(Schedulers.io())
            .subscribeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { response ->
                    if (response.isSuccessful) {
                        response.body()?.let {
                            movieLatesLiveData.postValue(it)
                        }
                    } else {
                        isFailure(response.body().toString())
                    }
                }, {
                    isFailure(it.message.toString())
                }
            )
            .addTo(compositeDisposable)
    }

    fun getListMovie(typeList: String, page: Int = 1) {
        apiService.getListMovie(
            typeList,
            language = sharedPreferencesManager.language,
            page = page
        )
            .observeOn(Schedulers.io())
            .subscribeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { response ->
                    if (response.isSuccessful) {
                        response.body()?.let {
                            postValue(typeList, it)
                        }
                    } else {
                        isFailure(response.body().toString())
                    }
                }, {
                    isFailure(it.message.toString())
                }
            )
            .addTo(compositeDisposable)
    }

    private fun postValue(
        type: String,
        movies: MoviesEntity
    ) {
        when (type) {
            LIST_MOVIE_NOW_PLAYING -> {
                movieNowPlayingLiveData.postValue(movies)
            }
            LIST_MOVIE_POPULAR -> {
                moviePopularLiveData.postValue(movies)
            }
            LIST_MOVIE_TOP_RATED -> {
                movieTopRatedLiveData.postValue(movies)
            }
            LIST_MOVIE_TOP_UPCOMING -> {
                movieUpcomingLiveData.postValue(movies)
            }
        }
    }
}