package com.criticove

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.criticove.backend.FirebaseManager
import com.criticove.backend.userModel
import com.google.firebase.database.FirebaseDatabase

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReviewGoalMainContent(navController: NavController, userModel: userModel) {
    var currentGoal by remember { mutableStateOf<Int?>(null) }
    checkReviewGoal { retrievedGoal ->
        currentGoal = retrievedGoal
    }

    var newGoal by remember { mutableStateOf("") }
    var goalInfoText = "No goal set yet."
    var btnText = "Set Goal"
    if (currentGoal != null) {
        goalInfoText = "Current Goal: $currentGoal"
        btnText = "Update Goal"
    }

    var isBtnEnabled by remember { mutableStateOf(false) }
    fun checkBtnEnabled() {
        isBtnEnabled = newGoal.isNotBlank()
                && newGoal.matches("^[0-9]+$".toRegex())
                && !newGoal.matches("^0+$".toRegex())
    }

    var statusMessage by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .background(colorResource(id = R.color.off_white))
            .fillMaxHeight(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        CommonHeader(navController, "Review Goal", "Dashboard")

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1F)
                .verticalScroll(rememberScrollState())
                .background(colorResource(id = R.color.off_white)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier.padding(top = 40.dp),
                text = goalInfoText,
                fontFamily = FontFamily(Font(R.font.alegreya_sans_bold)),
                color = colorResource(id = R.color.blue),
                fontSize = 24.sp
            )

            Text(
                modifier = Modifier.padding(top = 20.dp, bottom = 5.dp),
                text = statusMessage,
                fontFamily = FontFamily(Font(R.font.alegreya_sans_bold)),
                color = colorResource(id = R.color.darkgreen),
                fontSize = 18.sp
            )

            OutlinedTextField(
                value = newGoal,
                onValueChange = {
                    newGoal = it
                    checkBtnEnabled()
                    statusMessage = ""
                },
                modifier = Modifier
                    .width(150.dp)
                    .padding(10.dp),
                singleLine = true,
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    containerColor = colorResource(id = R.color.off_white),
                    focusedBorderColor = colorResource(id = R.color.blue),
                    unfocusedBorderColor = colorResource(id = R.color.teal)
                ),
                textStyle = TextStyle(
                    fontSize = 20.sp,
                    fontFamily = FontFamily(Font(R.font.alegreya_sans_regular)),
                    textAlign = TextAlign.Center
                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            Row(
                modifier = Modifier.padding(top = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                CustomButton(btnText, isBtnEnabled) {
                    val database = FirebaseDatabase.getInstance()
                    val userID = FirebaseManager.getUser()?.uid
                    val userRef = database.getReference("Users/$userID/reviewGoal")
                    userRef.setValue(newGoal.trim().toInt())
                        .addOnSuccessListener {
                            statusMessage = "Review goal updated."
                        }
                        .addOnFailureListener {
                            statusMessage = "Review goal update failed. Try again."
                        }
                }

                CustomButton("Back") { navController.navigate("Dashboard") }
            }
        }
    }
}
