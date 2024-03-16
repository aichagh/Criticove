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
import android.service.autofill.DateTransformation
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MenuItemColors
import androidx.compose.material3.TextButton
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.window.PopupProperties
import com.criticove.m3.ButtonStyles.IconButton

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
                composable("Reviews") {
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
    var elements = mutableListOf<String>()
    var text by remember { mutableStateOf("") }

    when (type) {
        "Book" -> elements =
            listOf("Book Title", "Author", "Date Published", "Genre", "Book Type").toMutableList()

        "TV Show" -> elements = listOf(
            "TV Show Title",
            "Director",
            "Date Released",
            "Genre",
            "Streaming Service"
        ).toMutableList()

        "Movie" -> elements = listOf(
            "Movie Title",
            "Director",
            "Date Released",
            "Genre",
            "Publication Company"
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
            "Book" -> { BookForm() }
            "TV Show" -> { TVShowForm() }
            "Movie" -> { MovieForm() }
        }
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
    ReviewFormMainContent(navController = rememberNavController())
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookForm() {
    // Fields: "Book Title","Author", "Date Published", "Genre", "Book Type"
    val genreList = listOf<String>("Romance", "Thriller", "Drama", "Autobiography", "Sci-fi")
    val typeList = listOf<String>("Physical", "E-Book")

    normalText(field = "Book Title", type = "Book")
    normalText(field = "Author", type = "Book")
    normalText(field = "Date Published", type = "Show")
    Dropdown(type = "Book", field = "Genre", list = genreList)
    Dropdown(type = "Book", field = "Book Type", list = typeList)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TVShowForm() {
//    "TV Show Title", "Director", "Date Released", "Genre", "Streaming Service"
    val genreList = listOf<String>("Romance", "Thriller", "Drama", "Autobiography", "Sci-fi")
    val serviceList = listOf<String>("Netflix", "Prime", "Hulu", "HBO", "Other")

    normalText(field = "TV Show Title", type = "TV Show")
    normalText(field = "Director", type = "TV Show")
    normalText(field = "Date Released", type = "TV Show")
    Dropdown(type = "TV Show", field = "Genre", list = genreList)
    Dropdown(type = "TV Show", field = "Streaming Service", list = serviceList)

}

@Composable
fun MovieForm() {
    //"Movie Title", "Director", "Date Released", "Genre", "Publication Company"
    val genreList = listOf<String>("Romance", "Thriller", "Drama", "Autobiography", "Sci-fi")
    val serviceList = listOf<String>("Netflix", "Prime", "Hulu", "HBO", "Other")

    normalText(field = "Movie Title", type = "Movie")
    normalText(field = "Director", type = "Movie")
    normalText(field = "Date Released", type = "Movie")
    Dropdown(type = "Movie", field = "Genre", list = genreList)
    Dropdown(type = "Movie", field = "Streaming Service", list = serviceList)

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun normalText(field: String, type: String) {
    var entered by remember { mutableStateOf("") }

    OutlinedTextField(
        value = entered,
        onValueChange = { entered = it },
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

@Suppress("ModifierParameter")
@Composable
fun Dropdown(type: String, field: String, list: List<String>) {
    var expanded by remember { mutableStateOf(false) }
    var entered by remember { mutableStateOf(field) }

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
            if(entered == field) {
                textColor = colorResource(id = R.color.coolGrey)
            }

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
                contentDescription = "drop", tint = colorResource(id = R.color.black),
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
                    onClick = { entered = el; expanded = false},
                    modifier = Modifier
                        .background(colorResource(id = R.color.off_white))
                        .fillMaxWidth(),
                )
            }
        }
    }

    filled[type]?.set(field, entered).toString()
}