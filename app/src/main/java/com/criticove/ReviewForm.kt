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

import com.criticove.m3.ButtonStyles.PrimaryButton
import android.content.Context
import android.content.Intent

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
                    ReviewFormMainContent(navController)
                }
                composable("ReviewPage") {
                    ReviewPageMainContent(navController)
                }
            }
        }
    }
}

@Composable
fun ReviewFormMainContent(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(colorResource(id = R.color.off_white))
    ) {
        ReviewHeader()
        Selection()
        println("this is filled $filled")
    }
}

@Composable
fun ReviewHeader() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .background(colorResource(id = R.color.blue)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "New Review",
            color = colorResource(id = R.color.white),
            fontSize = 20.sp

        )
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
fun CreateForm(type:String, navController: NavController) {
    var elements =  mutableListOf<String>()
    var text by remember { mutableStateOf("") }

    when (type) {
        "Book" -> elements = listOf("Book Title","Author", "Date Published", "Genre", "Book Type").toMutableList()
        "TV Show" -> elements = listOf("TV Show Title", "Director", "Date Released", "Genre", "Streaming Service").toMutableList()
        "Movie" -> elements = listOf("Movie Title", "Director", "Date Released", "Genre", "Publication Company").toMutableList()
    }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(colorResource(id = R.color.off_white)),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        )
        {
            elements.forEach { label ->
                var bookData by remember {mutableStateOf("")}
                var movieData by remember {mutableStateOf("")}
                var tvData by remember {mutableStateOf("")}

                when(type) {
                    "Book" -> { OutlinedTextField(
                        value = bookData,
                        onValueChange = { bookData = it },
                        singleLine = true,
                        label = { Text( text = label, color = colorResource(id = R.color.coolGrey),) },
                        modifier = Modifier
                            .fillMaxWidth(),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = colorResource(id = R.color.blue),
                            unfocusedBorderColor = colorResource(id = R.color.teal)
                        ),
                        shape = RoundedCornerShape(10.dp)
                    )
                        filled["Book"]?.set(label, bookData).toString()}

                    "TV Show" -> { OutlinedTextField(
                        value = tvData,
                        onValueChange = { tvData = it },
                        singleLine = true,
                        label = { Text( text = label, color = colorResource(id = R.color.coolGrey),) },
                        modifier = Modifier
                            .fillMaxWidth(),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = colorResource(id = R.color.blue),
                            unfocusedBorderColor = colorResource(id = R.color.teal)
                        ),
                        shape = RoundedCornerShape(10.dp)
                    )
                        filled["TV Show"]?.set(label, tvData).toString()}

                    "Movie" -> {OutlinedTextField(
                        value = movieData,
                        onValueChange = { movieData = it },
                        singleLine = true,
                        label = { Text( text = label, color = colorResource(id = R.color.coolGrey),) },
                        modifier = Modifier
                            .fillMaxWidth(),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = colorResource(id = R.color.blue),
                            unfocusedBorderColor = colorResource(id = R.color.teal)
                        ),
                        shape = RoundedCornerShape(10.dp)
                    )
                        filled["Movie"]?.set(label, movieData).toString()}
                }
            }
            OutlinedTextField(
                value = text,
                onValueChange = { text = it },
                minLines = 7,
                label = { Text( text = "Review", color = colorResource(id = R.color.coolGrey),) },
                modifier = Modifier
                    .fillMaxWidth(),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = colorResource(id = R.color.blue),
                    unfocusedBorderColor = colorResource(id = R.color.teal)
                ),
                shape = RoundedCornerShape(10.dp)
            )
        }
    println("this is filled $filled")
    StarRating(type)
    Submission(type, navController)
}

@Composable
fun Submission(type: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        Button(
            onClick = {
                when (type) {
                    "Book" -> submittedReview = filled["Book"]
                    "TV Show" -> submittedReview = filled["TV Show"]
                    "Movie" -> submittedReview = filled["Movie"]
                }
                submittedReview?.let { SubmittedReview(type, reviewScore, it) }
                      },
            colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(id = R.color.teal),
                contentColor = colorResource(id = R.color.off_white)),
            modifier = Modifier.weight(1F)
        ) { Text("Share") }

        Button(
            onClick = {},
            colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(id = R.color.teal),
                contentColor = colorResource(id = R.color.off_white)
            ),
            modifier = Modifier.weight(1F)
        ) { Text("Cancel") }
    }
}

@Composable
fun StarRating(type: String) {
    var bookScore by remember { mutableIntStateOf(1) }
    var tvScore by remember { mutableIntStateOf(1) }
    var movieScore by remember { mutableIntStateOf(1) }
    var id = R.drawable.star_empty
        Column ( modifier = Modifier.padding(10.dp).fillMaxWidth()) {
            Text(text = "Rating", modifier= Modifier.fillMaxWidth())
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
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(colorResource(id = R.color.off_white)),
    ) {
        ReviewHeader()
        Column(
            modifier = Modifier
                .padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Selection()
        }
    }
}


