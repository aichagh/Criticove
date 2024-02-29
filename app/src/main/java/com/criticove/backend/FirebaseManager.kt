package com.criticove.backend

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.userProfileChangeRequest

object FirebaseManager {
    private val auth = FirebaseAuth.getInstance()

    fun signup(username: String, email: String, password: String, callback: (Boolean) -> Unit) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task1 ->
                if (task1.isSuccessful) {
                    val user = auth.currentUser!!
                    val profileUpdate = userProfileChangeRequest {
                        displayName = username
                    }
                    user.updateProfile(profileUpdate)
                        .addOnCompleteListener { task2 ->
                            if (!task2.isSuccessful) {
                                Log.e("FirebaseManager", "Profile update failed", task2.exception)
                                callback(false)
                            }
                        }
                    callback(true)
                } else {
                    Log.e("FirebaseManager", "Signup failed", task1.exception)
                    callback(false)
                }
            }
    }

    fun login(email: String, password: String, callback: (Boolean) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    callback(true)
                } else {
                    Log.e("FirebaseManager", "Login failed", task.exception)
                    callback(false)
                }
            }
    }
}
