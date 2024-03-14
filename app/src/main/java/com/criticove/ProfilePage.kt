package com.criticove

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonColors
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.criticove.backend.SubmittedReview

import com.criticove.m3.ButtonStyles.PrimaryButton
import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.TextButton
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import com.criticove.backend.FirebaseManager
import java.util.Date

@Composable
fun ProfilePageMainContent(navController: NavController) {

    Column(
        modifier = Modifier
            .background(colorResource(id = R.color.off_white))
            .fillMaxHeight(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        ProfileHeader(navController)

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1F)
                .verticalScroll(rememberScrollState())
                .background(colorResource(id = R.color.off_white))
        ) {
            ProfileMain()
        }
        Navbar(navController)
    }

}

@Composable
fun ProfileHeader(navController: NavController) {
    Box(
        modifier = Modifier
            .height(50.dp)
            .background(colorResource(id = R.color.blue))
            .fillMaxWidth()
        ) {
        TextButton(onClick = { navController.navigate("Dashboard") }) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.right_arrow),
                contentDescription = "profile", tint = colorResource(id = R.color.off_white)
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            Alignment.Center
        ) {
            Text(
                text = "Profile",
                color = colorResource(id = R.color.off_white),
                fontSize = 30.sp,
                fontFamily = FontFamily(Font(R.font.alegreya_sans_bold)),
            )
        }
    }
}

@Composable
//@Preview
fun ProfileMain() {
    val username = FirebaseManager.getUsername()
//    var signupDate = username.getCreationTimestamp()


    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        //Temporary placeholder
        Icon(
            imageVector = ImageVector.vectorResource(id = R.drawable.default_profile),
            contentDescription = "profile", tint = colorResource(id = R.color.blue),
            modifier = Modifier
                .height(75.dp)
                .padding(10.dp)
        )

        Text(
           text = "$username",
            fontSize = 20.sp,
            fontFamily = FontFamily(Font(R.font.alegreya_sans_medium)),
        )

//        Text(
//            text = "Joined: mm/dd/yyyy", // temporary to view preview
//            fontSize = 18.sp,
//            fontFamily = FontFamily(Font(R.font.alegreya_sans_medium)),
//        )

        Button(
            onClick = {TODO()},
            colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(id = R.color.teal),
                contentColor = colorResource(id = R.color.off_white)
            ),
            modifier = Modifier
                .padding(10.dp)
        ) { Text(
            text = "Edit profile",
            fontFamily = FontFamily(Font(R.font.alegreya_sans_bold)),
            fontSize = 20.sp
        ) }

        Button(
            onClick = {TODO()},
            colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(id = R.color.teal),
                contentColor = colorResource(id = R.color.off_white)
            ),
            modifier = Modifier
                .padding(10.dp)
        ) { Text(
            text = "Logout",
            fontFamily = FontFamily(Font(R.font.alegreya_sans_bold)),
            fontSize = 20.sp
        ) }

        Button(
            onClick = {TODO()},
            colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(id = R.color.teal),
                contentColor = colorResource(id = R.color.off_white)
            ),
            modifier = Modifier
                .padding(10.dp)
        ) { Text(
            text = "Delete account",
            fontFamily = FontFamily(Font(R.font.alegreya_sans_bold)),
            fontSize = 20.sp
        ) }
    }
}