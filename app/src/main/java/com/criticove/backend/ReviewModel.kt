package com.criticove.backend

import android.content.ContentValues.TAG
import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
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


fun getUserReviews (userID: String): List<Review> {
    var reviewsRef = FirebaseDatabase.getInstance().getReference("Users/$userID/Reviews")
    println("the users $userID reviews keys and their corresponding values: ,")
    var reviewList =  mutableListOf<Review>()
    reviewsRef.addValueEventListener(object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            for (reviewSnapshot in dataSnapshot.children) {
                val reviewKey = reviewSnapshot.key
                val review = reviewSnapshot.value as Map<String, Any>
                lateinit var reviewPost: Review
                when (review["type"]) {
                   "Book" -> {
                    reviewPost = BookReview("Book", review["title"].toString(), review["date"].toString(),
                    review["genre"].toString(), 3, review["paragraph"].toString(),
                     review["author"].toString(), review["booktype"].toString()) }
                    "Movie" -> { reviewPost = MovieReview("Movie", review["title"].toString(), review["date"].toString(),
                        review["genre"].toString(), 3, review["paragraph"].toString(),
                        review["director"].toString(), review["publicationcompany"].toString()) }
                    "TV Show" -> { reviewPost = TVShowReview("Book", review["title"].toString(), review["date"].toString(),
                        review["genre"].toString(), 3, review["paragraph"].toString(),
                        review["director"].toString(), review["streamingservice"].toString()) }
                }
                reviewList.add(reviewPost)
                //println("this is the review back to a structure $reviewPost")
                //println("this is the reviews title ${reviewPost.title}")
                //println("this is the reviews date ${reviewPost.date}")
                //println("this is the reviews par ${reviewPost.paragraph}")
                // TODO: handle the post
            }
        }

        override fun onCancelled(error: DatabaseError) {
           // Log.w(TAG, "review:onCancelled", databaseError.toException())
        }

    })
    return reviewList
}