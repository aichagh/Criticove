package com.criticove


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonColors
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.criticove.backend.SubmittedReview
import androidx.compose.runtime.*
import com.criticove.m3.ButtonStyles.PrimaryButton
import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import com.criticove.backend.userModel
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.LocalContext
import com.criticove.api.Movie
import com.criticove.api.MovieDetails
import com.criticove.api.TvShow
import com.criticove.api.TvShowDetails

val filled = mutableMapOf(
    "Book" to mutableMapOf("Book Title" to "", "Author" to "", "Date Published" to "", "Genre" to "", "Book Type" to ""),
    "TV Show" to mutableMapOf("TV Show Title" to "", "Director" to "", "Date Released" to "", "Genre" to "", "Streaming Service" to ""),
    "Movie" to mutableMapOf("Movie Title" to "", "Director" to "" , "Date Released" to "", "Genre" to "", "Publication Company" to ""))

var reviewScore = 1
var submittedReview: MutableMap<String, String>? = null



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
            Selection(navController)
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

interface SuggestionItem {
    val id: Int
    val displayText: String // Combine 'title' or 'name' into one common field for simplicity
    val displayDate: String // Combine 'release_date' or 'first_air_date' into one common field
}

val Movie.suggestionItem: SuggestionItem
    get() = object : SuggestionItem {
        override val id: Int = this@suggestionItem.id
        override val displayText: String = this@suggestionItem.title
        override val displayDate: String = this@suggestionItem.release_date
    }

// Extension properties for TvShow
val TvShow.suggestionItem: SuggestionItem
    get() = object : SuggestionItem {
        override val id: Int = this@suggestionItem.id
        override val displayText: String = this@suggestionItem.name
        override val displayDate: String = this@suggestionItem.first_air_date
    }

@Composable
fun AutocompleteTextField(
    label: String,
    viewModel: MediaViewModel,
    onSuggestionSelected: (Int) -> Unit
) {
    var query by remember { mutableStateOf("") }
    var isExpanded by remember { mutableStateOf(false) }

    val movieSuggestions by viewModel.movieSuggestions.observeAsState()
    val tvShowSuggestions by viewModel.tvShowSuggestions.observeAsState()

    val suggestions: List<SuggestionItem> = if (label == "Movie Title")
        movieSuggestions?.map { it.suggestionItem } ?: emptyList()
    else
        tvShowSuggestions?.map { it.suggestionItem } ?: emptyList()

    OutlinedTextField(
        value = query,
        onValueChange = {
            query = it
            isExpanded = it.isNotEmpty()
            if (label == "Movie Title") {
                viewModel.searchMovieTitles(it)
            } else {
                viewModel.searchTvShowTitles(it)
            }
        },
        label = { Text(text = label) },
        modifier = Modifier.fillMaxWidth()
    )

    if (suggestions.isNotEmpty()) {
        DropdownMenu(
            expanded = isExpanded,
            onDismissRequest = { isExpanded = false }
        ) {
            suggestions.take(5).forEach { suggestion ->
                DropdownMenuItem(
                    onClick = {
                        query = suggestion.displayText
                        isExpanded = false
                        onSuggestionSelected(suggestion.id)
                    },
                    text = {
                        Text("${suggestion.displayText} (${suggestion.displayDate})")
                    }
                )
            }
        }
    }
}

@Composable
fun Selection(navController: NavController) {
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
    CreateForm(selectedType, navController)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateForm(type: String, navController: NavController, mediaViewModel: MediaViewModel) {
    val context = LocalContext.current

    // Use mutable states for dynamic form fields that might be populated from the API or edited by the user
    var title by remember { mutableStateOf("") }
    var directorOrAuthor by remember { mutableStateOf("") }
    var dateReleasedOrPublished by remember { mutableStateOf("") }
    var genre by remember { mutableStateOf("") }
    var streamingServiceOrPublisher by remember { mutableStateOf("") }
    var review by remember { mutableStateOf("") }

    // Observe movie and TV show details
    mediaViewModel.movieDetails.observeAsState().value?.let { movie ->
        if (type == "Movie") {
            title = movie.title
            dateReleasedOrPublished = movie.release_date
            genre = movie.genres.joinToString { it.name }
        }
    }

    mediaViewModel.tvShowDetails.observeAsState().value?.let { tvShow ->
        if (type == "TV Show") {
            title = tvShow.name
            dateReleasedOrPublished = tvShow.first_air_date
            genre = tvShow.genres.joinToString { it.name }
            directorOrAuthor = tvShow.created_by
            streamingServiceOrPublisher = tvShow.networks.joinToString { it.name }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        when (type) {
            "Book" -> {
                // Book form fields
                listOf(
                    "Book Title" to { title = it },
                    "Author" to { directorOrAuthor = it },
                    "Date Published" to { dateReleasedOrPublished = it },
                    "Genre" to { genre = it },
                    "Publisher" to { streamingServiceOrPublisher = it }
                ).forEach { (label, onValueChange) ->
                    OutlinedTextField(
                        value = when (label) {
                            "Book Title" -> title
                            "Author" -> directorOrAuthor
                            "Date Published" -> dateReleasedOrPublished
                            "Genre" -> genre
                            "Publisher" -> streamingServiceOrPublisher
                            else -> ""
                        },
                        onValueChange = onValueChange,
                        label = { Text(text = label) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                }
            }
            "TV Show", "Movie" -> {
                // Title field with autocomplete for TV Show or Movie
                AutocompleteTextField(
                    label = if (type == "Movie") "Movie Title" else "TV Show Title",
                    viewModel = mediaViewModel,
                    onSuggestionSelected = { id ->
                        if (type == "Movie") {
                            mediaViewModel.fetchMovieDetails(id)
                        } else {
                            mediaViewModel.fetchTvShowDetails(id)
                        }
                    }
                )

                // Additional fields for TV Show or Movie
                if (type == "TV Show") {
                    OutlinedTextField(
                        value = directorOrAuthor,
                        onValueChange = { directorOrAuthor = it },
                        label = { Text(text = "Director") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = streamingServiceOrPublisher,
                        onValueChange = { streamingServiceOrPublisher = it },
                        label = { Text(text = "Streaming Service") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                }

                // Common fields for both Movie and TV Show
                listOf(
                    "Date Released" to { dateReleasedOrPublished = it },
                    "Genre" to { genre = it }
                ).forEach { (label, onValueChange) ->
                    OutlinedTextField(
                        value = when (label) {
                            "Date Released" -> dateReleasedOrPublished
                            "Genre" -> genre
                            else -> ""
                        },
                        onValueChange = onValueChange,
                        label = { Text(text = label) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                }
            }
        }

        // Review field common for all types
        OutlinedTextField(
            value = review,
            onValueChange = { review = it },
            label = { Text(text = "Review") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 3,
            maxLines = 5
        )

        // Placeholder for StarRating and Submission components
         StarRating(type)
         Submission(type, navController)
    }
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
                    selected = shareOption == true,
                    onClick = { shareOption = true},
                    colors = RadioButtonDefaults.colors(
                        selectedColor = colorResource(id = R.color.blue),
                        unselectedColor = colorResource(id = R.color.blue),
                    )
                )

                Text(
                    text = "Share",
                    fontFamily = FontFamily(Font(R.font.alegreya_sans_regular)),
                    fontSize = 18.sp
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = shareOption == false,
                    onClick = { shareOption = false},
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
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Button(
            onClick = {
                when (type) {
                    "Book" -> submittedReview = filled["Book"]
                    "TV Show" -> submittedReview = filled["TV Show"]
                    "Movie" -> submittedReview = filled["Movie"]
                }
                submittedReview?.let { SubmittedReview(type, reviewScore, it)
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

    //ReviewFormMainContent(navController = rememberNavController())
}


