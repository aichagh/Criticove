package com.criticove

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// get this data from database, currently sample data
// try to get only needed data
var reviewListType = "Book"
var reviewList = mutableListOf(
    mutableMapOf("Title" to "The Night Circus", "Author" to "Erin Morgenstern",
        "Finished" to "20/01/2024", "Rating" to "4"),
    mutableMapOf("Title" to "Sample Movie", "Author" to "John Doe",
        "Finished" to "20/01/2024", "Rating" to "3"),
    mutableMapOf("Title" to "Sample Book", "Author" to "Sample Name",
        "Finished" to "23/01/2024", "Rating" to "5"))


class ReviewList : ComponentActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .background(colorResource(id = R.color.off_white))
            ) {
                Column {
                    ReviewListHeader()
                    DisplayReviewList(reviewListType)
                }
            }
        }
    }
}

@Composable
fun ReviewListHeader() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .background(colorResource(id = R.color.blue)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = reviewListType + "Reviews",
            color = colorResource(id = R.color.white),
            fontSize = 20.sp
        )
    }
}

@Composable
fun DisplayReviewList(type: String) {
    var elements =  mutableListOf<String>()
    when (type) {
        "Book" -> elements = listOf("Title", "Author", "Date Published", "Genre", "Book Type", "Started",
            "Finished", "Rating", "Review").toMutableList()
        "TV Show" -> elements = listOf("Title", "Director", "Date Released", "Genre", "Streaming Service", "Started",
            "Finished", "Rating", "Review").toMutableList()
        "Movie" -> elements = listOf("Title", "Director", "Date Released", "Genre", "Publication Company", "Started",
            "Finished", "Rating", "Review" ).toMutableList()
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(colorResource(id = R.color.off_white))
            .padding(10.dp)
    ) {
        Column() {
            reviewList.forEach { label ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Column() {

                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewReviewList() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(colorResource(id = R.color.off_white))
    ) {
        Column {
            ReviewListHeader()
            DisplayReviewList(reviewListType)
        }
    }
}