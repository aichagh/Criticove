package com.criticove

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

import com.criticove.backend.FirebaseManager
import com.criticove.backend.Review
import com.criticove.backend.userModel
import kotlinx.coroutines.flow.StateFlow
import java.text.DecimalFormat

@Composable
fun DashboardMainContent(navController: NavController, userModel: userModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(id = R.color.off_white))
    ) {
        DashboardHeader(navController)

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(bottom = 20.dp, start = 20.dp, end = 20.dp)
                .background(color = colorResource(id = R.color.off_white)),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally

        ) {
            WelcomeBanner()
            if (userModel.totalReviews == 0) {
                CallToAction(navController)
            }
            TopGenres(navController, userModel)
            ReviewsPerMediaType(userModel, navController)
            ProgressTracker(navController)
        }

        Navbar(navController)
    }
}

@Composable
fun DashboardHeader(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .background(colorResource(id = R.color.blue))
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(onClick = { navController.navigate("ProfilePage") }) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.default_profile),
                    contentDescription = "profile", tint = colorResource(id = R.color.off_white)
                )
            }

            Spacer(modifier = Modifier.width(80.dp))

            Text(
                text = "Dashboard",
                color = colorResource(id = R.color.off_white),
                fontSize = 30.sp,
                fontFamily = FontFamily(Font(R.font.alegreya_sans_bold))
            )
        }
    }
}

@Composable
fun WelcomeBanner() {
    val username = FirebaseManager.getUsername()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Welcome $username!",
            style = TextStyle(
                brush = Brush.linearGradient(
                    colors = listOf(
                        colorResource(id = R.color.blue),
                        colorResource(id = R.color.teal),
                        colorResource(id = R.color.blue),
                        colorResource(id = R.color.teal),
                        colorResource(id = R.color.blue),
                        colorResource(id = R.color.teal)),
                    tileMode = TileMode.Mirror)),
            fontSize = 30.sp,
            fontFamily = FontFamily(Font(R.font.alegreya_sans_bold))
        )
    }
}

@Composable
fun CallToAction(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .padding(bottom = 20.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            colorResource(id = R.color.teal),
                            colorResource(id = R.color.blue)
                        )
                    ),
                    shape = RoundedCornerShape(10.dp)
                )
                .clickable { navController.navigate("Reviews") },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.width(300.dp)
            ) {
                CallToActionText("This is your cove of media critiques and reflections.", true)
                CallToActionText("Get started now.")
            }

            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.right_arrow),
                contentDescription = "Open Review Page",
                tint = colorResource(id = R.color.off_white)
            )
        }
    }
}

@Composable
fun CallToActionText(str: String, italicize: Boolean = false) {
    Text(
        modifier = if (italicize) {
            Modifier.padding(top = 20.dp, start = 20.dp)
        } else {
            Modifier.padding(top = 20.dp, start = 20.dp, bottom = 20.dp)
               },
        text = str,
        color = colorResource(id = R.color.off_white),
        fontSize = 24.sp,
        fontFamily = if (italicize) {
            FontFamily(Font(R.font.alegreya_sans_bold_italic))
        } else {
            FontFamily(Font(R.font.alegreya_sans_bold))
        }
    )
}

@Composable
fun TopGenres(navController: NavController, userModel: userModel) {
    userModel.getReviews()
    val genres: List<Pair<Double, String>> = calcTopGenres(userModel.reviewList)
    val colors = listOf(colorResource(id = R.color.blue),
        colorResource(id = R.color.black),
        colorResource(id = R.color.coolGrey),
        colorResource(id = R.color.teal))

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                colorResource(id = R.color.green),
                shape = RoundedCornerShape(10.dp)
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        DashboardCardHeader("Your Top Genres")

        if (genres.isEmpty()) {
            Text(
                text = "You do not have any reviews yet.",
                color = colorResource(id = R.color.blue),
                fontSize = 18.sp,
                fontFamily = FontFamily(Font(R.font.alegreya_sans_regular)),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 20.dp),
                textAlign = TextAlign.Center
            )

        } else {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 20.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(150.dp)
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    val numbers = mutableListOf<Float>()

                    for (i in 0..3) {
                        if (i < genres.size) {
                            numbers.add(genres[i].first.toFloat())
                        }
                    }

                    PieChart(numbers, colors)
                }

                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    for (i in 0..3) {
                        if (i < genres.size) {
                            PieChartLegendRow(
                                color = colors[i],
                                str = genres[i].second,
                                num = genres[i].first
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PieChart(numbers: List<Float>, colors: List<Color>) {
    Canvas(modifier = Modifier.size(150.dp)) {
        var startAngle = 0f
        val total = numbers.sum()

        numbers.forEachIndexed { index, value ->
            val sweepAngle = (value / total) * 360
            drawArc(
                color = colors[index],
                startAngle = startAngle,
                sweepAngle = sweepAngle,
                useCenter = true,
                style = Fill
            )
            startAngle += sweepAngle
        }
    }
}

@Composable
fun PieChartLegendRow(color: Color, str: String, num: Double) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier.size(20.dp),
            imageVector = ImageVector.vectorResource(id = R.drawable.square),
            contentDescription = null,
            tint = color
        )

        Text(
            modifier = Modifier.padding(start = 10.dp),
            text = "$str ($num%)",
            color = color,
            fontSize = 18.sp,
            fontFamily = FontFamily(Font(R.font.alegreya_sans_bold))
        )
    }
}

@Composable
fun ReviewsPerMediaType(userModel: userModel, navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(260.dp)
            .padding(top = 20.dp)
            .background(
                colorResource(id = R.color.green),
                shape = RoundedCornerShape(10.dp)
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        DashboardCardHeader("So far, you have reviewed...")

        Spacer(modifier = Modifier.size(15.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            MediaTypeStatsColumn(
                imageVector = ImageVector.vectorResource(id = R.drawable.book_black),
                num = userModel.totalBookReviews,
                str = "book"
            )

            MediaTypeStatsColumn(
                imageVector = ImageVector.vectorResource(id = R.drawable.movie_black),
                num = userModel.totalMovieReviews,
                str = "movie"
            )

            MediaTypeStatsColumn(
                imageVector = ImageVector.vectorResource(id = R.drawable.tv_black),
                num = userModel.totalTVShowReviews,
                str = "show"
            )
        }
    }
}

@Composable
fun MediaTypeStatsColumn(imageVector: ImageVector, num: Int, str: String) {
    var mediaType = str
    if (num != 1) { mediaType += "s"}

    Column(
        modifier = Modifier
            .height(150.dp)
            .width(100.dp)
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        colorResource(id = R.color.teal),
                        colorResource(id = R.color.blue)
                    )
                ),
                shape = RoundedCornerShape(10.dp)
            ),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            modifier = Modifier
                .size(50.dp),
            imageVector = imageVector,
            contentDescription = null,
            tint = colorResource(id = R.color.off_white)
        )

        MediaTypeStatsColumnText("$num")

        MediaTypeStatsColumnText(mediaType)
    }
}

@Composable
fun MediaTypeStatsColumnText(str: String) {
    Text(
        modifier = Modifier.width(80.dp),
        text = str,
        color = colorResource(id = R.color.off_white),
        fontSize = 20.sp,
        fontFamily = FontFamily(Font(R.font.alegreya_sans_bold)),
        textAlign = TextAlign.Center,
    )
}

@Composable
fun ProgressTracker(navController: NavController) {
    // TO DO: Get user's progress stats
    // TO DO: Add edit option (popup..?)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(top = 20.dp)
            .background(
                colorResource(id = R.color.green),
                shape = RoundedCornerShape(10.dp)
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        DashboardCardHeader("Progress Tracker")

        Spacer(modifier = Modifier.size(15.dp))

        LinearProgressIndicator(
            progress = { 0.5f },
            modifier = Modifier.height(30.dp),
            color = colorResource(id = R.color.teal),
            trackColor = colorResource(id = R.color.off_white),
            strokeCap = StrokeCap.Round
        )

        Text(
            text = "50%",
            color = colorResource(id = R.color.blue),
            fontSize = 18.sp,
            fontFamily = FontFamily(Font(R.font.alegreya_sans_bold)),
            textAlign = TextAlign.Center,
        )

        Spacer(modifier = Modifier.size(15.dp))

        Text(
            text = "25/50 reviews",
            color = colorResource(id = R.color.blue),
            fontSize = 24.sp,
            fontFamily = FontFamily(Font(R.font.alegreya_sans_bold)),
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
fun DashboardCardHeader(heading: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .padding(top = 15.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = heading,
            color = colorResource(id = R.color.blue),
            fontSize = 24.sp,
            fontFamily = FontFamily(Font(R.font.alegreya_sans_bold))
        )
    }
}

@Preview
@Composable
fun Preview_Display() {
        //DashboardMainContent(rememberNavController())
}

@Composable
fun calcTopGenres(reviewList: StateFlow<MutableList<Review>>): List<Pair<Double, String>> {
    val reviewsList by reviewList.collectAsState()
    var temp = mutableListOf<Pair<Double, String>>()
    val finalList = mutableListOf<Pair<Double, String>>()
    val allGenres = mutableListOf<String>()

    for (review in reviewsList) {
        allGenres.add(review.genre)
    }

    val totalReviews = reviewsList.size.toDouble()

    val distinctGenres: List<String> = allGenres.distinct()

    distinctGenres.forEach { el ->
        val genreCount = allGenres.count { it == el }
        val percentage = (genreCount / totalReviews) * 100
        temp.add(Pair(DecimalFormat("#.#").format(percentage).toDouble(), el))
    }

    temp = temp.sortedByDescending{ it.first }.toMutableList()
    var other = 100.0

    for (i in 0..2) {
        if (i < temp.size) {
            other -= temp[i].first
            finalList.add(temp[i])
        }
    }

    if (temp.size > 3) {
        finalList.add(Pair(DecimalFormat("#.#").format(other).toDouble(), "Other"))
    }

    return finalList.sortedByDescending{ it.first }
}
