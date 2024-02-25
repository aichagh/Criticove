package com.criticove

import android.app.Activity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class Signup : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        val auth: FirebaseAuth = Firebase.auth
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        val btn = findViewById<Button>(R.id.signup)
        btn.setOnClickListener{
            val email = findViewById<EditText>(R.id.email).text.toString()
            val password = findViewById<EditText>(R.id.password).text.toString()
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
//                        Log.d(TAG, "createUserWithEmail:success")
                        val user = auth.currentUser
//                        updateUI(user)
                    } else {
                        // If sign in fails, display a message to the user.
//                        Log.w(TAG, "createUserWithEmail:failure", task.exception)
//                        Toast.makeText(
//                            baseContext,
//                            "Authentication failed.",
//                            Toast.LENGTH_SHORT,
//                        ).show()
//                        updateUI(null)
                    }
                }
        }
    }
}