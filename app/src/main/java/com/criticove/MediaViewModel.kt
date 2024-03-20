package com.criticove
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.criticove.api.Movie
import com.criticove.api.MovieDetail
import com.criticove.api.TmdbApiService
import com.criticove.api.TvShow
import com.criticove.api.TvShowDetail
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
class MediaViewModel: ViewModel() {

    companion object {
        const val API_KEY = "ed52425c0609ecf79ba558dcfbafb535"
    }

    private val tmdbApiService: TmdbApiService by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TmdbApiService::class.java)
    }

    val movieSuggestions = MutableLiveData<List<Movie>>(emptyList())
    val tvShowSuggestions = MutableLiveData<List<TvShow>>(emptyList())
    val movieDetails = MutableLiveData<MovieDetail?>()
    val tvShowDetails = MutableLiveData<TvShowDetail?>()



    fun searchMovieTitles(query: String) {
        viewModelScope.launch {
            try {
                val response = tmdbApiService.searchMovies(API_KEY, query)
                movieSuggestions.postValue(response.results)
            } catch (e: Exception) {
                Log.e("MediaViewModel", "Error searching movie titles", e)
                // Optionally update your LiveData to indicate an error state
            }
        }
    }

    fun searchTvShowTitles(query: String) {
        viewModelScope.launch {
            try {
                val response = tmdbApiService.searchTvShows(API_KEY, query)
                tvShowSuggestions.postValue(response.results)
            } catch (e: Exception) {
                Log.e("MediaViewModel", "Error searching TV show titles", e)
                // Optionally update your LiveData to indicate an error state
            }
        }
    }

    fun fetchMovieDetails(movieId: Int) {
        viewModelScope.launch {
            try {
                val response = tmdbApiService.getMovieDetail(movieId, API_KEY)
                // Post the detailed movie object to LiveData
                movieDetails.postValue(response)
            } catch (e: Exception) {
                Log.e("MediaViewModel", "Error fetching movie details", e)
            }
        }
    }

    fun fetchTvShowDetails(tvShowId: Int) {
        viewModelScope.launch {
            try {
                val response = tmdbApiService.getTVDetail(tvShowId, API_KEY)
                // Post the detailed TV show object to LiveData
                tvShowDetails.postValue(response)
            } catch (e: Exception) {
                Log.e("MediaViewModel", "Error fetching TV show details", e)
            }
        }
    }

}
