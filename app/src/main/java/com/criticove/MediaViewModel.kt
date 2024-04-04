package com.criticove

import androidx.lifecycle.MutableLiveData
import android.util.Log
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.criticove.api.BookItem
import com.criticove.api.GoogleBooksApiService
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

    val isAdultContent = MutableLiveData<Boolean>(false)


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
                movieDetails.postValue(response)
                isAdultContent.postValue(response.adult)
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
                isAdultContent.postValue(response.adult)
            } catch (e: Exception) {
                Log.e("MediaViewModel", "Error fetching TV show details", e)
            }
        }
    }

    private val googleBooksApiService: GoogleBooksApiService by lazy {
        Retrofit.Builder()
            .baseUrl("https://www.googleapis.com/books/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GoogleBooksApiService::class.java)
    }

    val bookSuggestions: MutableLiveData<List<BookItem>> = MutableLiveData(emptyList())
    val selectedBookDetails: MutableLiveData<BookItem?> = MutableLiveData(null)
    fun searchBookTitles(query: String) {
        val BOOK_API = "AIzaSyBtP-ywLo6XXLl-D3x3YjhECEPVGwRmJY8"
        viewModelScope.launch {
            try {
                    val response = googleBooksApiService.searchBooks(query, BOOK_API)
                    bookSuggestions.postValue(response.items ?: emptyList())
            } catch (e: Exception) {
                Log.e("MediaViewModel", "Error searching book titles", e)
            }
        }
    }

    fun selectBook(bookId: String) {
        val selectedBook = bookSuggestions.value?.find { it.id == bookId }
        selectedBookDetails.postValue(selectedBook)
    }

}
