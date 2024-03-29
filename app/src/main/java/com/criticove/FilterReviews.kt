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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.criticove.backend.userModel

@Composable
fun FilterReviews(navController: NavController, userModel: userModel, type: String, bookmarks: Boolean = false) {
    userModel.getReviews()
    val filteredReviews = userModel.filter(type, bookmarks)
    val sorting = listOf("Newest", "Oldest", "A to Z", "Z to A")

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
                    .padding(5.dp),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (filteredReviews.isNotEmpty()) {
                    var expanded by remember { mutableStateOf(false) }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
//                            .padding(vertical = 10.dp),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.Bottom,
                    ) {
                        TextButton(
                            onClick = { expanded = true },
                        ) {
                            Text(
                                text = "Sort by : $sortBy",
                                fontSize = 18.sp,
                                color = colorResource(id = R.color.black),
                                fontFamily = FontFamily(Font(R.font.alegreya_sans_regular))
                            )

                            Spacer(modifier = Modifier.size(15.dp))

                            Icon(
                                imageVector = ImageVector.vectorResource(id = R.drawable.funnel),
                                contentDescription = "filter",
                                tint = colorResource(id = R.color.black),
                                modifier = Modifier
                                    .height(30.dp)
                            )
                        }

                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false },
                            modifier = Modifier
                                .background(colorResource(id = R.color.off_white))
                                .fillMaxWidth()
                        ) {
                            sorting.forEachIndexed { index, el ->
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            text = el,
                                            fontFamily = FontFamily(Font(R.font.alegreya_sans_regular)),
                                            textAlign = TextAlign.Center,
                                            modifier = Modifier.fillMaxSize()
                                        )
                                    },
                                    onClick = {
                                        if (el != sortBy) { // Ignore clicks on placeholder
                                            sortBy = el
                                        }
                                        expanded = false
                                    },
                                    modifier = Modifier
                                        .background(colorResource(id = R.color.off_white))
                                        .fillMaxWidth(),
                                )
                                if (index != sorting.size - 1) {
                                    HorizontalDivider()
                                }
                            }
                        }
                    }
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
