package com.criticove

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.criticove.backend.userModel

data class Friend(val username: String)

@SuppressLint("UnrememberedMutableState")
@Composable
fun createFriendsList(usermodel: userModel): SnapshotStateList<Friend> {
    usermodel.getFriends()
    var curFriendsList = mutableStateListOf<Friend>()
    val friend_map by usermodel.friendMap.collectAsState()
    for ((key, value) in friend_map) {
        curFriendsList.add(Friend(value))
    }
    return curFriendsList
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FriendsMainContent(navController: NavController, usermodel: userModel) {
    var searchText by remember { mutableStateOf("") }
    var isSearchActive by remember { mutableStateOf(false) }
    var filteredFriends by remember { mutableStateOf(emptyList<Friend>()) }
    var ogcurFriendsList = createFriendsList(usermodel)
    println("the friend from the backend are $ogcurFriendsList")
    fun perform_search() {
        filteredFriends = if (isSearchActive) {
            ogcurFriendsList.filter {
                it.username.contains(searchText, ignoreCase = true)
            }
        } else {
            ogcurFriendsList
        }
    }

    perform_search()

    MainLayout(
        title = "Manage Friends",
        navController = navController,
        friends = true
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

            Column(
                verticalArrangement = Arrangement.Top,
                modifier = Modifier
                    .weight(1F)
            ) {
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
                    label = {
                        Text(
                            text = "Search friends ...",
                            style = TextStyle(
                                fontSize = 20.sp,
                                fontFamily = FontFamily(Font(R.font.alegreya_sans_regular))
                            )
                        )
                    },
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
                Text(text = "${friend.username}",
                    style = TextStyle(
                        fontSize = 20.sp,
                        fontFamily = FontFamily(Font(R.font.alegreya_sans_regular))
                    )
                )

            }

            TextButton(
                modifier = Modifier
                    .width(50.dp),
                onClick = {
//                    ognewFriendsList.add(Friend(friend.username))
                    remove_friend(friend.username)}
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.delete_user),
                    contentDescription = "delete", tint = colorResource(id = R.color.black)
                )
            }
        }
    }
}

fun remove_friend(username: String) {
//    ogFriendsList.remove(Friend(username))
}

//@Preview
//@Composable
//fun Preview_friends(){
//    FriendsMainContent(rememberNavController())
//}
