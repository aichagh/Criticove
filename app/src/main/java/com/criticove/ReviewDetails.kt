package com.criticove

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.criticove.backend.BookReview
import com.criticove.backend.MovieReview
import com.criticove.backend.Review
import com.criticove.backend.TVShowReview
import com.criticove.backend.delSelectedReview
import com.criticove.backend.updateSelReview
import com.criticove.backend.userModel

// get this data from database, or passed from review select page, currently sample data
var reviewID = "Insert ID"   // replace with other id
// var reviewData = getSelectedReview(reviewID)
var reviewType = ""   // reviewData["type"]!!

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

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun ReviewDetailsMainContent(navController: NavController,
                             reviewID: String,
                             isFriend: Boolean,
                             friendID: String,
                             userModel: userModel) {
    // userModel.getSelReview(reviewID, friendID)
    // val selOReview by userModel.selReview.collectAsState()
    userModel.getSelReview(reviewID, friendID)
    val selReview by userModel.selReview.collectAsState()
    println(reviewID)

    Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
//                .verticalScroll(rememberScrollState())
                .background(colorResource(id = R.color.off_white))
        ) {
            if(isFriend) {
                CommonHeader(navController, selReview.title, "FriendsReviews")
            } else {
                CommonHeader(navController, selReview.title, "Reviews")
            }
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
                "Title", "Date Released", "Genre", "Streaming Service",
                "Date finished", "Rating", "Review"
            ).toMutableList()

            val tvReview: TVShowReview = selReview as TVShowReview

            reviewData["Title"] = tvReview.title
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
                "Title", "Date Released", "Genre", "Streaming Service",
                "Date watched", "Rating", "Review"
            ).toMutableList()

            val movieReview: MovieReview = selReview as MovieReview

            reviewData["Title"] = movieReview.title
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

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun displayReviewDetails(type: String, reviewData: MutableMap<String, String>,
                         elements: MutableList<String>,
                         reviewID: String, isFriend: Boolean,
                         navController: NavController) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(colorResource(id = R.color.off_white))
            .padding(10.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        Column() {
            println("in the review details page but not yet added info")
            elements.forEach { label ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Column() {
                        var curData = reviewData[label].toString()
                        var edit by remember { mutableStateOf(false) }
                        // var curData by remember {mutableStateOf(reviewData[label].toString())}

                        if (label != "Review") {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(5.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "$label: ",
                                    fontSize = 16.sp,
                                    fontFamily = FontFamily(Font(R.font.alegreya_sans_medium)),
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
                                label = {
                                    Text(
                                        text = "Review",
                                        fontSize = 16.sp,
                                        fontFamily = FontFamily(Font(R.font.alegreya_sans_medium)),
                                        color = colorResource(id = R.color.teal))
                                        },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .horizontalScroll(rememberScrollState())
                                    .padding(horizontal = 10.dp),
                                colors = TextFieldDefaults.outlinedTextFieldColors(
                                    focusedBorderColor = colorResource(id = R.color.blue),
                                    unfocusedBorderColor = colorResource(id = R.color.teal),
                                    disabledTextColor = colorResource(id = R.color.black),
                                ),
                                textStyle = TextStyle(
                                    fontFamily = FontFamily(Font(R.font.alegreya_sans_regular)),
                                    fontSize = 18.sp,
//                                    color = colorResource(id = R.color.black))
                                )
                            )

                            Spacer(modifier = Modifier.size(15.dp))

                            if (!isFriend) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceEvenly
                                ) {
                                    if (!edit) {
                                        CustomButton("Edit") { edit = true }
                                        CustomButton("Delete") {
                                            //navController.navigate("Reviews")
                                            navController.popBackStack()
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
