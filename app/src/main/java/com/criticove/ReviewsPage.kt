package com.criticove

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.vectorResource
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
import com.criticove.backend.TVShowReview
import com.criticove.backend.userModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ReviewsPage: ComponentActivity() {
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val userModel: userModel by viewModels()
        lifecycleScope.launch {
            userModel.getReviews()
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                userModel.reviewList.collect {
                    println("here review list is ${userModel.reviewList}")
                    setContent {
                        ReviewPageMainContent(rememberNavController())

                    }
                }
            }
        }
    }
}

@Composable
fun ReviewPageMainContent(navController: NavController) {
    var userModel = userModel()
    userModel.getReviews()
    println("in review page main content")
    Column(
        modifier = Modifier
            .background(colorResource(id = R.color.off_white))
            .fillMaxHeight()
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.SpaceBetween
    ) {

        Topbar()

        Column(
            verticalArrangement = Arrangement.Top,
            modifier = Modifier
                .weight(1F)
        ) {
            print("after calling ${userModel.reviewList}")
            displayReviews(userModel.reviewList)
        }

        Navbar()
    }

}

// This is a temporary function, later this would take the rating in account
@Composable
fun Stars() {
    repeat(5) {
        Icon(
            modifier = Modifier
                .height(30.dp),
            imageVector = ImageVector.vectorResource(id = R.drawable.star_full),
            contentDescription = "star", tint = colorResource(id = R.color.black)
        )
    }
}
@Composable
fun Review(title: String = "Title",
           author: String = "Author",
           year: Number = 0,
           rating: Number = 1) {
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
                    fontSize = 20.sp
                )
                Text("$author, $year")
                Row() {
                    Stars()
                }
            }

            TextButton(
                modifier = Modifier
                    .width(50.dp),
                onClick = { TODO() }
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.bookmark_empty),
                    contentDescription = "bookmark", tint = colorResource(id = R.color.black)
                )
            }
        }
    }
}

@Composable
fun displayReviews(reviewList: StateFlow<MutableList<Review>>) {
    val reviewsList by reviewList.collectAsState()
    println("the review list in reviewspage is $reviewsList")
    println(reviewList)
    for (review in reviewsList) {
        when (review) {
            is BookReview -> {
                val bookReview: BookReview = review
                println("here book review")
                println("this is the review $review")
                Review(bookReview.title, bookReview.author, 1999, 9)
            }
            is TVShowReview -> {
                val tvShowReview: TVShowReview = review
                Review(tvShowReview.title, tvShowReview.director, 1999, 9)
            }
            is MovieReview -> {
                val tvShowReview: MovieReview = review
                Review(tvShowReview.title, tvShowReview.director, 1999, 9)
            }
        }
    }
}
@Composable
@Preview
fun ReviewsPagePreview() {
    val userModel = userModel()
    Column (
        modifier = Modifier
            .background(colorResource(id = R.color.off_white))
            .fillMaxHeight()
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.SpaceBetween
    ){

        Topbar()

        Column(
            verticalArrangement = Arrangement.Top,
            modifier = Modifier
                .weight(1F)
        ){
            repeat(3) {
                Review()
            }
        }

        Navbar()
    }
}