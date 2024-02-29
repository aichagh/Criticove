package com.criticove.backend

import android.util.Log
import com.google.firebase.auth.FirebaseAuth

object FirebaseManager {
    private val auth = FirebaseAuth.getInstance()

    fun signup(email: String, password: String, callback: (Boolean) -> Unit) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    callback(true)
                } else {
                    Log.e("FirebaseManager", "Sign up failed", task.exception)
                    callback(false)
                }
            }
    }
}
