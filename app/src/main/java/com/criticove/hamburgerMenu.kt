// DrawerMenu.kt
package com.criticove

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Icon

data class MenuItem(val name: String, val icon: ImageVector)

val menuItems = listOf(
    MenuItem("Books", Icons.Filled.Book),
    MenuItem("Movies", Icons.Filled.Movie),
    MenuItem("TV Shows", Icons.Filled.Tv),
    MenuItem("Bookmarks", Icons.Filled.Bookmark)
)

@Composable
fun DrawerContent(onMenuItemClicked: (MenuItem) -> Unit) {
    Column {
        menuItems.forEach { item ->
            IconButton(onClick = { onMenuItemClicked(item) }) {
                Icon(imageVector = item.icon, contentDescription = item.name)
                Text(text = item.name, modifier = Modifier.padding(8.dp))
            }
        }
    }
}
