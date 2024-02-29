package com.criticove

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
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

// get this data from database, or passed from review select page, currently sample data
var reviewType = "Book"
var reviewData = mutableMapOf("Title" to "The Night Circus", "Author" to "Erin",
    "Date Published" to "01/01/2024", "Genre" to "Fantasy", "Book Type" to "eBook",
    "Started" to "01/01/2024", "Finished" to "20/01/2024", "Rating" to "4",
    "Review" to "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.")

var updatedReview: MutableMap<String, String>? = null

@Composable
fun ReviewDetailsHeader() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .background(colorResource(id = R.color.blue)),
        contentAlignment = Alignment.Center
    ) {
        reviewData["Title"]?.let {
            Text(
                text = it,
                color = colorResource(id = R.color.white),
                fontSize = 20.sp

            )
        }
    }
}

@Composable
fun ReviewDetailsTable(type: String) {
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
            elements.forEach { label ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Column() {
                        var curData by remember {mutableStateOf(reviewData[label].toString())}

                        OutlinedTextField(
                            value = curData,
                            onValueChange = { curData = it },
                            label = {Text( text = label, color = colorResource(id = R.color.blue)) },
                            modifier = Modifier.fillMaxWidth()
                        )
                        reviewData.set(label, curData).toString()

                    }
                }
            }
        }
    }
    SubmitUpdatedReview()
}

@Composable
fun SubmitUpdatedReview() {
    var saveToDB by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(colorResource(id = R.color.off_white))
            .padding(10.dp)

    ) {
        Row(
            horizontalArrangement = Arrangement.End
        ) {
            Button(
                onClick = {
                    updatedReview = reviewData
                    saveToDB = true
                }
            ) {
                Text("Submit")
            }

            if (saveToDB) {
                // update review details on database

                saveToDB = false
            }
        }
    }
}

@Preview
@Composable
fun PreviewReviewDetails() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(colorResource(id = R.color.off_white))
    ) {
        Column() {
            ReviewDetailsHeader()
            ReviewDetailsTable(reviewType)
        }
    }
}