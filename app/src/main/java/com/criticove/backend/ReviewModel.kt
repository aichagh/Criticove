package com.criticove.backend

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


class userModel: ViewModel {
    //private val _userID: MutableStateFlow<String> = MutableStateFlow("ZFZrCVjIR0P76TqT5lxX0W3dUI93")
    var userID: String = "ZFZrCVjIR0P76TqT5lxX0W3dUI93"
    private val _reviewList: MutableStateFlow<MutableList<Review>> = MutableStateFlow(mutableListOf())
    val reviewList: StateFlow<MutableList<Review>> = _reviewList

    fun getReviews() {
        println("this is the user id : ${userID}")
            var reviewsRef = FirebaseDatabase.getInstance().getReference("Users/${userID}/Reviews")
            println("the users ${this.userID} reviews keys and their corresponding values: ,")
            reviewsRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    var newReviewList: MutableList<Review> = mutableListOf()
                    for (reviewSnapshot in dataSnapshot.children) {
                        val reviewKey = reviewSnapshot.key
                        val review = reviewSnapshot.value as Map<String, Any>
                        lateinit var reviewPost: Review
                        val rDB = review["rating"]
                        val r = when (rDB) {
                            is Int -> rDB
                            is Long -> rDB.toInt()
                            else -> 3
                        }
                        println("this is rint $r")

                        when (review["type"]) {
                            "Book" -> {
                                reviewPost = BookReview(
                                    "Book", review["title"].toString(), review["date"].toString(),
                                    review["genre"].toString(), r , review["paragraph"].toString(),
                                    review["author"].toString(), review["booktype"].toString()
                                )
                            }

                            "Movie" -> {
                                reviewPost = MovieReview(
                                    "Movie",
                                    review["title"].toString(),
                                    review["date"].toString(),
                                    review["genre"].toString(),
                                    r,
                                    review["paragraph"].toString(),
                                    review["director"].toString(),
                                    review["publicationcompany"].toString()
                                )
                            }

                            "TV Show" -> {
                                reviewPost = TVShowReview(
                                    "Book", review["title"].toString(), review["date"].toString(),
                                    review["genre"].toString(), r, review["paragraph"].toString(),
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
    constructor() {
        val user = Firebase.auth.currentUser
        if (user != null) {
          this.userID = user.uid
          println("in the constructor user id is $userID")
       }

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
    //userID = "ZFZrCVjIR0P76TqT5lxX0W3dUI93"
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

fun delSelectedReview(reviewID: String) {
    val user = Firebase.auth.currentUser
    lateinit var userID : String
    if (user != null) {
        userID = user.uid
        println("the user id is $userID, and reviewId is $reviewID")
    }

    var reviewsRef = FirebaseDatabase.getInstance().getReference("Users/$userID/Reviews")
    reviewsRef.child(reviewID).removeValue()

}

fun getSelectedReview(reviewID: String): MutableMap<String, String> {
    val user = Firebase.auth.currentUser
    lateinit var userID : String
    if (user != null) {
        userID = user.uid
        println("the user id is $userID, and reviewId is $reviewID")
    }
    // sample data
    var reviewData = mutableMapOf("Title" to "The Night Circus", "Author" to "Erin Morgenstern",
        "Date Published" to "01/01/2024", "Genre" to "Fantasy", "Book Type" to "eBook",
        "Started" to "01/01/2024", "Finished" to "20/01/2024", "Rating" to "4",
        "Review" to "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
        "type" to "Book")
    var reviewsRef = FirebaseDatabase.getInstance().getReference("Users/$userID/Reviews")

    reviewsRef.child(reviewID).get().addOnSuccessListener {
        if (it.exists()) {
            println("Data Exists")

            var type = it.child("type").value.toString()
            var title = it.child("title").value.toString()
            var date = it.child("date").value.toString()
            var genre = it.child("genre").value.toString()
            //var started = it.child("started").value.toString()
            //var finished = it.child("finished").value.toString()
            var rating = it.child("rating").value.toString()
            var review = it.child("paragraph").value.toString()

            when (type) {
                "Book" -> {
                    var author = it.child("author").value.toString()
                    var typeType = it.child("book type").value.toString()

                    reviewData = mutableMapOf("Title" to title, "Author" to author,
                        "Date Published" to date, "Genre" to genre, "Book Type" to typeType,
                        "Started" to "01/01/2024", "Finished" to "20/01/2024", "Rating" to rating,
                        "Review" to review, "type" to type)
                }
                "TV Show" -> {
                    var author = it.child("director").value.toString()
                    var typeType = it.child("streamingService").value.toString()

                    reviewData = mutableMapOf("Title" to title, "Director" to author,
                        "Date Published" to date, "Genre" to genre, "Streaming Service" to typeType,
                        "Started" to "01/01/2024", "Finished" to "20/01/2024", "Rating" to rating,
                        "Review" to review, "type" to type)
                }
                "Movie" -> {
                    var author = it.child("director").value.toString()
                    var typeType = it.child("publicationCompany").value.toString()

                    reviewData = mutableMapOf("Title" to title, "Director" to author,
                        "Date Published" to date, "Genre" to genre, "Publication Company" to typeType,
                        "Started" to "01/01/2024", "Finished" to "20/01/2024", "Rating" to rating,
                        "Review" to review, "type" to type)
                }
            }

        } else {
            println("Data doesn't exist")
        }
        println("Successful")

    }.addOnFailureListener {
        println("Unsuccessful")
    }

    return reviewData
}
