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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.criticove.backend.BookReview
import com.criticove.backend.MovieReview
import com.criticove.backend.Review
import com.criticove.backend.TVShowReview
import com.criticove.backend.userModel

@Composable
fun FilterReviews(navController: NavController, userModel: userModel, type: String, bookmarks: Boolean = false) {
    userModel.getReviews()
    val filteredReviews = userModel.filter(type, bookmarks)

    var title = type

    if (bookmarks) {
        title = "Bookmarked"
    }

    var sortBy by remember { mutableStateOf("Newest") }

    MainLayout(
        title = "$title Reviews",
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

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .padding(top = 15.dp, end = 15.dp),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.Bottom
            ) {
                if (filteredReviews.isNotEmpty()) {
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
                    displayFilteredReviews(filteredReviews, title, sortBy, navController)
                    Spacer(modifier = Modifier.size(15.dp))
                }

                FloatingAddButton(navController)
            }

            Navbar(navController)
        }
    }
}
@Composable
fun displayFilteredReviews(filteredReviews: List<Review>, type: String, sortBy: String, navController: NavController) {
    val noReviewsText = when (type) {
        "Book" -> "reviewed any books yet"
        "Movie" -> "reviewed any movies yet"
        "TV Show" -> "reviewed any TV shows yet"
        "Bookmarked" -> "bookmarked any reviews yet"
        else -> "media"
    }

    if (filteredReviews.isEmpty()) {
        Text(
            text = "You have not $noReviewsText.",
            color = colorResource(id = R.color.blue),
            fontSize = 20.sp,
            fontFamily = FontFamily(Font(R.font.alegreya_sans_medium)),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 300.dp, start = 15.dp, end = 15.dp),
            textAlign = TextAlign.Center
        )
    } else {
        val sortedReviews = when (sortBy) {
            "Oldest" -> filteredReviews
            "Newest" -> filteredReviews.reversed()
            "A to Z" -> filteredReviews.sortedBy { it.title }
            "Z to A" -> filteredReviews.sortedByDescending { it.title }
            else -> filteredReviews.reversed()
        }

        for (review in sortedReviews) {
            when (review) {
                is BookReview -> {
                    val bookReview: BookReview = review
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
}
