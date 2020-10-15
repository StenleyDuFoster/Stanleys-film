package com.stenleone.stanleysfilm.viewModel.network

import androidx.lifecycle.MutableLiveData
import com.stenleone.stanleysfilm.network.ApiService
import com.stenleone.stanleysfilm.network.TmdbNetworkConstant.LIST_MOVIE_NOW_PLAYING
import com.stenleone.stanleysfilm.network.TmdbNetworkConstant.LIST_MOVIE_POPULAR
import com.stenleone.stanleysfilm.network.TmdbNetworkConstant.LIST_MOVIE_TOP_RATED
import com.stenleone.stanleysfilm.network.TmdbNetworkConstant.LIST_MOVIE_TOP_UPCOMING
import com.stenleone.stanleysfilm.network.entity.lates.LatesEntity
import com.stenleone.stanleysfilm.network.entity.movie.MovieEntity
import com.stenleone.stanleysfilm.viewModel.base.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers

class MainViewModel(
    val apiService: ApiService
) : BaseViewModel() {

    val movieLatesLiveData = MutableLiveData<LatesEntity>()
    val movieNowPlayingLiveData = MutableLiveData<MovieEntity>()
    val moviePopularLiveData = MutableLiveData<MovieEntity>()
    val movieTopRatedLiveData = MutableLiveData<MovieEntity>()
    val movieUpcomingLiveData = MutableLiveData<MovieEntity>()

    init {
        getLatesMovie()
        getListMovie(LIST_MOVIE_NOW_PLAYING)
        getListMovie(LIST_MOVIE_POPULAR)
        getListMovie(LIST_MOVIE_TOP_RATED)
        getListMovie(LIST_MOVIE_TOP_UPCOMING)
    }

    fun getLatesMovie(page: Int = 1) {
        apiService.getLatesMovie(
            language = "ru",
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
                    }
                }, {

                }
            )
            .addTo(compositeDisposable)
    }

    fun getListMovie(typeList: String, page: Int = 1) {
        apiService.getListMovie(
            typeList,
            language = "ru",
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
                    }
                }, {

                }
            )
            .addTo(compositeDisposable)
    }

    private fun postValue(
        type: String,
        movie: MovieEntity
    ) {
        when (type) {
            LIST_MOVIE_NOW_PLAYING -> {
                movieNowPlayingLiveData.postValue(movie)
            }
            LIST_MOVIE_POPULAR -> {
                moviePopularLiveData.postValue(movie)
            }
            LIST_MOVIE_TOP_RATED -> {
                movieTopRatedLiveData.postValue(movie)
            }
            LIST_MOVIE_TOP_UPCOMING -> {
                movieUpcomingLiveData.postValue(movie)
            }
        }
    }
}