package com.criticove

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
    var value = navController.currentDestination?.route
    var home_tint_color = colorResource(id = R.color.off_white)
    var reviews_tint_color = colorResource(id = R.color.off_white)
    var friends_tint_color = colorResource(id = R.color.off_white)
    if (value == "Dashboard") {
        home_tint_color = colorResource(id = R.color.green)
    }
    if (value == "Reviews") {
        reviews_tint_color = colorResource(id = R.color.green)
    }
    if (value == "FriendsReviews") {
        friends_tint_color = colorResource(id = R.color.green)
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .background(colorResource(id = R.color.blue))
            .padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        TextButton(onClick = { navController.navigate("Dashboard")
        }) {
//
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.homepage),
                contentDescription = "homepage", tint = home_tint_color
            )
        }

        TextButton(onClick = { navController.navigate("Reviews") }) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.list),
                contentDescription = "all reviews", tint = reviews_tint_color
            )
        }

        TextButton(onClick = { navController.navigate("FriendsReviews") }) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.people_white),
                contentDescription = "friends", tint = friends_tint_color
            )
        }

    }
}
