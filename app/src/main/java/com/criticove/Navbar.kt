package com.criticove

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Icon
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun Navbar(navController: NavController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .background(colorResource(id = R.color.blue)),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        TextButton(onClick = { navController.navigate("ReviewForm") }) {
            Icon(imageVector = ImageVector.vectorResource(id = R.drawable.write),
                contentDescription = "new review", tint = colorResource(id = R.color.off_white) )
        }

        TextButton(onClick = { navController.navigate("Reviews") }) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.list),
                contentDescription = "all reviews", tint = colorResource(id = R.color.off_white)
            )
        }

        TextButton(onClick = { navController.navigate("Dashboard") }) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.homepage),
                contentDescription = "homepage", tint = colorResource(id = R.color.off_white)
            )
        }

        TextButton(onClick = { navController.navigate("Friends") }) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.people_white),
                contentDescription = "friends", tint = colorResource(id = R.color.off_white)
            )
        }
    }
}
