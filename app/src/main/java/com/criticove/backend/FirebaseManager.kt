package com.criticove.backend

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.auth.userProfileChangeRequest

object FirebaseManager {
    private val auth = FirebaseAuth.getInstance()

    fun updateUsername(username: String, callback: (Boolean) -> Unit) {
        val user = auth.currentUser!!
        val profileUpdate = userProfileChangeRequest {
            displayName = username
        }
        user.updateProfile(profileUpdate)
            .addOnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.e("FirebaseManager", "Profile update failed", task.exception)
                    callback(false)
                }
            }
        callback(true)
    }

    fun signup(username: String, email: String, password: String, callback: (Boolean) -> Unit) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    updateUsername(username) { success ->
                        callback(success)
                    }
                } else {
                    Log.e("FirebaseManager", "Signup failed", task.exception)
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

    fun signOut() {
        auth.signOut()
    }

    fun deleteAccount() {
        auth.currentUser?.delete()
    }

    fun getUsername(): String {
        val username = Firebase.auth.currentUser?.displayName
        return if (username.isNullOrEmpty()) {
            ""
        } else {
            username
        }
    }
}
