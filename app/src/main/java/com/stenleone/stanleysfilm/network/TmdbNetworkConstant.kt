package com.stenleone.stanleysfilm.network

object TmdbNetworkConstant {

    const val BASE_URL = "https://api.themoviedb.org/3/"
    const val BASE_IMAGE_URL = "https://image.tmdb.org/t/p/w500"

    const val LIST_MOVIE = "movie/{search}"
    const val LIST_MOVIE_LATES = "latest"
    const val LIST_MOVIE_NOW_PLAYING = "now_playing"
    const val LIST_MOVIE_POPULAR = "popular"
    const val LIST_MOVIE_TOP_RATED = "top_rated"
    const val LIST_MOVIE_TOP_UPCOMING = "upcoming"
    const val MOVIE_DETAILS = "movie/{movie_id}"
    const val LIST_RECOMENDED = "$MOVIE_DETAILS/recommendations"
    const val LIST_SIMILAR = "$MOVIE_DETAILS/similar"
    const val LIST_LIKE_MOVIE = "guest_session/{session_id}/rated/movies?"
    const val GET_SESSION = "authentication/guest_session/new?"
    const val LIKE_MOVIE = "$MOVIE_DETAILS/rating?"
    const val SEARCH_MOVIE = "search/movie"
    const val GET_IMAGE = "$MOVIE_DETAILS/images"
    const val GET_VIDEOS = "$MOVIE_DETAILS/videos"
}