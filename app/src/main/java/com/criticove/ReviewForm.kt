package com.criticove


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import com.criticove.m3.ButtonStyles.PrimaryButton

val filled = mutableMapOf(
    "Book" to mutableMapOf("Book Title" to "", "Author" to "", "Date Published" to "", "Genre" to "", "Book Type" to ""),
    "TV Show" to mutableMapOf("TV Show Title" to "", "Director" to "", "Date Released" to "", "Genre" to "", "Streaming Service" to ""),
    "Movie" to mutableMapOf("Movie Title" to "", "Director" to "" , "Date Released" to "", "Genre" to "", "Publication Company" to ""))

var reviewScore = 1
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
                Column {
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
            var id = R.drawable.movie_black
            when(el) {
                "Book" -> id = R.drawable.book_black
                "TV Show" -> id = R.drawable.tv_black
            }
            Icon(imageVector = ImageVector.vectorResource(id = id),
                contentDescription = el )
        }
    }
    CreateForm(selectedType)
}
@Composable
fun CreateForm(type:String) {
    var elements =  mutableListOf<String>()
    when (type) {
        "Book" -> elements = listOf("Book Title","Author", "Date Published", "Genre", "Book Type", "Review").toMutableList()
        "TV Show" -> elements = listOf("TV Show Title", "Director", "Date Released", "Genre", "Streaming Service", "Review" ).toMutableList()
        "Movie" -> elements = listOf("Movie Title", "Director", "Date Released", "Genre", "Publication Company", "Review" ).toMutableList()
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
                            "Book" -> {OutlinedTextField(value=bookData, onValueChange={bookData=it},
                                label = {Text( text = label, color = colorResource(id = R.color.blue),) },
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
    StarRating(type)
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
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            )
            {
                PrimaryButton(
                    onClick = {
                        when (type) {
                            "Book" -> submittedReview = filled["Book"]
                            "TV Show" -> submittedReview = filled["TV Show"]
                            "Movie" -> submittedReview = filled["Movie"]
                        }
                    }, "Share")
            }
        }
    }
}

@Composable
fun StarRating(type: String) {
    var bookScore by remember { mutableIntStateOf(1) }
    var tvScore by remember { mutableIntStateOf(1) }
    var movieScore by remember { mutableIntStateOf(1) }
    var id = R.drawable.star_empty
    Box( modifier = Modifier.padding(10.dp).fillMaxWidth()) {
        Column () {
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
        Column {
            ReviewHeader()
            Selection()
        }
    }
}


