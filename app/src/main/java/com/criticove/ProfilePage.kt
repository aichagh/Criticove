package com.criticove

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.navigation.NavController
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextOverflow

import com.criticove.backend.FirebaseManager
import com.criticove.backend.userModel

val profilePic = R.drawable.default_pic // later if profile pic is set, change it

@Composable
fun ProfilePageMainContent(navController: NavController, userModel: userModel) {

    Column(
        modifier = Modifier
            .background(colorResource(id = R.color.off_white))
            .fillMaxHeight(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        CommonHeader(navController, "Profile", "Dashboard")

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1F)
                .verticalScroll(rememberScrollState())
                .background(colorResource(id = R.color.off_white))
        ) {
            ProfileMain(navController)
        }
    }
}

@Composable
fun CommonHeader(navController: NavController, title: String, route: String) {
    Box(
        modifier = Modifier
            .height(50.dp)
            .background(colorResource(id = R.color.blue))
            .fillMaxWidth()
        ) {
        TextButton(onClick = { navController.navigate(route) }) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.right_arrow),
                contentDescription = "backarrow", tint = colorResource(id = R.color.off_white),
                modifier = Modifier.scale(scaleX = -1f, scaleY = 1f)
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            Alignment.Center
        ) {
            Text(
                modifier = Modifier.padding(horizontal = 40.dp),
                text = title,
                color = colorResource(id = R.color.off_white),
                fontSize = 30.sp,
                fontFamily = FontFamily(Font(R.font.alegreya_sans_bold)),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Composable
//@Preview
fun ProfileMain(navController: NavController) {
    val username = FirebaseManager.getUsername()
    val profilePic = R.drawable.default_pic // later if profile pic is set, change it
    var showDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        //Temporary placeholder
        Box(
            modifier = Modifier
                .padding(10.dp)
        ) {
            Image(
                painter = painterResource(id = profilePic),
                contentDescription = "profile picture",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(200.dp)
                    .clip(CircleShape)
            )
        }

        Text(
            text = username,
            Modifier.padding(
                horizontal = 15.dp
            ),
            fontSize = 24.sp,
            fontFamily = FontFamily(Font(R.font.alegreya_sans_bold)),
        )


        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            CustomButton("Edit Profile") { navController.navigate("EditProfile") }
            CustomButton("Logout") { showDialog = true }
            CustomButton("Delete Account") { showDeleteDialog = true }
        }
    }
    // Dialog
    if (showDialog) {
        AlertDialog(
            onDismissRequest = {
                showDialog = false
            },
            title = {
                Text(text = "Logout", fontSize = 24.sp, fontFamily = FontFamily(Font(R.font.alegreya_sans_bold)))
            },
            text = {
                Text(text = "Are you sure?", fontSize = 20.sp, fontFamily = FontFamily(Font(R.font.alegreya_sans_regular)))
            },
            confirmButton = {
                CustomButton("Logout") {
                    showDialog = false
                    FirebaseManager.signOut()
                    navController.navigate("Login")
                }
            },
            dismissButton = {
                CustomButton("Cancel") { showDialog = false }
            },
        )
    }
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = {
                showDeleteDialog = false
            },
            title = {
                Text(text = "Delete Account", fontSize = 24.sp, fontFamily = FontFamily(Font(R.font.alegreya_sans_bold)))
            },
            text = {
                Text("Are you sure?", fontSize = 20.sp, fontFamily = FontFamily(Font(R.font.alegreya_sans_regular)))
            },
            confirmButton = {
                CustomButton("Delete") {
                    showDeleteDialog = false
                    FirebaseManager.deleteAccount()
                    navController.navigate("Signup")
                }
            },
            dismissButton = {
                CustomButton("Cancel") { showDeleteDialog = false }
            }
        )
    }
}


@Composable
fun CustomButton(text: String = "Default",
                 isBtnEnabled: Boolean = true,
                 clicked: () -> Unit) {
    Button(
        enabled = isBtnEnabled,
        onClick = clicked,
        colors = ButtonDefaults.buttonColors(
            containerColor = colorResource(id = R.color.teal),
            contentColor = colorResource(id = R.color.off_white)
        ),
    ) { Text(
        text = text,
        fontFamily = FontFamily(Font(R.font.alegreya_sans_bold)),
        fontSize = 20.sp
    ) }
}

@Composable
fun EditProfile(navController: NavController, userModel: userModel) {
    Column(
        modifier = Modifier
            .background(colorResource(id = R.color.off_white))
            .fillMaxHeight(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        CommonHeader(navController, "Edit Profile", "ProfilePage")

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1F)
                .verticalScroll(rememberScrollState())
                .background(colorResource(id = R.color.off_white))
        ) {
            EditMain(navController)
        }
    }
}

@Composable
fun EditHeader() {
    Column(
        modifier = Modifier
            .height(50.dp)
            .background(colorResource(id = R.color.blue))
            .fillMaxWidth()
            .fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            Alignment.Center
        ) {
            Text(
                text = "Edit Profile",
                color = colorResource(id = R.color.off_white),
                fontSize = 30.sp,
                fontFamily = FontFamily(Font(R.font.alegreya_sans_bold))
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditMain(navController: NavController) {
    val user = FirebaseManager.getUsername()
    var username by remember { mutableStateOf(user) }

    var statusMessage by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(colorResource(id = R.color.off_white)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {

        TextButton(
            modifier = Modifier.padding(top = 20.dp),
            onClick = { }
        ) {
            Image(
                painter = painterResource(id = profilePic),
                contentDescription = "profile picture",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(200.dp)
                    .clip(CircleShape)
            )
        }
        var textColor = if (statusMessage == "Username updated.") colorResource(id = R.color.darkgreen) else colorResource(id = R.color.red)
        Text(
            text = statusMessage,
            fontFamily = FontFamily(Font(R.font.alegreya_sans_bold)),
            color = textColor,
            fontSize = 18.sp
        )

        OutlinedTextField(
            value = username,
            onValueChange = {
                username = it
                statusMessage = ""
            },
            modifier = Modifier
                .width(300.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = colorResource(id = R.color.blue),
                unfocusedBorderColor = colorResource(id = R.color.teal)
            ),
            textStyle = TextStyle(
                fontFamily = FontFamily(Font(R.font.alegreya_sans_regular)),
                fontSize = 18.sp
            )
        )

        Row(
            modifier = Modifier.padding(top = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
            CustomButton("Save changes") {
                if (username != "") {
                    FirebaseManager.updateUsername(username) { success ->
                        statusMessage = if (success && username != "") {
                            "Username updated."
                        } else {
                            "Username update failed. Try again."
                        }
                    }
                } else {
                    statusMessage = "Username cannot be empty"
                }
            }
//            CustomButton("Back") { navController.navigate("ProfilePage") }
        }

    }
}
