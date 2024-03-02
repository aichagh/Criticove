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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun NavBarGraph(navController: NavController) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "Dashboard") {
        composable("Create Review") {
            ReviewFormMainContent(navController)
        }
        composable("All Reviews") {
            TODO()
        }
        composable("Dashboard") {
            DashboardMainContent(navController)
        }
        composable("Friends") {
            TODO()
        }
    }
}

@Composable
fun Navbar(navController: NavController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .background(colorResource(id = R.color.blue)),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        TextButton(onClick = { navController.navigate("Create Review") }) {
            Icon(imageVector = ImageVector.vectorResource(id = R.drawable.write),
                contentDescription = "new review", tint = colorResource(id = R.color.off_white) )
        }

        TextButton(onClick = { TODO() }) {
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

        TextButton(onClick = { TODO() }) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.people_white),
                contentDescription = "friends", tint = colorResource(id = R.color.off_white)
            )
        }
    }
}

@Composable
@Preview
fun NavBarPreview() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .background(colorResource(id = R.color.blue)),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        TextButton(onClick = { TODO() }) {
            Icon(imageVector = ImageVector.vectorResource(id = R.drawable.write),
                contentDescription = "new review", tint = colorResource(id = R.color.off_white) )
        }

        TextButton(onClick = { TODO() }) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.list),
                contentDescription = "all reviews", tint = colorResource(id = R.color.off_white)
            )
        }

        TextButton(onClick = { TODO() }) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.homepage),
                contentDescription = "homepage", tint = colorResource(id = R.color.off_white)
            )
        }

        TextButton(onClick = { TODO() }) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.people_white),
                contentDescription = "friends", tint = colorResource(id = R.color.off_white)
            )
        }
    }
}
