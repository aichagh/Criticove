package com.criticove

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.criticove.backend.userModel

@Composable
fun DashboardMainContent(navController: NavController, userModel: userModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(id = R.color.off_white))
    ) {
        DashboardHeader()

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(top = 10.dp, bottom = 20.dp, start = 20.dp, end = 20.dp)
                .background(color = colorResource(id = R.color.off_white)),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            WelcomeBanner()

            CallToActionExistingEntry(navController)

            TopGenres(navController)

            ReviewsPerMediaType(navController)

            ProgressTracker(navController)
        }

        Navbar(navController)
    }
}

@Composable
fun DashboardHeader() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .background(colorResource(id = R.color.blue))
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = null,
                modifier = Modifier
                    .padding(10.dp)
            )

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
    // TO DO: Get username from firebase - currently logged in user's displayName
    val username = "happyBunny123"

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Welcome, $username!",
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
fun CallToActionExistingEntry(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            modifier = Modifier.padding(bottom = 5.dp, start = 5.dp),
            text = "Continue editing your last entry",
            color = colorResource(id = R.color.blue),
            fontSize = 18.sp,
            fontFamily = FontFamily(Font(R.font.alegreya_sans_regular))
        )

        // TO DO: Retrieve user's latest entry details from firebase
        val title = "Stitching Snow"
        val author = "R.C. Lewis"
        val year = "2014"
        val rating = 3
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            colorResource(id = R.color.green),
                            colorResource(id = R.color.teal)
                        )
                    ),
                    shape = RoundedCornerShape(10.dp)
                )
                // TO DO: Navigate to corresponding edit review page
                .clickable { navController.navigate("Signup") },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.width(300.dp)
            ) {
                Text(
                    modifier = Modifier.padding(top = 20.dp, start = 20.dp),
                    text = title,
                    color = colorResource(id = R.color.blue),
                    fontSize = 24.sp,
                    fontFamily = FontFamily(Font(R.font.alegreya_sans_bold))
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        modifier = Modifier.padding(top = 5.dp, start = 20.dp),
                        text = author,
                        color = colorResource(id = R.color.blue),
                        fontSize = 20.sp,
                        fontFamily = FontFamily(Font(R.font.alegreya_sans_italic))
                    )

                    Spacer(modifier = Modifier.size(10.dp))

                    Column {
                        Spacer(modifier = Modifier.height(5.dp))
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .background(colorResource(id = R.color.blue), shape = CircleShape)
                        )
                    }

                    Text(
                        modifier = Modifier.padding(top = 5.dp, start = 10.dp),
                        text = year,
                        color = colorResource(id = R.color.blue),
                        fontSize = 20.sp,
                        fontFamily = FontFamily(Font(R.font.alegreya_sans_regular))
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                        .padding(top = 10.dp, bottom = 20.dp, start = 18.dp)
                ) {
                    for (i in 1..5) {
                        if (i <= rating) {
                            Icon(
                                imageVector = ImageVector.vectorResource(id = R.drawable.star_full),
                                contentDescription = "Filled Star",
                                tint = colorResource(id = R.color.blue)
                            )
                        } else {
                            Icon(
                                imageVector = ImageVector.vectorResource(id = R.drawable.star_empty),
                                contentDescription = "Outlined Star",
                                tint = colorResource(id = R.color.blue)
                            )
                        }
                    }
                }
            }

            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.right_arrow),
                contentDescription = "Open Entry",
                tint = colorResource(id = R.color.blue)
            )
        }
    }
}

@Composable
fun TopGenres(navController: NavController) {
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
        DashboardCardHeader("Your Top Genres", navController)

        Spacer(modifier = Modifier.size(15.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(150.dp)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                // TO DO: Get user's top 3 genres (number of entries in each genre)
                val numbers = listOf(6f, 10f, 7f)
                val colors = listOf(colorResource(id = R.color.black),
                    colorResource(id = R.color.teal),
                    colorResource(id = R.color.blue))

                PieChart(numbers, colors)
            }

            Column {
                // TO DO: Get the user's top genres and calculate percentages
                // TO DO: Also generate # of rows accordingly
                PieChartLegendRow(
                    color = colorResource(id = R.color.blue),
                    str = "Romance (30.4%)"
                )

                PieChartLegendRow(
                    modifier = Modifier.padding(top = 20.dp, bottom = 20.dp, start = 20.dp),
                    color = colorResource(id = R.color.black),
                    str = "Fantasy (26.1%)"
                )

                PieChartLegendRow(
                    color = colorResource(id = R.color.teal),
                    str = "Thriller (43.5%)"
                )
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
fun PieChartLegendRow(modifier: Modifier = Modifier, color: Color, str: String) {
    Row(
        modifier = modifier,
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
            text = str,
            color = color,
            fontSize = 18.sp,
            fontFamily = FontFamily(Font(R.font.alegreya_sans_bold))
        )
    }
}

@Composable
fun ReviewsPerMediaType(navController: NavController) {
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
        DashboardCardHeader("This month you...", navController)

        Spacer(modifier = Modifier.size(15.dp))

        // TO DO: Get user's stats on # of reviews per media type
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            MediaTypeStatsColumn(
                imageVector = ImageVector.vectorResource(id = R.drawable.movie_black),
                str = "Watched 3 movies"
            )

            MediaTypeStatsColumn(
                imageVector = ImageVector.vectorResource(id = R.drawable.book_black),
                str = "Read 10 books"
            )

            MediaTypeStatsColumn(
                imageVector = ImageVector.vectorResource(id = R.drawable.tv_black),
                str = "Watched 5 shows"
            )
        }
    }
}

@Composable
fun MediaTypeStatsColumn(imageVector: ImageVector, str: String) {
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

        Text(
            modifier = Modifier.width(80.dp),
            text = str,
            color = colorResource(id = R.color.off_white),
            fontSize = 20.sp,
            fontFamily = FontFamily(Font(R.font.alegreya_sans_bold)),
            textAlign = TextAlign.Center,
        )
    }
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
        DashboardCardHeader("Progress Tracker", navController)

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
fun DashboardCardHeader(heading: String, navController: NavController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .padding(top = 10.dp, start = 105.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier.padding(end = 60.dp),
            text = heading,
            color = colorResource(id = R.color.blue),
            fontSize = 24.sp,
            fontFamily = FontFamily(Font(R.font.alegreya_sans_bold))
        )

        Icon(
            // TO DO: Navigate to discussion page with stats as keyboard input
            modifier = Modifier
                .clickable { navController.navigate("Signup") }
                .size(30.dp),
            imageVector = ImageVector.vectorResource(id = R.drawable.share),
            contentDescription = "Share",
            tint = colorResource(id = R.color.blue)
        )
    }
}
