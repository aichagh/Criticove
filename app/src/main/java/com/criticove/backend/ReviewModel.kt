package com.criticove.backend

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

open class Review(val type: String, val title: String, val date: String, val genre: String, val rating: Int, val paragraph: String) {
}

class BookReview(type: String, title:String, date:String, genre: String, rating: Int, paragraph: String,
                 val author: String, val booktype: String): Review(type, title, date, genre, rating, paragraph) {
}
class TVShowReview(type: String, title:String, date:String, genre: String, rating: Int, paragraph: String,
                 val director: String, val streamingservice: String): Review(type, title, date, genre, rating, paragraph) {
}

class MovieReview(type: String, title:String, date:String, genre: String, rating: Int, paragraph: String,
                   val director: String, val publicationcompany: String): Review(type, title, date, genre, rating, paragraph) {
}

fun SubmittedReview(type: String, rating: Int, review: MutableMap<String, String>) {
    val user = Firebase.auth.currentUser
    lateinit var userID : String
    if (user != null) {
        userID = user.uid
        println("the user id is $userID")
    }
    var reviewsRef = FirebaseDatabase.getInstance().getReference("Users/$userID/Reviews")
    lateinit var reviewPost: Review
    when (type) {
        "Book" -> {
            reviewPost = BookReview("Book", review["Book Title"].toString(), review["Date Published"].toString(),
                review["Genre"].toString(), rating, review["Review"].toString(),
                review["Author"].toString(), review["Book Type"].toString())
        }
        "TV Show" -> {
            reviewPost = TVShowReview("TV Show", review["TV Show Title"].toString(), review["Date Released"].toString(),
            review["Genre"].toString(), rating, review["Review"].toString(),
            review["Director"].toString(), review["Streaming Service"].toString())
    }
        "Movie" -> {
            reviewPost = MovieReview("Movie", review["Movie Title"].toString(), review["Date Released"].toString(),
                review["Genre"].toString(), rating, review["Review"].toString(),
                review["Director"].toString(), review["Publication Company"].toString())
        }
    }
    var newReview = reviewsRef.push()
    newReview.setValue(reviewPost)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
            } else {
                Log.e("", "Post Failed", task.exception)
            }
        }
}

fun delSelectedReview(reviewID: String) {
    val user = Firebase.auth.currentUser
    lateinit var userID : String
    if (user != null) {
        userID = user.uid
        println("the user id is $userID, and reviewId is $reviewID")
    }

    var reviewsRef = FirebaseDatabase.getInstance().getReference("Users/$userID/Reviews/$reviewID")
    reviewsRef.removeValue()

}

fun getSelectedReview(reviewID: String): MutableMap<String, String> {
    val user = Firebase.auth.currentUser
    lateinit var userID : String
    if (user != null) {
        userID = user.uid
        println("the user id is $userID, and reviewId is $reviewID")
    }

    var reviewsRef = FirebaseDatabase.getInstance().getReference("Users/$userID/Reviews")

    reviewsRef.child(reviewID).get().addOnSuccessListener {
        if (it.exists()) {
            println("Here")

            println(it.child("title").value)
            println(it.child("type").value)
        } else {
            println("Don't exist")
        }
        println("It is in")

    }.addOnFailureListener {
        println("Unsuccessful")
    }

    var message = ""

    var reviewData = mutableMapOf("Title" to message)
    return reviewData
}
