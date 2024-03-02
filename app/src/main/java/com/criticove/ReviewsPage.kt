package com.criticove

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class ReviewsPage: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Column (
                modifier = Modifier
                    .fillMaxHeight()
            ){
                Topbar()

                Column {

                }

                NavBarPreview()
            }
        }
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
@Preview
fun ReviewsPagePreview() {
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

        NavBarPreview()
    }
}