package com.criticove

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.criticove.backend.BookReview
import com.criticove.backend.MovieReview
import com.criticove.backend.Review
import com.criticove.backend.TVShowReview
import com.criticove.backend.changeBookmark
import com.criticove.backend.userModel
import kotlinx.coroutines.flow.StateFlow

@Composable
fun ReviewPageMainContent(navController: NavController, userModel: userModel) {
    userModel.getReviews()
    val totalReviews = getTotalReviews(userModel.reviewList)

    var sortBy by remember { mutableStateOf("Newest") }

    MainLayout(
        title = "All Reviews",
        navController = navController
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .background(colorResource(id = R.color.off_white))
                .fillMaxHeight()
                .fillMaxWidth(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            if (totalReviews == 0) {
                Text(
                    text = "You do not have any reviews yet.",
                    color = colorResource(id = R.color.blue),
                    fontSize = 20.sp,
                    fontFamily = FontFamily(Font(R.font.alegreya_sans_medium)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 300.dp, start = 15.dp, end = 15.dp),
                    textAlign = TextAlign.Center
                )
            } else {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .padding(top = 15.dp, end = 15.dp),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.Bottom
                ) {
                    SortingButton(
                        text = "Newest",
                        selected = sortBy == "Newest",
                        onClick = { sortBy = "Newest" }
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    SortingButton(
                        text = "Oldest",
                        selected = sortBy == "Oldest",
                        onClick = { sortBy = "Oldest" }
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    SortingButton(
                        text = "A to Z",
                        selected = sortBy == "A to Z",
                        onClick = { sortBy = "A to Z" }
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    SortingButton(
                        text = "Z to A",
                        selected = sortBy == "Z to A",
                        onClick = { sortBy = "Z to A" }
                    )
                }
            }

            Box(
                modifier = Modifier
                    .weight(1F)
                    .fillMaxSize()
            ) {
                Column(
                    verticalArrangement = Arrangement.Top,
                    modifier = Modifier
                        .fillMaxHeight()
                        .verticalScroll(rememberScrollState()),
                ) {
                    displayReviews(navController, userModel.reviewList, sortBy)
                    Spacer(modifier = Modifier.size(15.dp))
                }

                FloatingAddButton(navController)
            }

            Navbar(navController)
        }
    }
}

@Composable
fun SortingButton(text: String, selected: Boolean, onClick: () -> Unit) {
    TextButton(
        modifier = Modifier
            .background(
                color = if (selected) colorResource(id = R.color.teal) else colorResource(id = R.color.green),
                shape = RoundedCornerShape(20.dp)
            )
            .height(40.dp),
        onClick = onClick
    ) {
        Text(
            text = text,
            color = if (selected) colorResource(id = R.color.off_white) else colorResource(id = R.color.black),
            fontSize = 16.sp,
            fontFamily = FontFamily(Font(R.font.alegreya_sans_medium))
        )
    }
}

@Composable
fun FloatingAddButton(navController: NavController) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(15.dp),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.Bottom
    ) {
        IconButton(
            onClick = { navController.navigate("ReviewForm") },
            modifier = Modifier
                .size(80.dp)
                .padding(10.dp)
                .clip(CircleShape)
                .background(colorResource(id = R.color.teal)),
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.plus),
                contentDescription = "friends", tint = colorResource(id = R.color.off_white),
                modifier = Modifier
                    .height(40.dp)

            )
        }
    }
}

@Composable
fun Stars(rating: Int) {
    var id = R.drawable.star_full
    println("got id in stars() $id")
    for (i in 1..5) {
        if (i > rating) {
            id = R.drawable.star_empty
        }
        Icon(
            modifier = Modifier
                .height(30.dp),
            imageVector = ImageVector.vectorResource(id = id),
            contentDescription = "star", tint = colorResource(id = R.color.black)
        )
    }
}
@Composable
fun Review(title: String = "Title",
           author: String,
           year: String = "1999",
           rating: Int = 1,
           reviewID: String,
           navController: NavController,
           bookmarked: Boolean = false) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp, 15.dp, 15.dp, 0.dp)
            .clip(shape = RoundedCornerShape(5.dp, 5.dp, 5.dp, 5.dp))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(colorResource(id = R.color.green))
                .padding(20.dp)
        ) {
            Column(
                modifier = Modifier
                    .width(100.dp)
                    .weight(1F),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = title,
                    fontSize = 24.sp,
                    fontFamily = FontFamily(Font(R.font.alegreya_sans_medium))
                )
                var author_text = "$author, $year"
                if (author == "null") {
                    author_text = "$year"
                }
                Text(
                    text = author_text,
                    fontSize = 16.sp,
                    fontFamily = FontFamily(Font(R.font.alegreya_sans_regular))
                )
                Row() {
                    Stars(rating)
                }
            }

            Row {
                TextButton(
                    modifier = Modifier.width(50.dp),
                    onClick = { navController.navigate("ViewReview/$reviewID/false/none") }
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.view),
                        contentDescription = "view", tint = colorResource(id = R.color.black)
                    )
                }
                var bm by remember { mutableStateOf(bookmarked) }
                TextButton(
                    modifier = Modifier.width(50.dp),
                    onClick = {
                        bm = !bm
                        changeBookmark(reviewID, bm)
                    }
                ) {

                    var id = R.drawable.bookmark_empty
                    if (bm) {
                        id = R.drawable.bookmark_full
                    }
                    Icon(
                        imageVector = ImageVector.vectorResource(id = id),
                        contentDescription = "bookmark", tint = colorResource(id = R.color.black)
                    )
                }
            }
        }
    }
}

@Composable
fun displayReviews(navController: NavController, reviewList: StateFlow<MutableList<Review>>, sortBy: String) {
    val reviewsList by reviewList.collectAsState()

    val sortedReviews = when (sortBy) {
        "Oldest" -> reviewsList
        "Newest" -> reviewsList.reversed()
        "A to Z" -> reviewsList.sortedBy { it.title }
        "Z to A" -> reviewsList.sortedByDescending { it.title }
        else -> reviewsList.reversed()
    }

    for (review in sortedReviews) {
        when (review) {
            is BookReview -> {
                val bookReview: BookReview = review
                println("this is the reviews  bookmarked ${bookReview.bookmarked}")
                Review(bookReview.title, bookReview.author, bookReview.date, bookReview.rating, bookReview.reviewID, navController, bookReview.bookmarked)
            }
            is TVShowReview -> {
                val tvShowReview: TVShowReview = review
                Review(tvShowReview.title, tvShowReview.director,tvShowReview.date, tvShowReview.rating, tvShowReview.reviewID, navController, tvShowReview.bookmarked)
            }
            is MovieReview -> {
                val movieReview: MovieReview = review
                Review(movieReview.title, movieReview.director, movieReview.date, movieReview.rating, movieReview.reviewID, navController, movieReview.bookmarked)
            }
        }
    }
}
