package com.criticove.backend

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


class userModel: ViewModel {
    private val _userID: MutableStateFlow<String> = MutableStateFlow("ZFZrCVjIR0P76TqT5lxX0W3dUI93")
    val userID: StateFlow<String> = _userID
    private val _reviewList: MutableStateFlow<MutableList<Review>> = MutableStateFlow(mutableListOf())
    val reviewList: StateFlow<MutableList<Review>> = _reviewList

    fun getReviews() {
        println("this is the user id : ${_userID.value}")
            var reviewsRef = FirebaseDatabase.getInstance().getReference("Users/${_userID.value}/Reviews")
            println("the users ${this.userID} reviews keys and their corresponding values: ,")
            reviewsRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    var newReviewList: MutableList<Review> = mutableListOf()
                    for (reviewSnapshot in dataSnapshot.children) {
                        val reviewKey = reviewSnapshot.key
                        val review = reviewSnapshot.value as Map<String, Any>
                        lateinit var reviewPost: Review
                        when (review["type"]) {
                            "Book" -> {
                                reviewPost = BookReview(
                                    "Book", review["title"].toString(), review["date"].toString(),
                                    review["genre"].toString(), 3, review["paragraph"].toString(),
                                    review["author"].toString(), review["booktype"].toString()
                                )
                            }

                            "Movie" -> {
                                reviewPost = MovieReview(
                                    "Movie",
                                    review["title"].toString(),
                                    review["date"].toString(),
                                    review["genre"].toString(),
                                    3,
                                    review["paragraph"].toString(),
                                    review["director"].toString(),
                                    review["publicationcompany"].toString()
                                )
                            }

                            "TV Show" -> {
                                reviewPost = TVShowReview(
                                    "Book", review["title"].toString(), review["date"].toString(),
                                    review["genre"].toString(), 3, review["paragraph"].toString(),
                                    review["director"].toString(), review["streamingservice"].toString()
                                )
                            }
                        }
                        newReviewList.add(reviewPost)
                        _reviewList.update{newReviewList}
                        println("reviewList in the event handelr $reviewList")
                        println("this is the review back to a structure $reviewPost")
                        println("this is the reviews title ${reviewPost.title}")
                        println("this is the reviews date ${reviewPost.date}")
                        println("this is the reviews par ${reviewPost.paragraph}")
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Log.w(TAG, "review:onCancelled", databaseError.toException())
                }

            })

        }
    //var friends: List<String>
    //UNCOMMENT THIS TO USE LOGGED IN USER RATHER THAN DEFAULT TEST
    constructor() {
        val user = Firebase.auth.currentUser
//        if (user != null) {
//            this.userID = user.uid
//            println("the user id is $userID")
//        }

    }
}
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
    userID = "ZFZrCVjIR0P76TqT5lxX0W3dUI93"
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
    println("this si the review i just shared${reviewPost.title}")
    newReview.setValue(reviewPost)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                println("shared review")
            } else {
                Log.e("", "Post Failed", task.exception)
            }
        }
}