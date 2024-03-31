package com.criticove

import android.annotation.SuppressLint
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.criticove.backend.userModel

@SuppressLint("UnrememberedMutableState")
@Composable
fun createNewFriendsList(usermodel: userModel): SnapshotStateList<Friend> {
    usermodel.getUsers()
    var friendsList = mutableStateListOf<Friend>()
    val userMap by usermodel.userMap.collectAsState()
    for ((_, value) in userMap) {
        friendsList.add(Friend(value))
    }
    return friendsList
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddFriends(navController: NavController, usermodel: userModel) {
    var searchText by remember { mutableStateOf("") }
    var isSearchActive by remember { mutableStateOf(false) }
    var filteredFriends by remember { mutableStateOf(emptyList<Friend>()) }
    val ognewFriendsList = createNewFriendsList(usermodel)

    fun performSearch() {
        filteredFriends = if (isSearchActive) {
            ognewFriendsList.filter {
                it.username.contains(searchText, ignoreCase = true)
            }
        } else {
            ognewFriendsList
        }
    }

    performSearch()
    MainLayout(
        title = "Add Friends",
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
                        performSearch()
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
                            color = colorResource(id = R.color.blue),
                            style = TextStyle(
                                fontSize = 20.sp,
                                fontFamily = FontFamily(Font(R.font.alegreya_sans_regular))
                            )
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.search),
                            contentDescription = "search", tint = colorResource(id = R.color.black),
                            modifier = Modifier
                                .size(24.dp)

                        )
                    },

                    )
                if (searchText.isNotEmpty()) {
                    LazyColumn {
                        items(filteredFriends) { friend ->
                            AddFriends(friend, ognewFriendsList, usermodel)
                        }
                    }
                }
            }

            Navbar(navController)
        }
    }
}

@Composable
fun AddFriends(friend: Friend,
               ognewFriendsList: SnapshotStateList<Friend>,
               usermodel: userModel) {
    var friendAdded by remember { mutableStateOf(false) }

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
                .padding(4.dp, 4.dp, 20.dp, 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(
                modifier = Modifier
                    .width(50.dp),
                onClick = {
                    /* Nothing */
                }
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
                Text(text = friend.username,
                    style = TextStyle(
                        fontSize = 20.sp,
                        fontFamily = FontFamily(Font(R.font.alegreya_sans_regular))
                    )
                )
            }

            if (friendAdded) {
                Icon(
                    modifier = Modifier.padding(end = 15.dp),
                    imageVector = Icons.Filled.Check,
                    contentDescription = "check", tint = colorResource(id = R.color.black)
                )
            } else {
                TextButton(
                    modifier = Modifier.width(50.dp),
                    onClick = {
                        usermodel.addFriend(friend.username)
                        ognewFriendsList.remove(Friend(friend.username))
                        friendAdded = true
                    }
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.add_friend_svgrepo_com),
                        contentDescription = "add", tint = colorResource(id = R.color.black)
                    )
                }
            }
        }
    }
}
