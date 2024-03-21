package com.criticove

import FriendsReviews
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.ui.Modifier
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

import com.criticove.backend.FirebaseManager
import com.criticove.backend.userModel

class Signup : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_CritiCove)
        val userModel = userModel()
        setContent {
            val navController = rememberNavController()
            NavHost(
                navController,
                startDestination = "Signup",

                // removes the default crossfade animation when changing between pages
                enterTransition = { EnterTransition.None },
                exitTransition = { ExitTransition.None }
            ) {
                composable("Signup") {
                    SignupMainContent(navController, userModel)
                }
                composable("Login") {
                    LoginMainContent(navController, userModel)
                }
                composable("ReviewForm") {
                    ReviewFormMainContent(navController, userModel)
                }
                composable("Reviews") {
                    ReviewPageMainContent(navController, userModel)
                }
                composable("Dashboard") {
                    DashboardMainContent(navController, userModel)
                }
                composable("Friends") {
                    FriendsMainContent(navController, userModel)
                }
                composable("ViewReview/{reviewID}/{isFriend}") {
                    val reviewID = it.arguments?.getString("reviewID")!!
                    val isFriendStr = it.arguments?.getString("isFriend")!!
                    var isFriend = false
                    if (isFriendStr == "true") {
                        isFriend = true
                    }

                    println("In composable: " + reviewID)

                    ReviewDetailsMainContent(navController, reviewID, isFriend, userModel)
                }
                composable("AddFriends") {
                    AddFriends(navController, userModel)
                }
                composable("FriendsReviews") {
                    FriendsReviews(navController, userModel)
                }
                composable("Books") {
                    FilterReviews(navController, userModel, "Book")
                }
                composable("Movies") {
                    FilterReviews(navController, userModel, "Movie")
                }
                composable("TVShows") {
                    FilterReviews(navController, userModel, "TV Show")
                }
                composable("ProfilePage") {
                    ProfilePageMainContent(navController, userModel)
                }
                composable("EditProfile") {
                    EditProfile(navController, userModel)
                }

            }
        }
    }
}

@Composable
fun SignupMainContent(navController: NavController, userModel: userModel) {
    val scope = rememberCoroutineScope()

    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    var isSignupEnabled by remember { mutableStateOf(false) }
    fun checkSignupEnabled() {
        isSignupEnabled = username.isNotBlank() && email.isNotBlank()
                && password.isNotBlank()
    }

    var errorMessage by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.blue)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .padding(bottom = 50.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = null,
                modifier = Modifier
                    .size(150.dp)
                    .padding(bottom = 10.dp)
            )

            Text(
                text = stringResource(id = R.string.app_name),
                fontFamily = FontFamily(Font(R.font.righteous_regular)),
                color = colorResource(id = R.color.yellow),
                fontSize = 40.sp
            )
        }

        Column(
            modifier = Modifier
                .width(300.dp)
                .background(colorResource(id = R.color.green), shape = RoundedCornerShape(10.dp)),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier.padding(top = 10.dp),
                text = errorMessage,
                fontFamily = FontFamily(Font(R.font.alegreya_sans_bold)),
                color = colorResource(id = R.color.red),
                fontSize = 18.sp
            )

            OutlinedTextFieldSignup("Username") {
                username = it
                checkSignupEnabled()
            }

            OutlinedTextFieldSignup("Email") {
                email = it
                checkSignupEnabled()
            }

            OutlinedTextFieldSignup("Password", true) {
                password = it
                checkSignupEnabled()
            }

            Button(
                onClick = {
                    FirebaseManager.signup(username, email, password) { success ->
                        if (success) {
                            userModel.signupUser(username)
                            userModel.getCurUser()
                            errorMessage = ""
                            scope.launch {
                                // Allow 0.5s delay for username update in Firebase so fetching the
                                // username on the Dashboard doesn't return null
                                delay(500L)
                                navController.navigate("Dashboard")
                            }
                        } else {
                            errorMessage = "Sign up failed. Try again."
                        }
                    }
                },
                enabled = isSignupEnabled,
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(id = R.color.teal),
                    contentColor = colorResource(id = R.color.yellow)
                ),
                modifier = Modifier
                    .width(150.dp)
                    .padding(top = 20.dp, bottom = 30.dp),
            ) {
                Text(
                    text = stringResource(id = R.string.signup),
                    fontFamily = FontFamily(Font(R.font.alegreya_sans_bold)),
                    color = colorResource(id = R.color.off_white),
                    fontSize = 20.sp
                )
            }
        }

        Row(
            modifier = Modifier.padding(50.dp)
        ) {
            Text(
                modifier = Modifier
                    .padding(end = 5.dp),
                text = stringResource(id = R.string.already_have_account),
                fontFamily = FontFamily(Font(R.font.alegreya_sans_regular)),
                color = colorResource(id = R.color.yellow),
                fontSize = 20.sp
            )
            Text(
                modifier = Modifier.clickable { navController.navigate("Login") },
                text = stringResource(id = R.string.login),
                style = TextStyle(textDecoration = TextDecoration.Underline),
                fontFamily = FontFamily(Font(R.font.alegreya_sans_bold)),
                color = colorResource(id = R.color.yellow),
                fontSize = 20.sp
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OutlinedTextFieldSignup(
    placeholder: String,
    isPassword: Boolean = false,
    onValueChanged: (String) -> Unit
) {
    var text by remember { mutableStateOf("") }

    OutlinedTextField(
        value = text,
        onValueChange = {
            text = it
            onValueChanged(it)
        },
        modifier = Modifier
            .width(280.dp)
            .padding(10.dp)
            .background(colorResource(id = R.color.green), shape = RoundedCornerShape(10.dp)),
        singleLine = true,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            containerColor = colorResource(id = R.color.off_white),
            focusedBorderColor = colorResource(id = R.color.blue),
            unfocusedBorderColor = colorResource(id = R.color.teal)
        ),
        textStyle = TextStyle(fontSize = 20.sp, fontFamily = FontFamily(Font(R.font.alegreya_sans_regular))),
        placeholder = {
            Text(
                text = placeholder,
                style = TextStyle(
                    fontSize = 20.sp,
                    fontFamily = FontFamily(Font(R.font.alegreya_sans_regular))
                )
            )
        },
        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
        keyboardOptions = if (isPassword) KeyboardOptions(keyboardType = KeyboardType.Password) else KeyboardOptions.Default,
    )
}