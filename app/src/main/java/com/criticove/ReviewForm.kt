@file:OptIn(ExperimentalMaterial3Api::class)

package com.criticove


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerColors
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.criticove.api.Movie
import com.criticove.api.TvShow
import com.criticove.backend.SubmittedReview
import com.criticove.backend.userModel
import com.criticove.api.TvShowDetail
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import com.criticove.api.BookItem
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import kotlin.math.abs


val filled = mutableMapOf(
    "Book" to mutableMapOf("Book Title" to "", "Author" to "", "Year Published" to "", "Genre" to "", "Book Type" to "", "Date finished" to ""),
    "TV Show" to mutableMapOf("TV Show Title" to "", "Year Released" to "", "Genre" to "", "Streaming Service" to "", "Date finished" to ""),
    "Movie" to mutableMapOf("Movie Title" to "", "Year Released" to "", "Genre" to "", "Streaming Service" to "", "Date watched" to ""))

var reviewScore = 1
var submittedReview: MutableMap<String, String>? = null
var shared: Boolean = false

val genreList = listOf<String>("Romance", "Thriller", "Drama", "Autobiography", "Sci-fi")
val serviceList = listOf<String>("Netflix", "Apple TV", "Prime", "Hulu", "HBO", "Other")

class ReviewForm : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            NavHost(navController, startDestination = "ReviewForm") {
                composable("ReviewForm") {
                    //ReviewFormMainContent(navController)
                }
                composable("Reviews") {
                    //ReviewPageMainContent(navController)
                }
            }
        }
    }
}

@Composable
fun ReviewFormMainContent(navController: NavController, userModel: userModel) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(colorResource(id = R.color.off_white)),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        ReviewHeader()
        Column (modifier = Modifier.verticalScroll(rememberScrollState())) {
            Selection(userModel, navController)
        }
        println("this is filled $filled")
    }
}

@Composable
fun ReviewHeader() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .background(colorResource(id = R.color.blue))
            .padding(5.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "New Review",
            color = colorResource(id = R.color.white),
            fontSize = 30.sp,
            fontFamily = FontFamily(Font(R.font.alegreya_sans_bold))
        )
    }
}

sealed class Suggestion {
    abstract val displayText: String
    abstract val displayDate: String
    abstract val genre: String?

    data class MovieSuggestion(
        val id: Int,
        override val displayText: String,
        override val displayDate: String,
        override val genre: String? = null
    ) : Suggestion()

    data class TvShowSuggestion(
        val id: Int,
        override val displayText: String,
        override val displayDate: String,
        override val genre: String? = null
    ) : Suggestion()

    data class BookSuggestion(
        val id: String,
        override val displayText: String,
        override val displayDate: String,
        override val genre: String?
    ) : Suggestion()

}

val Movie.suggestion: Suggestion.MovieSuggestion
    get() = Suggestion.MovieSuggestion(
        id = this.id,
        displayText = this.title,
        displayDate = this.release_date.substringBefore("-"),
    )

val TvShow.suggestion: Suggestion.TvShowSuggestion
    get() = Suggestion.TvShowSuggestion(
        id = this.id,
        displayText = this.name,
        displayDate = this.first_air_date.substringBefore("-"),
    )

val BookItem.suggestion: Suggestion.BookSuggestion
    get() = Suggestion.BookSuggestion(
        id = this.id,
        displayText = this.volumeInfo.title?: "",
        displayDate = this.volumeInfo.publishedDate.substringBefore("-")?: "",
        genre = this.volumeInfo.categories?.firstOrNull()?: "Other"
    )

@ExperimentalMaterial3Api
@Composable
fun AutocompleteTextField(
    label: String,
    viewModel: MediaViewModel,
//    onSuggestionSelected: (Suggestion) -> Unit,
    type: String
) {
    val focusRequester = remember { FocusRequester() }
    val debouncePeriod = 300L
    val coroutineScope = rememberCoroutineScope()
    var searchJob by remember { mutableStateOf<Job?>(null) }

    var query by remember { mutableStateOf("") }
    var isExpanded by remember { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current

    val movieSuggestions by viewModel.movieSuggestions.observeAsState()
    val tvShowSuggestions by viewModel.tvShowSuggestions.observeAsState()
    val bookSuggestions by viewModel.bookSuggestions.observeAsState()


    val suggestions: List<Suggestion> = when (type) {
        "Movie" -> movieSuggestions?.map { it.suggestion } ?: emptyList()
        "Book" -> bookSuggestions?.map { it.suggestion } ?: emptyList()
        else -> tvShowSuggestions?.map { it.suggestion } ?: emptyList()
    }

    OutlinedTextField(
        value = query,
        onValueChange = {newValue ->
            query = newValue
            isExpanded = newValue.isNotEmpty()
            searchJob?.cancel()
            searchJob = coroutineScope.launch {
                try {
                    delay(debouncePeriod) // Debounce delay
                    if (query == newValue) { // Check if the query hasn't changed
                        if (type == "Movie") {
                            viewModel.searchMovieTitles(newValue)
                        } else if (type == "TV Show"){
                            viewModel.searchTvShowTitles(newValue)
                        } else {
                            viewModel.searchBookTitles(newValue)
                        }
                    }
                } catch (e: Exception) {
                println("Error in coroutine: ${e.message}")
            }
            }
        },
        label = { Text(text = label,
            color = colorResource(id = R.color.coolGrey),
            fontFamily = FontFamily(Font(R.font.alegreya_sans_regular))
        ) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp)
            .focusRequester(focusRequester),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = colorResource(id = R.color.blue),
            unfocusedBorderColor = colorResource(id = R.color.teal)
        ),
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(onDone = {
            keyboardController?.hide()
        }),
        shape = RoundedCornerShape(10.dp)
    )

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    if (suggestions.isNotEmpty() && isExpanded) {
        DropdownMenu(
            expanded = isExpanded,
            onDismissRequest = { isExpanded = false },
            modifier = Modifier
                .background(colorResource(id = R.color.off_white))
                .fillMaxWidth()
        ) {
            suggestions.take(5).forEach { suggestion ->
                DropdownMenuItem(
                    onClick = {
                        query = suggestion.displayText
                        isExpanded = false
                        when(suggestion) {
                            is Suggestion.MovieSuggestion -> viewModel.fetchMovieDetails(suggestion.id)
                            is Suggestion.TvShowSuggestion -> viewModel.fetchTvShowDetails(suggestion.id)
                            is Suggestion.BookSuggestion -> viewModel.selectBook(suggestion.id)
                        }
                    },
                    text = {
                        Text("${suggestion.displayText} (${suggestion.displayDate})",
                        fontFamily = FontFamily(Font(R.font.alegreya_sans_regular)),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxSize(),
                        color = colorResource(id = R.color.black))
                    },
                    modifier = Modifier
                        .background(colorResource(id = R.color.off_white)),
                    )
            }
        }
    }

    filled[type]?.set(label, query).toString()
}

@Composable
fun Selection(userModel: userModel, navController: NavController) {
    var selectedType by remember { mutableStateOf("Book") }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(30.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        val mediaType = listOf("Book", "TV Show", "Movie")
        mediaType.forEach{ el ->
            Row(
                modifier = Modifier
                    .fillMaxHeight()
            ) {
                RadioButton(
                    selected = selectedType == el,
                    onClick = { selectedType = el},
                    colors = RadioButtonDefaults.colors(
                        selectedColor = colorResource(id = R.color.blue),
                        unselectedColor = colorResource(id = R.color.blue),
                    )
                )
                var id = R.drawable.movie_black
                when(el) {
                    "Book" -> id = R.drawable.book_black
                    "TV Show" -> id = R.drawable.tv_black
                }
                Icon(imageVector = ImageVector.vectorResource(id = id),
                    contentDescription = el )
            }
        }
    }
    val mediaViewModel: MediaViewModel = viewModel()
    CreateForm(selectedType, userModel, navController, mediaViewModel)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateForm(type:String, userModel: userModel, navController: NavController, mediaViewModel: MediaViewModel) {
    var elements = mutableListOf<String>()
    var text by remember { mutableStateOf("") }

    when (type) {
        "Book" -> elements =
            listOf("Book Title", "Author", "Year Published", "Genre", "Book Type", "Date finished").toMutableList()
        "TV Show" -> elements = listOf(
            "TV Show Title",
            "Director",
            "Year Released",
            "Genre",
            "Streaming Service",
            "Date finished"
        ).toMutableList()
        "Movie" -> elements = listOf(
            "Movie Title",
            "Year Released",
            "Genre",
            "Streaming Service",
            "Date watched"
        ).toMutableList()
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(colorResource(id = R.color.off_white)),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    )
    {
        when (type) {
            "Book" -> {
                BookForm(mediaViewModel)
                reviewText("Book")
            }
            "TV Show" -> {
                TVShowForm(mediaViewModel)
                reviewText("TV Show")

            }
            "Movie" -> {
                MovieForm(mediaViewModel)
                reviewText("Movie")
            }
        }
        /*
        OutlinedTextField(
            value = text,
            onValueChange = { text = it },
            minLines = 7,
            label = {
                Text(
                    text = "Review",
                    color = colorResource(id = R.color.coolGrey),
                    fontFamily = FontFamily(Font(R.font.alegreya_sans_regular))
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = colorResource(id = R.color.blue),
                unfocusedBorderColor = colorResource(id = R.color.teal)
            ),
            shape = RoundedCornerShape(10.dp)
        )

         */


    }
    println("this is filled $filled")
    StarRating(type)
    Submission(type, navController)
}

@Composable
fun Submission(type: String, navController: NavController) {
    var shareOption by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
    ) {
        Text(
            text = "Do you want to share your review with your friends?",
            fontFamily = FontFamily(Font(R.font.alegreya_sans_medium)),
            fontSize = 18.sp
        )

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = shareOption == false,
                    onClick = { shareOption = false },
                    colors = RadioButtonDefaults.colors(
                        selectedColor = colorResource(id = R.color.blue),
                        unselectedColor = colorResource(id = R.color.blue),
                    )
                )

                Text(
                    text = "Keep private",
                    fontFamily = FontFamily(Font(R.font.alegreya_sans_regular)),
                    fontSize = 18.sp
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = shareOption == true,
                    onClick = { shareOption = true },
                    colors = RadioButtonDefaults.colors(
                        selectedColor = colorResource(id = R.color.blue),
                        unselectedColor = colorResource(id = R.color.blue),
                    )
                )

                Text(
                    text = "Share with friends",
                    fontFamily = FontFamily(Font(R.font.alegreya_sans_regular)),
                    fontSize = 18.sp
                )
            }
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Button(
            onClick = {
                //TODO : CLEAR UP THE MAP VALUES/ALL FORM VALUES
                navController.navigate("Reviews")

            },
            colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(id = R.color.teal),
                contentColor = colorResource(id = R.color.off_white)
            ),
            modifier = Modifier
                .weight(1F)
                .padding(10.dp)
        ) { Text(
            text = "Cancel",
            fontFamily = FontFamily(Font(R.font.alegreya_sans_bold)),
            fontSize = 20.sp
        ) }

        Button(
            onClick = {
                when (type) {
                    "Book" -> submittedReview = filled["Book"]
                    "TV Show" -> submittedReview = filled["TV Show"]
                    "Movie" -> submittedReview = filled["Movie"]
                }
                submittedReview?.let { SubmittedReview(type, reviewScore, shareOption, it)
                    navController.navigate("Reviews")
                }
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(id = R.color.teal),
                contentColor = colorResource(id = R.color.off_white)),
            modifier = Modifier
                .weight(1F)
                .padding(10.dp),
        ) { Text(
            text = "Share",
            fontFamily = FontFamily(Font(R.font.alegreya_sans_bold)),
            fontSize = 20.sp
        ) }
    }
}


@Composable
fun StarRating(type: String) {
    var bookScore by remember { mutableIntStateOf(1) }
    var tvScore by remember { mutableIntStateOf(1) }
    var movieScore by remember { mutableIntStateOf(1) }
    var id = R.drawable.star_empty
    Column ( modifier = Modifier
        .padding(10.dp)
        .fillMaxWidth()) {
        Text(
            text = "Rating",
            modifier= Modifier.fillMaxWidth(),
            fontFamily = FontFamily(Font(R.font.alegreya_sans_medium)),
            fontSize = 18.sp
        )
        Row() {
            for (i in 1..5) {
                when (type) {
                    "Book" -> {
                        if (i <= bookScore) {
                            id = R.drawable.star_full
                        } else {
                            id = R.drawable.star_empty
                        }
                        Icon(
                            imageVector = ImageVector.vectorResource(id = id),
                            contentDescription = "Star $i",
                            modifier = Modifier
                                .padding(3.dp)
                                .clickable(onClick = {
                                    bookScore = i
                                    reviewScore = bookScore
                                })
                                .size(32.dp)
                        )
                    }

                    "TV Show" -> {
                        if (i <= tvScore) {
                            id = R.drawable.star_full
                        } else {
                            id = R.drawable.star_empty
                        }
                        Icon(
                            imageVector = ImageVector.vectorResource(id = id),
                            contentDescription = "Star $i",
                            modifier = Modifier
                                .padding(3.dp)
                                .clickable(onClick = {
                                    tvScore = i
                                    reviewScore = tvScore
                                })
                                .size(32.dp)
                        )
                    }

                    "Movie" -> {
                        if (i <= movieScore) {
                            id = R.drawable.star_full
                        } else {
                            id = R.drawable.star_empty
                        }
                        Icon(
                            imageVector = ImageVector.vectorResource(id = id),
                            contentDescription = "Star $i",
                            modifier = Modifier
                                .padding(3.dp)
                                .clickable(onClick = {
                                    movieScore = i
                                    reviewScore = movieScore
                                })
                                .size(32.dp)
                        )
                    }

                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewCreateReview() {
//    ReviewFormMainContent(navController = rememberNavController())
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookForm(mediaViewModel: MediaViewModel) {
    // Fields: "Book Title","Author", "Date Published", "Genre", "Book Type"
    val bookDetails by mediaViewModel.selectedBookDetails.observeAsState()
    var bookTitle by remember { mutableStateOf("") }
    var author by remember { mutableStateOf("") }
    var yearPublished by remember { mutableStateOf("") }
    var selectedGenre by remember { mutableStateOf("") }
    val genreList = listOf<String>("Romance", "Thriller", "Drama", "Autobiography", "Sci-fi")
    val typeList = listOf<String>("Physical", "E-Book")
    var selectedType by remember { mutableStateOf("") }


    AutocompleteTextField (
        label = "Book Title",
        viewModel = mediaViewModel,
        type = "Book"
    )

    LaunchedEffect(bookDetails) {
        bookDetails?.let {
            bookTitle = it.volumeInfo.title
            author = it.volumeInfo.authors?.joinToString(", ") ?: ""
            yearPublished = it.volumeInfo.publishedDate.substringBefore("-") ?: ""
            selectedGenre = it.volumeInfo.categories?.firstOrNull() ?: ""
        }
    }

    normalText(field = "Author", type = "Book", initialValue = author,  onValueChange = { author = it })
    normalNumber(field = "Year Published", type = "Book", initialValue = yearPublished,  onValueChange = { yearPublished = it })
    Dropdown(type = "Book", field = "Genre", list = genreList, selectedGenre) { selectedGenre = it }
   Dropdown(type = "Book", field = "Book Type", list = typeList, selectedType) { selectedType = it }
    datePicker(type = "Book", field = "Date finished")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TVShowForm(mediaViewModel: MediaViewModel) {
    val tvShowDetails by mediaViewModel.tvShowDetails.observeAsState()
    var tvShowTitle by remember { mutableStateOf("") }
    var yearReleased by remember { mutableStateOf("") }
    var selectedGenre by remember { mutableStateOf("") }
    val genreList = listOf("Drama", "Comedy", "Action", "Fantasy", "Science Fiction")
    val updatedGenreList = remember { mutableStateListOf(*genreList.toTypedArray()) }
    var selectedService by remember { mutableStateOf("") }

    AutocompleteTextField (
        label = "TV Show Title",
        viewModel = mediaViewModel,
        type = "TV Show"
    )

    LaunchedEffect(tvShowDetails) {
        tvShowDetails?.let {
            tvShowTitle = it.name
            yearReleased = it.first_air_date.substringBefore("-")
            // Update genre list if the first genre isn't in the static list
            if (it.genres.isNotEmpty()) {
                val firstgenre = it.genres.first().name
                if (firstgenre !in genreList) {
                    updatedGenreList.clear()
                    updatedGenreList.addAll(genreList) // Reset to default and add fetched genre
                    updatedGenreList.add(0, firstgenre)
                    selectedGenre = "Select a genre" // Reset to placeholder on new selection
                }
                selectedGenre = firstgenre
                println("the dropdown $selectedGenre")
            }
        }
    }

//    "TV Show Title", "Director", "Date Released", "Genre", "Streaming Service"
    normalNumber(field = "Year Released", type = "TV Show", initialValue = yearReleased, onValueChange = { yearReleased = it })
    Dropdown(type = "TV Show", field = "Genre", list = updatedGenreList, selectedGenre) { selectedGenre = it }
    Dropdown(type = "TV Show", field = "Streaming Service", list = serviceList, selectedService) { selectedService = it }
//    dateField(field = "Date finished", type = "TV Show")
    datePicker(type = "TV Show", field = "Date finished")
}

@Composable
fun MovieForm(mediaViewModel: MediaViewModel) {
    val movieDetails by mediaViewModel.movieDetails.observeAsState()
    var movieTitle by remember { mutableStateOf("") }
    var yearReleased by remember { mutableStateOf("") }
    var selectedGenre by remember { mutableStateOf("") }
    val genreList = listOf("Romance", "Thriller", "Drama", "Autobiography", "Sci-fi")
    val updatedGenreList = remember { mutableStateListOf(*genreList.toTypedArray()) }
    var selectedService by remember { mutableStateOf("") }
    var auto by remember { mutableStateOf("false") }

    AutocompleteTextField(
        label = "Movie Title",
        viewModel = mediaViewModel,
        type = "Movie"
    )

    LaunchedEffect(movieDetails) {
        movieDetails?.let {
            movieTitle = it.title
            yearReleased = it.release_date.substringBefore("-") // Assuming YYYY-MM-DD format
            // Update genre list if the first genre isn't in the static list
            if (it.genres.isNotEmpty()) {
                val firstgenre = it.genres.first().name
                if (firstgenre !in genreList) {
                    updatedGenreList.clear()
                    updatedGenreList.addAll(genreList) // Reset to default and add fetched genre
                    updatedGenreList.add(0, firstgenre)
                    selectedGenre = "Select a genre" // Reset to placeholder on new selection
                }
                selectedGenre = firstgenre
                println("the dropdown $selectedGenre")
            }
        }
    }


    //"Movie Title", "Director", "Date Released", "Genre", "Publication Company"
//    normalText(field = "Movie Title", type = "Movie", initialValue = movieTitle, onValueChange = { movieTitle = it })
    normalNumber(field = "Year Released", type = "Movie", initialValue = yearReleased, onValueChange = { yearReleased = it })
    Dropdown(type = "Movie", field = "Genre", list = updatedGenreList, selectedGenre) { selectedGenre = it }
    Dropdown(type = "Movie", field = "Streaming Service", list = serviceList, selectedService) { selectedService = it }
//    dateField(field = "Date watched", type = "Movie")
    datePicker(type = "Movie", field = "Date watched")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun normalText(field: String, type: String, initialValue: String = "", onValueChange: (String) -> Unit) {
    var entered by remember { mutableStateOf(initialValue)  }
    LaunchedEffect(initialValue) {
        entered = initialValue
    }
    OutlinedTextField(
        value = entered,
        onValueChange = {
                        entered = it
//                        onValueChange(it)
                        },
        singleLine = true,
        label = {
            Text(
                text = field, color = colorResource(id = R.color.coolGrey),
                fontFamily = FontFamily(Font(R.font.alegreya_sans_regular))
            )
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = colorResource(id = R.color.blue),
            unfocusedBorderColor = colorResource(id = R.color.teal)
        ),
        shape = RoundedCornerShape(10.dp)
    )
    filled[type]?.set(field, entered).toString()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun reviewText(type: String, initialValue: String = "") {
    var entered by remember { mutableStateOf(initialValue)  }
    LaunchedEffect(initialValue) {
        entered = initialValue
    }
    OutlinedTextField(
        value = entered,
        onValueChange = {entered = it },
        minLines = 7,
        label = {
            Text(
                text = "Review",
                color = colorResource(id = R.color.coolGrey),
                fontFamily = FontFamily(Font(R.font.alegreya_sans_regular))
            )
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = colorResource(id = R.color.blue),
            unfocusedBorderColor = colorResource(id = R.color.teal)
        ),
        shape = RoundedCornerShape(10.dp)
    )
    filled[type]?.set("Review", entered).toString()
}

@Suppress("ModifierParameter")
@Composable
fun Dropdown(type: String, field: String, list: List<String>,
             selected: String = "", onSelectedChange: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    // Use the placeholder if 'selected' is empty, otherwise, show the selected value
    var entered by remember { mutableStateOf(if (selected.isEmpty()) field else selected) }

    LaunchedEffect(selected) {
        entered = if(selected.isNotEmpty()) selected else field
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 5.dp)
            .wrapContentSize(Alignment.TopCenter)
    ) {
        TextButton(
            onClick = { expanded = true },
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    color = colorResource(id = R.color.teal),
                    shape = RoundedCornerShape(10.dp)
                )
                .background(colorResource(id = R.color.off_white)),
        ) {
            var textColor = colorResource(id = R.color.black);
            // Adjust the condition to change text color if necessary
            textColor = if (entered == field) colorResource(id = R.color.coolGrey) else colorResource(id = R.color.black)

            Text(
                text = entered,
                fontFamily = FontFamily(Font(R.font.alegreya_sans_regular)),
                fontSize = 18.sp,
                color = textColor,
                textAlign = TextAlign.Left,
                modifier = Modifier.weight(1F)
            )

            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.right_arrow),
                contentDescription = "dropdown",
                tint = colorResource(id = R.color.black),
                modifier = Modifier
                    .rotate(90F)
                    .height(20.dp)
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .background(colorResource(id = R.color.off_white))
                .fillMaxWidth()
        ) {
            list.forEach { el ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = el,
                            fontFamily = FontFamily(Font(R.font.alegreya_sans_regular)),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxSize()
                        )},
                    onClick = {
                        if (el != field) { // Ignore clicks on placeholder
                        entered = el
                        onSelectedChange(el)
                    }
                        expanded = false
                    },
                    modifier = Modifier
                        .background(colorResource(id = R.color.off_white))
                        .fillMaxWidth(),
                )
            }
        }
    }

    if (entered != field) {
        filled[type]?.set(field, entered).toString()
    }
}

//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun dateField(field: String, type: String) {
//    var entered by remember{ mutableStateOf("Select a date") }
//
//    val datePickerState = rememberDatePickerState(initialSelectedDateMillis = 1578096000000)
//    DatePicker(
//        state = datePickerState,
//        modifier = Modifier
//            .padding(5.dp)
//            .fillMaxWidth()
//            .border(1.dp, colorResource(id = R.color.teal)),
//        showModeToggle = true,
//        title = {
//            Text(
//                text = "Select a date",
//                fontFamily = FontFamily(Font(R.font.alegreya_sans_regular)),
//                textAlign = TextAlign.Center,
//                fontSize = 18.sp,
//                modifier = Modifier.padding(horizontal = 5.dp)
//            )
//        },
//    )
//    val formatter = SimpleDateFormat("dd/MM/yyyy")
//    entered = formatter.format(datePickerState.selectedDateMillis?.let { Date(it) })
//
//    filled[type]?.set(field, entered).toString()
//}

@Composable
fun normalNumber(field: String, type: String, initialValue: String = "", onValueChange: (String) -> Unit) {
    var entered by remember { mutableStateOf(initialValue) }
    // Initially validate the initialValue. If no initialValue is provided, assume no error.
    var isError by remember(initialValue) { mutableStateOf(initialValue.isNotEmpty() && !isValidYear(initialValue)) }

    LaunchedEffect(initialValue) {
        entered = initialValue
        isError = !isValidYear(initialValue) && initialValue.isNotEmpty()
    }
    OutlinedTextField(
        value = entered,
        onValueChange = { newValue ->
            entered = newValue
            isError = !isValidYear(newValue) && newValue.isNotEmpty()
            onValueChange(newValue)
        },
        singleLine = true,
        label = {
            Text(
                text = field,
                // Use MaterialTheme.colors to access current theme colors.
                color = colorResource(id = R.color.coolGrey),
                fontFamily = FontFamily(Font(R.font.alegreya_sans_regular))
            )
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp),
        // Update TextField colors based on isError state.
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = if (isError) colorResource(id = R.color.red) else colorResource(id = R.color.blue),
            unfocusedBorderColor = if (isError) colorResource(id = R.color.red) else colorResource(id = R.color.teal),
            errorBorderColor = colorResource(id = R.color.red),
        ),
        shape = RoundedCornerShape(10.dp),
        isError = isError, // Indicate error state.
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
    )

    // Optionally display an error message
    if (isError) {
        Text(
            "Enter a valid year (e.g., 2023)",
            color = colorResource(id = R.color.red),
            fontFamily = FontFamily(Font(R.font.alegreya_sans_regular)),
            modifier = Modifier.padding(start = 16.dp, top = 2.dp)
        )
    }

    filled[type]?.set(field, entered).toString()
}

fun isValidYear(year: String): Boolean {
    return year.length == 4 && year.toIntOrNull()?.let { it in 1000..2099 } == true
}

@Composable
fun datePicker(type: String, field: String) {
    var entered = "Select a date"
    Box(
        modifier = Modifier.padding(horizontal = 10.dp)
    ) {
        val snackState = remember { SnackbarHostState() }
        SnackbarHost(hostState = snackState, Modifier)
        val openDialog = remember { mutableStateOf(false) }
        val datePickerState = rememberDatePickerState()

        if (openDialog.value) {
            val confirmEnabled = remember {
                derivedStateOf { datePickerState.selectedDateMillis != null }
            }
            // this button doesn't do anything but is here so there is no gap in background when
            // the dialog pops up
            TextButton(
                onClick = { /* TODO() */},
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        width = 1.dp,
                        color = colorResource(id = R.color.teal),
                        shape = RoundedCornerShape(10.dp)
                    )
                    .background(colorResource(id = R.color.off_white))
            ){""}

            DatePickerDialog(
                onDismissRequest = { openDialog.value = false },
                confirmButton = {
                    TextButton(
                        onClick = { openDialog.value = false },
                        enabled = confirmEnabled.value
                    ) {
                        Text("OK")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { openDialog.value = false }) {
                        Text("Cancel")
                    }
                },
                colors = DatePickerDefaults.colors(
                    containerColor = colorResource(id = R.color.off_white),
                    yearContentColor = colorResource(id = R.color.off_white),
                    navigationContentColor = colorResource(id = R.color.off_white),
                    subheadContentColor = colorResource(id = R.color.off_white),
                    weekdayContentColor = colorResource(id = R.color.off_white),
                    headlineContentColor = colorResource(id = R.color.off_white),
                    selectedDayContainerColor = colorResource(id = R.color.off_white),
                    selectedDayContentColor = colorResource(id = R.color.teal),
                ),
            ) {
                DatePicker(state = datePickerState)
            }
        } else {
            TextButton(
                onClick = { openDialog.value = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        width = 1.dp,
                        color = colorResource(id = R.color.teal),
                        shape = RoundedCornerShape(10.dp)
                    )
                    .background(colorResource(id = R.color.off_white))
            ) {
                val formatter = SimpleDateFormat("MM/dd/yyyy")
                datePickerState.selectedDateMillis?.let {
                    val temp = (Date(datePickerState.selectedDateMillis!!).time + abs(Date(datePickerState.selectedDateMillis!!).timezoneOffset * 60000)).toLong()
                    entered = formatter.format(Date(temp)).toString()
                }
                Text(
                    text = entered,
                    fontFamily = FontFamily(Font(R.font.alegreya_sans_regular)),
                    color = colorResource(id = R.color.black),
                    fontSize = 18.sp,
                    textAlign = TextAlign.Left,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 15.dp, horizontal = 10.dp),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.calendar),
                contentDescription = "calendar icon",
                tint = colorResource(id = R.color.black),
                modifier = Modifier.height(20.dp)
            )
        }
    }

    filled[type]?.set(field, entered).toString()
}