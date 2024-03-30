package com.criticove

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.LiveTv
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.launch

data class MenuItem(val name: String, val icon: ImageVector, val route: String)

val menuItems = listOf(
    MenuItem("All", Icons.Filled.Checklist, "Reviews" ),
    MenuItem("Books", Icons.Filled.Book, "Books" ),
    MenuItem("Movies", Icons.Filled.Movie, "Movies"),
    MenuItem("TV Shows", Icons.Filled.LiveTv, "TVShows"),
    MenuItem("Bookmarks", Icons.Filled.Bookmark, "Bookmarks")
)

val menuItemsFriends = listOf(
    MenuItem("Friends Reviews", Icons.Filled.Checklist, "FriendsReviews"),
    MenuItem("Manage Friends", Icons.Filled.Group, "Friends"),
    MenuItem("Add Friends", Icons.Filled.Group, "AddFriends")
)

@Composable
fun DrawerContent(navController: NavController, friends: Boolean = false, closeDrawer: () -> Unit) {
    val backgroundColor = colorResource(id = R.color.green)
    val dividerColor = Color.Gray
    val itemsToShow = if (friends) menuItemsFriends else menuItems

    Column(
        modifier = Modifier
            .background(backgroundColor)
            .width(260.dp)
            .fillMaxHeight()
    ) {
        Spacer(modifier = Modifier.height(24.dp)) // Add padding at the top of the drawer

        itemsToShow.forEachIndexed { index, item ->
            // Create a custom drawer item
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .background(backgroundColor)
                    .clickable {
                        closeDrawer()
                        navController.navigate(item.route) {
                            popUpTo(0)
                        }
                    },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(item.icon, contentDescription = item.name, tint = Color.Black)
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = item.name,
                    color = Color.Black,
                    fontSize = 20.sp,
                    fontFamily = FontFamily(Font(R.font.alegreya_sans_regular))
                )
            }

            // Add a gray divider after each item except the last one
            if (index != itemsToShow.size - 1) {
            Divider(
                color = dividerColor,
                modifier = Modifier.padding(horizontal = 16.dp),
                thickness = 1.dp // This sets the thickness of the divider
            )

            }
        }
    }
}


@Composable
fun MainLayout(
    title: String,
    navController: NavController,
    friends: Boolean = false,
    content: @Composable (PaddingValues) -> Unit
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerContent(navController, friends, closeDrawer = {
                coroutineScope.launch { drawerState.close() }
            })
        }
    ) {
        Scaffold(
            topBar = {
                Topbar(
                    pageTitle = title,
                    onMenuClicked = { coroutineScope.launch { drawerState.open() } },
                    navController
                )
            }
        ) { innerPadding ->
            content(innerPadding)
        }
    }
}
