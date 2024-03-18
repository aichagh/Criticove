package com.criticove

import android.content.ContentValues.TAG
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextButton
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.core.content.FileProvider
import com.criticove.backend.FirebaseManager

import com.criticove.backend.userModel

import com.google.firebase.Firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.auth.userProfileChangeRequest
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Objects
import kotlin.coroutines.CoroutineContext

val profilePic = R.drawable.default_pic // later if profile pic is set, change it

@Composable
fun ProfilePageMainContent(navController: NavController, userModel: userModel) {

    Column(
        modifier = Modifier
            .background(colorResource(id = R.color.off_white))
            .fillMaxHeight(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        ProfileHeader(navController, "Profile", "Dashboard")

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
fun ProfileHeader(navController: NavController, title: String, route: String) {
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
                text = title,
                color = colorResource(id = R.color.off_white),
                fontSize = 30.sp,
                fontFamily = FontFamily(Font(R.font.alegreya_sans_bold)),
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
            .fillMaxWidth(),
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
            fontSize = 24.sp,
            fontFamily = FontFamily(Font(R.font.alegreya_sans_bold)),
        )


        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            customButton("Edit Profile",  { navController.navigate("EditProfile") })
            customButton("Logout") {
                showDialog = true
            }
            customButton("Delete Account")
            {showDeleteDialog = true}
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
                customButton("Logout", {
                    showDialog = false
                    FirebaseAuth.getInstance().signOut()
                    navController.navigate("Login")})
            },
            dismissButton = {
                customButton("Cancel",{showDialog = false})
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
                customButton("Delete", {
                    showDeleteDialog = false
                    FirebaseAuth.getInstance().getCurrentUser()?.delete()
                    navController.navigate("Login")})
            },
            dismissButton = {
                customButton("Cancel",{showDeleteDialog = false})
            }
        )
    }
}


@Composable
fun customButton(text: String = "Default",
                 clicked: () -> Unit) {
    Button(
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
fun editProfile(navController: NavController, userModel: userModel) {
    Column(
        modifier = Modifier
            .background(colorResource(id = R.color.off_white))
            .fillMaxHeight(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        EditHeader()

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

@Composable
fun EditMain(navController: NavController) {
    val user = FirebaseManager.getUsername()
    var username by remember { mutableStateOf(user) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(colorResource(id = R.color.off_white)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {

        TextButton(
            onClick = { },
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

        OutlinedTextField(
            value = username,
            onValueChange = {
                username = it
            }
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
            customButton("Save changes", { changed(username) })
            customButton("Back", { navController.navigate("ProfilePage") })
        }

    }
}

fun changed(newUsername: String) {
    val user = Firebase.auth.currentUser
    val profileUpdates = userProfileChangeRequest {
        displayName = newUsername
//        photoUri = Uri.parse("https://example.com/jane-q-user/profile.jpg")
    }

    user!!.updateProfile(profileUpdates)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d(TAG, "User profile updated.")
            }
        }
}