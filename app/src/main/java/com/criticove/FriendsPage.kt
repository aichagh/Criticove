package com.criticove

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

data class Friend(val firstName: String, val lastName: String)

var ogFriendsList = mutableStateListOf(
Friend("Sara", "Z"),
Friend("Reem", "F"),
Friend("Uma", "G"),
Friend("Sharmistha", "G"),
Friend("Ashmita", "M")
)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FriendsMainContent(navController: NavController) {
    var searchText by remember { mutableStateOf("") }
    var isSearchActive by remember { mutableStateOf(false) }
    var filteredFriends by remember { mutableStateOf(emptyList<Friend>()) }

    fun perform_search() {
        filteredFriends = if (isSearchActive) {
            ogFriendsList.filter {
                it.firstName.contains(searchText, ignoreCase = true) ||
                        it.lastName.contains(searchText, ignoreCase = true)
            }
        } else {
            ogFriendsList
        }
    }

    perform_search()
    
    Column (
        modifier = Modifier
            .background(colorResource(id = R.color.off_white))
            .fillMaxHeight()
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.SpaceBetween
    ){

        Topbar("Friends")

        Column(
            verticalArrangement = Arrangement.Top,
            modifier = Modifier
                .weight(1F)
        ){
            // Search Bar
            OutlinedTextField(
                value = searchText,
                onValueChange = {
                    searchText = it
                    isSearchActive = it.isNotEmpty()
                    perform_search()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    containerColor = colorResource(id = R.color.off_white),
                    focusedBorderColor = colorResource(id = R.color.blue),
                    unfocusedBorderColor = colorResource(id = R.color.teal)
                ),
                label = { Text("Search friends ...") },
                leadingIcon = {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.search),
                        contentDescription = "delete", tint = colorResource(id = R.color.black),
                        modifier = Modifier
                            .size(24.dp)

                    )
                },

            )

            LazyColumn {
                items(filteredFriends) { friend ->
                    Friends(friend)
                }
            }
        }

        Navbar(navController)
    }
}


@Composable
fun Friends(friend: Friend) {
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
                .padding(4.dp, 4.dp, 20.dp, 4.dp)
        ) {
            TextButton(
                modifier = Modifier
                    .width(50.dp),
                onClick = { TODO() }
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.default_profile),
                    contentDescription = "profile", tint = colorResource(id = R.color.black)
                )
            }

            Column(
                modifier = Modifier
                    .width(30.dp)
                    .weight(1F)
                    .align(Alignment.CenterVertically),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text("${friend.firstName} ${friend.lastName}")
            }

            TextButton(
                modifier = Modifier
                    .width(50.dp),
                onClick = {remove_friend(friend.firstName, friend.lastName)}
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.delete_user),
                    contentDescription = "delete", tint = colorResource(id = R.color.black)
                )
            }
        }
    }
}

fun remove_friend(firstName: String, lastName : String) {
    ogFriendsList.remove(Friend(firstName, lastName))
}

@Preview
@Composable
fun Preview_friends(){
    FriendsMainContent(rememberNavController())
}