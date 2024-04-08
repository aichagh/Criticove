package com.criticove.api

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

// TMDB API service interface

interface TmdbApiService {
    @GET("search/movie")
    suspend fun searchMovies(@Query("api_key") apiKey: String, @Query("query") query: String): MovieResponse

    @GET("search/tv")
    suspend fun searchTvShows(@Query("api_key") apiKey: String, @Query("query") query: String): TvShowResponse

    @GET("tv/{series_id}")
    suspend fun getTVDetail(@Path("series_id") seriesId: Int, @Query("api_key") apiKey: String): TvShowDetail

    @GET("movie/{movie_id}")
    suspend fun getMovieDetail(@Path("movie_id") movieId: Int, @Query("api_key") apiKey: String): MovieDetail
}

// Data models
data class Genre(val id: Int, val name: String)

data class Network(val name: String)

data class MovieResponse(val results: List<Movie>)
data class TvShowResponse(val results: List<TvShow>)
data class MovieDetail(
    val id: Int,
    val title: String,
    val release_date: String,
    val genres: List<Genre>,
    val adult: Boolean)
data class TvShowDetail(
    val id: Int,
    val name: String,
    val first_air_date:
    String, val genres: List<Genre>,
    val networks: List<Network>,
    val adult: Boolean)

data class Movie(
    val id: Int,
    val title: String,
    val release_date: String
)

data class TvShow(
    val id: Int,
    val name: String,
    val first_air_date: String
)


