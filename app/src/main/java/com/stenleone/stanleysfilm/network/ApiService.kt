package com.stenleone.stanleysfilm.network

import com.stenleone.stanleysfilm.network.ApiConstant.API_SORT
import com.stenleone.stanleysfilm.network.ApiConstant.API_V3
import com.stenleone.stanleysfilm.network.TmdbNetworkConstant.GET_IMAGE
import com.stenleone.stanleysfilm.network.TmdbNetworkConstant.GET_SESSION
import com.stenleone.stanleysfilm.network.TmdbNetworkConstant.GET_VIDEOS
import com.stenleone.stanleysfilm.network.TmdbNetworkConstant.LIKE_MOVIE
import com.stenleone.stanleysfilm.network.TmdbNetworkConstant.LIST_LIKE_MOVIE
import com.stenleone.stanleysfilm.network.TmdbNetworkConstant.LIST_MOVIE
import com.stenleone.stanleysfilm.network.TmdbNetworkConstant.LIST_MOVIE_LATES
import com.stenleone.stanleysfilm.network.TmdbNetworkConstant.LIST_RECOMENDED
import com.stenleone.stanleysfilm.network.TmdbNetworkConstant.LIST_SIMILAR
import com.stenleone.stanleysfilm.network.TmdbNetworkConstant.MOVIE_DETAILS
import com.stenleone.stanleysfilm.network.TmdbNetworkConstant.SEARCH_MOVIE
import com.stenleone.stanleysfilm.network.entity.Videos
import com.stenleone.stanleysfilm.network.entity.images.ImagesEntityUI
import com.stenleone.stanleysfilm.network.entity.lates.LatesEntityUI
import com.stenleone.stanleysfilm.network.entity.movie.MovieDetailsEntityUI
import com.stenleone.stanleysfilm.network.entity.movie.MoviesEntity
import com.stenleone.stanleysfilm.network.entity.movie.MoviesEntityUI
import com.stenleone.stanleysfilm.network.entity.tmdb_auth.GuestSessionEntityUI
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @GET(LIST_MOVIE)
    fun getListMovieAsync(
        @Path("search") search: String,
        @Query("api_key") api_key: String = API_V3,
        @Query("language") language: String,
        @Query("page") page: Int
    ): Deferred<Response<MoviesEntity>>

    @GET(LIST_MOVIE)
    fun getLatesMovieAsync(
        @Path("search") search: String = LIST_MOVIE_LATES,
        @Query("api_key") api_key: String = API_V3,
        @Query("language") language: String,
        @Query("page") page: Int = 1
    ): Deferred<Response<LatesEntityUI>>

    @GET(MOVIE_DETAILS)
    fun getMovieDetails(
        @Path("movie_id") movieId: Int,
        @Query("api_key") api_key: String = API_V3,
        @Query("language") language: String
    ): Deferred<Response<MovieDetailsEntityUI>>

    @GET(LIST_RECOMENDED)
    fun getRecomendedList(
        @Path("movie_id") movieId: Int,
        @Query("api_key") api_key: String = API_V3,
        @Query("language") language: String,
        @Query("page") page: Int = 1
    ): Deferred<Response<MoviesEntityUI>>

    @GET(LIST_SIMILAR)
    fun getSimilarList(
        @Path("movie_id") movieId: Int,
        @Query("api_key") api_key: String = API_V3,
        @Query("language") language: String,
        @Query("page") page: Int = 1
    ): Deferred<Response<MoviesEntityUI>>

    @GET(GET_VIDEOS)
    fun getVideos(
        @Path("movie_id") movieId: Int,
        @Query("api_key") api_key: String = API_V3,
        @Query("language") language: String
    ): Deferred<Response<Videos>>

    @GET(LIST_LIKE_MOVIE)
    fun getLikeMovieAsync(
        @Path("session_id") session_id: String,
        @Query("api_key") api_key: String = API_V3,
        @Query("language") language: String,
        @Query("page") page: Int,
        @Query("sort_by") sort_by: String = API_SORT
    ): Deferred<Response<String>>

    @GET(GET_SESSION)
    fun getSessionAsync(
        @Query("api_key") api_key: String = API_V3
    ): Deferred<Response<GuestSessionEntityUI>>

    @GET(GET_IMAGE)
    fun getImageList(
        @Path("movie_id") movieId: Int,
        @Query("language") language: String,
        @Query("api_key") api_key: String = API_V3
    ): Deferred<Response<ImagesEntityUI>>

    @POST(LIKE_MOVIE)
    fun postLikeMovieAsync(
        @Path("movie_id") movie_id: Int,
        @Query("api_key") api_key: String = API_V3,
        @Query("guest_session_id") guest_session_id: String,
        @Body userData: String
    ): Deferred<Response<String>>

    @DELETE(LIKE_MOVIE)
    fun deleteLikeMovieAsync(
        @Path("movie_id") movie_id: Int,
        @Query("api_key") api_key: String = API_V3,
        @Query("guest_session_id") guest_session_id: String
    ): Deferred<Response<String>>

    @GET(SEARCH_MOVIE)
    fun searchMovie(
        @Query("query") search: String,
        @Query("api_key") api_key: String = API_V3,
        @Query("language") language: String,
        @Query("page") page: Int,
        @Query("include_adult") includeAdult: Boolean = true
    ): Deferred<Response<MoviesEntityUI>>

}