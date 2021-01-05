package com.stenleone.stanleysfilm.network

import com.stenleone.stanleysfilm.network.ApiConstant.API_SORT
import com.stenleone.stanleysfilm.network.ApiConstant.API_V3
import com.stenleone.stanleysfilm.network.TmdbNetworkConstant.DELETE_LIKE_MOVIE
import com.stenleone.stanleysfilm.network.TmdbNetworkConstant.GET_SESSION
import com.stenleone.stanleysfilm.network.TmdbNetworkConstant.LIST_LIKE_MOVIE
import com.stenleone.stanleysfilm.network.TmdbNetworkConstant.LIST_MOVIE
import com.stenleone.stanleysfilm.network.TmdbNetworkConstant.LIST_MOVIE_LATES
import com.stenleone.stanleysfilm.network.TmdbNetworkConstant.POST_LIKE_MOVIE
import com.stenleone.stanleysfilm.network.entity.lates.LatesEntity
import com.stenleone.stanleysfilm.network.entity.movie.MoviesEntity
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
    ): Deferred<Response<LatesEntity>>

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
    ): Deferred<Response<String>>

    @POST(POST_LIKE_MOVIE)
    fun postLikeMovieAsync(
        @Path("movie_id") movie_id: Int,
        @Query("api_key") api_key: String = API_V3,
        @Query("guest_session_id") guest_session_id: String,
        @Body userData: String
    ): Deferred<Response<String>>

    @DELETE(DELETE_LIKE_MOVIE)
    fun deleteLikeMovieAsync(
        @Path("movie_id") movie_id: Int,
        @Query("api_key") api_key: String = API_V3,
        @Query("guest_session_id") guest_session_id: String
    ): Deferred<Response<String>>

}