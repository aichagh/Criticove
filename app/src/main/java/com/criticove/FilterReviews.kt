package com.criticove

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.criticove.backend.BookReview
import com.criticove.backend.MovieReview
import com.criticove.backend.Review
import com.criticove.backend.TVShowReview
import com.criticove.backend.userModel

@Composable
fun FilterReviews(navController: NavController, userModel: userModel, type: String) {
    MainLayout(
        title = "$type Reviews",
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
            userModel.getReviews()
            println("in filter reviews ")

            Column(
                verticalArrangement = Arrangement.Top,
                modifier = Modifier
                    .weight(1F)
                    .padding(bottom = 10.dp)
                    .verticalScroll(rememberScrollState()),
            ) {
                displayFilteredReviews(userModel.filter(type), navController)
            }

            Navbar(navController)
        }
    }
}
@Composable
fun displayFilteredReviews(filteredReviews: List<Review>, navController: NavController) {
    for (review in filteredReviews) {
        when (review) {
            is BookReview -> {
                val bookReview: BookReview = review
                println("here book review")
                println("this is the review $review")
                Review(bookReview.title, bookReview.author, bookReview.date, bookReview.rating, bookReview.reviewID, navController)
            }
            is TVShowReview -> {
                val tvShowReview: TVShowReview = review
                Review(tvShowReview.title, tvShowReview.director,tvShowReview.date, tvShowReview.rating, tvShowReview.reviewID, navController)
            }
            is MovieReview -> {
                val movieReview: MovieReview = review
                Review(movieReview.title, movieReview.director, movieReview.date, movieReview.rating, movieReview.reviewID, navController)
            }
        }
    }
}
