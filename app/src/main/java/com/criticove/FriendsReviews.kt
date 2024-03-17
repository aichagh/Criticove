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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.criticove.MainLayout
import com.criticove.Navbar
import com.criticove.R
import com.criticove.Stars
import com.criticove.backend.userModel
import com.criticove.displayReviews

val profilePic = R.drawable.default_pic // later if profile pic is set, change it

@Composable
fun FriendsReviews(navController: NavController) {
    MainLayout(
        title = "Friends Reviews",
        navController = navController
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .background(colorResource(id = R.color.off_white))
                .fillMaxHeight()
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            val userModel = userModel()
            userModel.getReviews()
            println("in review page main content")

            Column(
                verticalArrangement = Arrangement.Top,
                modifier = Modifier
                    .weight(1F)
            ) {
                // Add here
            }

            Navbar(navController)
        }
    }
}

@Composable
@Preview
fun FriendReview(
    username: String = "realfriendtm", title: String = "Title",
    author: String = "Author", year: String = "1999", rating: Int = 1) {
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
                        text = "$username",
                        fontSize = 18.sp,
                        fontFamily = FontFamily(Font(R.font.alegreya_sans_medium))
                    )
                }

                Text(
                    text = title,
                    fontSize = 24.sp,
                    fontFamily = FontFamily(Font(R.font.alegreya_sans_medium))
                )
                Text(
                    text = "$author, $year",
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
                onClick = { }
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.bookmark_empty),
                    contentDescription = "bookmark", tint = colorResource(id = R.color.black)
                )
            }
        }
    }
}