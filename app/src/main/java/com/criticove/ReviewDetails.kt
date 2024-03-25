package com.criticove

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.criticove.backend.BookReview
import com.criticove.backend.MovieReview
import com.criticove.backend.Review
import com.criticove.backend.SubmittedReview
import com.criticove.backend.TVShowReview
import com.criticove.backend.delSelectedReview
import com.criticove.backend.getSelectedReview
import com.criticove.backend.updateSelReview
import com.criticove.backend.userModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// get this data from database, or passed from review select page, currently sample data
var reviewID = "Insert ID"   // replace with other id
// var reviewData = getSelectedReview(reviewID)
var reviewType = ""   // reviewData["type"]!!

// var reviewData = mutableMapOf("Title" to "The Night Circus", "Author" to "Erin Morgenstern",
//    "Date Published" to "01/01/2024", "Genre" to "Fantasy", "Book Type" to "eBook",
//    "Started" to "01/01/2024", "Finished" to "20/01/2024", "Rating" to "4",
//    "Review" to "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.")

var updatedReview: MutableMap<String, String>? = null

/*
class ReviewDetails: ComponentActivity() {
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val userModel: userModel by viewModels()
        lifecycleScope.launch {
            //userModel.getReviews()
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                userModel.selReview.collect {
                    println("here sel review is ${userModel.selReview}")
                    setContent {
                        ReviewDetailsMainContent(rememberNavController(), reviewID)

                    }
                }
            }
        }
    }
}

 */

@Composable
fun ReviewDetailsMainContent(navController: NavController,
                             reviewID: String,
                             isFriend: Boolean,
                             friendID: String,
                             userModel: userModel) {
    userModel.getSelReview(reviewID, friendID)
    val selReview by userModel.selReview.collectAsState()
    println(reviewID)

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .verticalScroll(rememberScrollState())
                .background(colorResource(id = R.color.off_white))
        ) {
            CommonHeader(navController, selReview.title, "Reviews")
            ReviewDetailsTable(reviewType, selReview, reviewID, isFriend, navController)
        }
//    }
}

@Composable
fun ReviewDetailsTable(type: String, selReview: Review,
                       reviewID: String, isFriend: Boolean,
                       navController: NavController) {
    //val selReview by selReview.collectAsState()

    var elements =  mutableListOf<String>()
    var reviewData: MutableMap<String, String> = mutableMapOf()

    elements = listOf(
        "Title", "Author", "Date Published", "Genre", "Book Type", "Started",
        "Finished", "Rating", "Review"
    ).toMutableList()

    when (selReview) {
        is BookReview -> {
            reviewData.clear()

            elements = listOf(
                "Title", "Author", "Date Published", "Genre", "Book Type",
                "Date finished", "Rating", "Review"
            ).toMutableList()

            val bookReview: BookReview = selReview as BookReview

            reviewData["Title"] = bookReview.title
            reviewData["Author"] = bookReview.author
            reviewData["Date Published"] = bookReview.date
            reviewData["Genre"] = bookReview.genre
            reviewData["Book Type"] = bookReview.booktype
            reviewData["Date finished"] = bookReview.datefinished
            reviewData["Rating"] = bookReview.rating.toString()
            reviewData["Review"] = bookReview.paragraph

        }
        is TVShowReview -> {
            reviewData.clear()
            elements = listOf(
                "Title", "Director", "Date Released", "Genre", "Streaming Service",
                "Date finished", "Rating", "Review"
            ).toMutableList()

            val tvReview: TVShowReview = selReview as TVShowReview

            reviewData["Title"] = tvReview.title
            reviewData["Director"] = tvReview.director
            reviewData["Date Released"] = tvReview.date
            reviewData["Genre"] = tvReview.genre
            reviewData["Streaming Service"] = tvReview.streamingservice
            reviewData["Date finished"] = tvReview.datefinished
            reviewData["Rating"] = tvReview.rating.toString()
            reviewData["Review"] = tvReview.paragraph
        }
        is MovieReview -> {
            reviewData.clear()
            elements = listOf(
                "Title", "Director", "Date Released", "Genre", "Streaming Service",
                "Date watched", "Rating", "Review"
            ).toMutableList()

            val movieReview: MovieReview = selReview as MovieReview

            reviewData["Title"] = movieReview.title
            reviewData["Director"] = movieReview.director
            reviewData["Date Released"] = movieReview.date
            reviewData["Genre"] = movieReview.genre
            reviewData["Streaming Service"] = movieReview.streamingservice
            reviewData["Date watched"] = movieReview.datewatched
            reviewData["Rating"] = movieReview.rating.toString()
            reviewData["Review"] = movieReview.paragraph
        }
    }

    displayReviewDetails(type, reviewData, elements, reviewID, isFriend, navController)
}

@Composable
fun displayReviewDetails(type: String, reviewData: MutableMap<String, String>,
                         elements: MutableList<String>,
                         reviewID: String, isFriend: Boolean,
                         navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(colorResource(id = R.color.off_white))
            .padding(10.dp)
    ) {
        Column() {
            println("in the review details page but not yet added info")
            elements.forEach { label ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Column() {
                        //var curData = reviewData[label].toString()
                        var edit by remember { mutableStateOf(false) }
                        var curData by remember {mutableStateOf(reviewData[label].toString())}

                        if (label != "Review") {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(5.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "$label: ",
                                    fontSize = 18.sp,
                                    fontFamily = FontFamily(Font(R.font.alegreya_sans_regular)),
                                    textAlign = TextAlign.End,
                                    color = colorResource(id = R.color.teal),
                                    modifier = Modifier
                                        .padding(5.dp)
                                        .width(120.dp)
                                )
                                /*
                                Text(
                                    text = "$curData",
                                    fontFamily = FontFamily(Font(R.font.alegreya_sans_regular)),
                                    modifier = Modifier
                                        .padding(5.dp)
                                )
                                */




                                if (label == "Rating" && curData != "null") {
                                    println("at rating point $label : $curData")
                                    Stars(curData.toInt())
                                } else {
                                    Text(
                                        text = "$curData",
                                        fontSize = 18.sp,
                                        fontFamily = FontFamily(Font(R.font.alegreya_sans_regular)),
                                        modifier = Modifier
                                            .padding(5.dp)
                                    )
                                }




                            }
                        }

                        else {
                            OutlinedTextField(
                                value = curData,
                                onValueChange = { curData = it },
                                enabled = edit,
                                minLines = 3,
                                maxLines = 7,
                                label = {Text( text = "Review", fontSize = 18.sp, color = colorResource(id = R.color.blue)) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .horizontalScroll(rememberScrollState())
                                    .padding(5.dp)
                            )
                            if (!isFriend) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceEvenly
                                ) {
                                    if (!edit) {
                                        CustomButton("Edit") { edit = true }
                                        CustomButton("Delete") {
                                            navController.navigate("Friends")
                                            delSelectedReview(reviewID)
                                            //navController.navigate("Reviews")
                                        }
                                    } else {
                                        CustomButton("Save") {
                                            edit = false
                                            reviewData.set("Review", curData).toString()

                                            updateSelReview(reviewID, curData)
                                        }
                                        CustomButton("Cancel") { edit = false }
                                    }
                                }
                            }
//                            println("new reviewData is $reviewData")
                        }



                    }
                }
            }
            /*
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
                Column() {
                    var revData by remember {mutableStateOf(selReview.paragraph)}

                    OutlinedTextField(
                        value = revData,
                        onValueChange = { revData = it },
                        minLines = 3,
                        maxLines = 7,
                        label = {Text( text = "Review", color = colorResource(id = R.color.blue)) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState())
                    )
                    reviewData.set("Review", revData).toString()

                }
            }

             */
        }
    }
//    if (!isFriend) {
//        SubmitUpdatedReview(type, reviewData, reviewID)
//    }
}

@Composable
fun SubmitUpdatedReview(type: String, reviewData: MutableMap<String, String>,
                        reviewID: String) {
    var saveToDB by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(colorResource(id = R.color.off_white))
            .padding(10.dp)

    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            Button(
                onClick = {
                    TODO()
                    //updatedReview = reviewData
                    //delSelectedReview(reviewID)
                    //updatedReview?.let { SubmittedReview(type, reviewData["Rating"]!!.toInt(), it) }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(id = R.color.teal),
                    contentColor = colorResource(id = R.color.off_white)
                ),
                modifier = Modifier
                    .width(150.dp)
            ) {
                Text("Edit")
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
            .padding(10.dp)

    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            Button(
                onClick = {
                    //updatedReview = reviewData
                    //delSelectedReview(reviewID)
                    //updatedReview?.let { SubmittedReview(type, reviewData["Rating"]!!.toInt(), it) }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(id = R.color.teal),
                    contentColor = colorResource(id = R.color.off_white)
                ),
                modifier = Modifier
                    .width(200.dp)
            ) {
                Text("Submit")
            }
        }
    }
}