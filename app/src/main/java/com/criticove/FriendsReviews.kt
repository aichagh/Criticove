import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.criticove.MainLayout
import com.criticove.Navbar
import com.criticove.R
import com.criticove.Stars
import com.criticove.backend.BookReview
import com.criticove.backend.MovieReview
import com.criticove.backend.Review
import com.criticove.backend.TVShowReview
import com.criticove.backend.userModel

val profilePic = R.drawable.default_pic // later if profile pic is set, change it

@SuppressLint("UnrememberedMutableState")
@Composable
fun createFriendsReviews(usermodel: userModel, navController: NavController) {
    val friendsReviewsList by usermodel.friendReviews.collectAsState()
    if (friendsReviewsList.isEmpty()) {
        Text(
            text = "No reviews to show here.",
            color = colorResource(id = R.color.blue),
            fontSize = 20.sp,
            fontFamily = FontFamily(Font(R.font.alegreya_sans_medium)),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 300.dp, start = 15.dp, end = 15.dp),
            textAlign = TextAlign.Center
        )
    } else {
        for ((key, value) in friendsReviewsList) {
            DisplayFriendsReviews(key, value, navController)
        }
    }

}

@Composable
fun FriendsReviews(navController: NavController, usermodel: userModel) {
    usermodel.getfriendReviews()
    MainLayout(
        title = "Friends' Reviews",
        navController = navController,
        friends = true
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .background(colorResource(id = R.color.off_white))
                .fillMaxHeight()
                .fillMaxWidth(),

            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                verticalArrangement = Arrangement.Top,
                modifier = Modifier
                    .weight(1F)
                    .padding(bottom = 10.dp)
                    .verticalScroll(rememberScrollState()),
            ) {
                createFriendsReviews(usermodel, navController)
            }

            Navbar(navController)
        }
    }
}

@Composable
fun FriendReview(
    username: String = "realfriendtm", title: String = "Title",
    author: String = "Author", year: String = "1999", rating: Int = 1,
    reviewID: String, friendID: String, navController: NavController) {
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
                Row() {
                    Box(
                        modifier = Modifier.padding(0.dp, 0.dp, 5.dp, 0.dp)
                    ) {
                        Image(
                            painter = painterResource(id = profilePic),
                            contentDescription = "profile picture",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(20.dp)
                                .clip(CircleShape)
                        )
                    }
                    Text(
                        text = username,
                        fontSize = 18.sp,
                        fontFamily = FontFamily(Font(R.font.alegreya_sans_medium))
                    )
                }

                Text(
                    text = title,
                    fontSize = 24.sp,
                    fontFamily = FontFamily(Font(R.font.alegreya_sans_medium))
                )
                var authorText = "$author, $year"
                if (author == "null") {
                    authorText = year
                }
                Text(
                    text = authorText,
                    fontSize = 16.sp,
                    fontFamily = FontFamily(Font(R.font.alegreya_sans_regular))
                )
                Row() {
                    Stars(rating)
                }
            }

            TextButton(
                modifier = Modifier
                    .width(50.dp),
                onClick = {
                    navController.navigate("ViewReview/$reviewID/true/$friendID")
                }
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.view),
                    contentDescription = "bookmark", tint = colorResource(id = R.color.black)
                )
            }
        }
    }
}

@Composable
fun DisplayFriendsReviews(key: Pair<String, String>, reviewsList: List<Review>,
                          navController: NavController) {
    for (review in reviewsList) {
        val (username, friendID) = key
        when (review) {
            is BookReview -> {
                val bookReview: BookReview = review
                FriendReview(username, bookReview.title, bookReview.author, bookReview.date, bookReview.rating, bookReview.reviewID, friendID, navController)
            }
            is TVShowReview -> {
                val tvShowReview: TVShowReview = review
                FriendReview(username, tvShowReview.title, tvShowReview.director,tvShowReview.date, tvShowReview.rating, tvShowReview.reviewID, friendID, navController)
            }
            is MovieReview -> {
                val movieReview: MovieReview = review
                FriendReview(username, movieReview.title, movieReview.director, movieReview.date, movieReview.rating, movieReview.reviewID, friendID, navController)
            }
        }
    }
}