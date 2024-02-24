package com.criticove


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview

val filled = mutableMapOf(
    "Book" to mutableMapOf("Book Title" to "", "Author" to "", "Date Published" to "", "Genre" to "", "Book Type" to ""),
    "TV Show" to mutableMapOf("TV Show Title" to "", "Director" to "", "Date Released" to "", "Genre" to "", "Streaming Service" to ""),
    "Movie" to mutableMapOf("Movie Title" to "", "Director" to "" , "Date Released" to "", "Genre" to "", "Publication Company" to ""))

var submittedReview: MutableMap<String, String>? = null


class ReviewForm : ComponentActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .background(colorResource(id = R.color.off_white))
            ) {
                Column() {
                    ReviewHeader()
                    Selection()
                    println("this is filled $filled")
                }
            }
        }
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
fun Selection() {
    var selectedType by remember { mutableStateOf("Book") }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(30.dp)
                .padding(horizontal = 10.dp)
                .background(colorResource(id = R.color.off_white)),
        ) {
            val mediaType = listOf("Book", "TV Show", "Movie")
            mediaType.forEach{ el ->
                RadioButton(
                    selected = selectedType == el,
                    onClick = { selectedType = el }
                )
                Text(
                    text = el,
                    modifier = Modifier.padding(start = 4.dp)
                )
            }
        }
    CreateForm(selectedType)
}
@Composable
fun CreateForm(type:String) {
    var elements =  mutableListOf<String>()
    when (type) {
        "Book" -> elements = listOf("Book Title","Author", "Date Published", "Genre", "Book Type" ).toMutableList()
        "TV Show" -> elements = listOf("TV Show Title", "Director", "Date Released", "Genre", "Streaming Service" ).toMutableList()
        "Movie" -> elements = listOf("Movie Title", "Director", "Date Released", "Genre", "Publication Company" ).toMutableList()
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
                            var bookData by remember {mutableStateOf("")}
                            var movieData by remember {mutableStateOf("")}
                            var tvData by remember {mutableStateOf("")}
                            //Text(
                                //text = label
                           // )
                           // println("this is the value: $filled[type]?.get(label)")
                            when(type) {
                                "Book" -> {OutlinedTextField(value=bookData,onValueChange={bookData=it},
                                    label = {Text( text = label, color = colorResource(id = R.color.blue)) },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                )
                                    filled["Book"]?.set(label, bookData).toString()}
                                "TV Show" -> {OutlinedTextField(value=tvData,onValueChange={tvData=it},
                                    label = {Text( text = label, color = colorResource(id = R.color.blue)) },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                )
                                    filled["TV Show"]?.set(label, tvData).toString()}
                                "Movie" -> {OutlinedTextField(value=movieData,onValueChange={movieData=it},
                                    label = {Text( text = label, color = colorResource(id = R.color.blue)) },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                )
                                    filled["Movie"]?.set(label, movieData).toString()}
                            }
                        }
                    }
            }
        }
    }
    println("this is filled $filled")
    Submission(type)
}

@Composable
fun Submission(type: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(colorResource(id = R.color.off_white))
    ) {
        Column() {
            Button(onClick = {
                when (type) {
                    "Book" -> submittedReview = filled["Book"]
                    "TV Show" -> submittedReview = filled["TV Show"]
                    "Movie" -> submittedReview = filled["Movie"]
                }
            }) {

            }
        }
    }
}


@Preview
@Composable
fun PreviewCreateReview() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(colorResource(id = R.color.off_white))
    ) {
        Column() {
            ReviewHeader()
            Selection()
        }
    }
}

