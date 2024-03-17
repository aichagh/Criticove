package com.criticove.backend

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
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
    //private val _userID: MutableStateFlow<String> = MutableStateFlow("ZFZrCVjIR0P76TqT5lxX0W3dUI93")
    var userID: String = "ZFZrCVjIR0P76TqT5lxX0W3dUI93"
    private val _reviewList: MutableStateFlow<MutableList<Review>> = MutableStateFlow(mutableListOf())
    val reviewList: StateFlow<MutableList<Review>> = _reviewList
    private val _friendMap: MutableStateFlow<MutableMap<String, String>> = MutableStateFlow(mutableMapOf<String, String>())
    val friendMap: StateFlow<MutableMap<String, String>> = _friendMap
    private val _userMap: MutableStateFlow<MutableMap<String, String>> = MutableStateFlow(mutableMapOf<String, String>())
    val userMap: StateFlow<MutableMap<String, String>> = _userMap
    private val _friendReviews: MutableStateFlow<MutableMap<String, List<Review>>> = MutableStateFlow(mutableMapOf())
    val friendReviews: StateFlow<MutableMap<String, List<Review>>> = _friendReviews

    fun addFriend(friendusername: String) {
        val friendID = userMap.value.entries.find { it.value == friendusername }?.key
        var friendsRef = FirebaseDatabase.getInstance().getReference("Users/${userID}/Friends")
        if (friendID != null) {
            friendsRef.child("$friendID").setValue(friendusername)
        }
    }

    fun getFriends() {
        var curuserID = this.userID
        var friendsRef = FirebaseDatabase.getInstance().getReference("Users/${userID}/Friends")
        friendsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val newfriendMap: MutableMap<String, String> = mutableMapOf<String, String>()
                for (childSnapshot in dataSnapshot.children) {
                    val friendID = childSnapshot.key
                    if (friendID is String) {
                        val frienduserName =
                            childSnapshot.getValue(String::class.java)
                        println("in addfriend friends id $friendID")
                        println("in addfriend friends name $frienduserName")
                        if (friendID is String && frienduserName is String) {
                            newfriendMap[friendID] = frienduserName
                        }
                        _friendMap.update{newfriendMap}
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Log.w(TAG, "review:onCancelled", databaseError.toException())
            }
        })
    }

    fun getUsers() {
        //getfriendReviews()
        //addFriend("bear")
        val usersRef = FirebaseDatabase.getInstance().getReference("Users")
        var curuserID = this.userID

        usersRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val newUserMap: MutableMap<String, String> = mutableMapOf<String, String>()
                for (childSnapshot in dataSnapshot.children) {
                    val userID = childSnapshot.key
                    val userName = childSnapshot.child("username").getValue(String::class.java)
                    println("get users $userID")
                    println("get users $userName")
                    if (userID is String && userName is String) {
                        if (userID != curuserID) {
                            newUserMap[userID] = userName
                        }
                    }
                    _userMap.update{newUserMap}
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Log.w(TAG, "review:onCancelled", databaseError.toException())
            }

        })
    }

    fun getReviews() {
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
                        val sDB = review["shared"]
                        val s = when (sDB) {
                            is Boolean -> sDB
                            else -> false
                        }
                        println("this is rint $r")

                        when (review["type"]) {
                            "Book" -> {
                                reviewPost = BookReview(
                                    "Book", review["title"].toString(), review["date"].toString(),
                                    review["genre"].toString(), r , review["paragraph"].toString(),
                                    review["author"].toString(), review["booktype"].toString(),
                                    review["datefinished"].toString(), s
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
                                    review["streamingservice"].toString(),
                                    review["datewatched"].toString(), s
                                )
                            }

                            "TV Show" -> {
                                reviewPost = TVShowReview(
                                    "Book", review["title"].toString(), review["date"].toString(),
                                    review["genre"].toString(), r, review["paragraph"].toString(),
                                    review["director"].toString(), review["streamingservice"].toString(),
                                    review["datefinished"].toString(), s
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
    fun getfriendReviews() {
        var friendsRef = FirebaseDatabase.getInstance().getReference("Users/${userID}/Friends")
        friendsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val newfriendMap: MutableMap<String, List<Review>> = mutableMapOf<String, List<Review>>()
                for (childSnapshot in dataSnapshot.children) {
                    val friendID = childSnapshot.key
                    val frienduserName = childSnapshot.getValue(String::class.java)
                    if (friendID is String && frienduserName is String) {
                        var friendreviewsRef = FirebaseDatabase.getInstance().getReference("Users/$friendID/Reviews")
                        friendreviewsRef.addValueEventListener(object : ValueEventListener {
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
                                    val sDB = review["shared"]
                                    val s = when (sDB) {
                                        is Boolean -> sDB
                                        else -> false
                                    }

                                    when (review["type"]) {
                                        "Book" -> {
                                            reviewPost = BookReview(
                                                "Book", review["title"].toString(), review["date"].toString(),
                                                review["genre"].toString(), r , review["paragraph"].toString(),
                                                review["author"].toString(), review["booktype"].toString(),
                                                review["datefinished"].toString(), s
                                            )
                                        }
                                        "Movie" -> {
                                            reviewPost = MovieReview(
                                                "Movie", review["title"].toString(), review["date"].toString(),
                                                review["genre"].toString(), r, review["paragraph"].toString(),
                                                review["director"].toString(), review["streamingservice"].toString(),
                                                review["datewatched"].toString(), s
                                            )
                                        }
                                        "TV Show" -> {
                                            reviewPost = TVShowReview(
                                                "Book", review["title"].toString(), review["date"].toString(),
                                                review["genre"].toString(), r, review["paragraph"].toString(),
                                                review["director"].toString(), review["streamingservice"].toString(),
                                                review["datefinished"].toString(), s
                                            )
                                        }
                                    }
                                    if (s) {
                                        newReviewList.add(reviewPost)
                                        newfriendMap[frienduserName] = newReviewList
                                        _friendReviews.update { newfriendMap }
                                        _friendReviews.value.forEach { (frienuserName, reviews) ->
                                            println("in getfriendreviews Friend ID: $frienduserName")
                                            reviews.forEach { review ->
                                                println("in getfriendreviews Title: ${review.title}")
                                            }
                                        }
                                    }
                                }
                            }
                            override fun onCancelled(error: DatabaseError) {
                                // Log.w(TAG, "review:onCancelled", databaseError.toException())
                            }

                        })
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Log.w(TAG, "review:onCancelled", databaseError.toException())
            }
        })

    }
    fun signupUser(username: String) {
        val user = Firebase.auth.currentUser
        if (user != null) {
            this.userID = user.uid
        }
        var userRef = FirebaseDatabase.getInstance().getReference("Users/${userID}")
        userRef.child("username").setValue(username)
    }
    fun loginUser() {
        val user = Firebase.auth.currentUser
        if (user != null) {
            this.userID = user.uid
        }
    }

    constructor() {

    }

}

fun getUserID(username: String) {


}
open class Review(val type: String, val title: String, val date: String, val genre: String, val rating: Int, val paragraph: String,
    val shared: Boolean = false) {
}

class BookReview(type: String, title:String, date:String, genre: String, rating: Int, paragraph: String,
                 val author: String, val booktype: String, val datefinished: String, shared: Boolean = false): Review(type, title, date, genre, rating, paragraph, shared) {
}
class TVShowReview(type: String, title:String, date:String, genre: String, rating: Int, paragraph: String,
                 val director: String, val streamingservice: String, val datefinished: String, shared: Boolean = false): Review(type, title, date, genre, rating, paragraph, shared) {
}

class MovieReview(type: String, title:String, date:String, genre: String, rating: Int, paragraph: String,
                   val director: String, val streamingservice: String, val datewatched: String, shared: Boolean = false): Review(type, title, date, genre, rating, paragraph, shared) {
}

fun SubmittedReview(type: String, rating: Int, shared: Boolean, review: MutableMap<String, String>) {
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
            reviewPost = BookReview("Book", review["Book Title"].toString(), review["Year Published"].toString(),
                review["Genre"].toString(), rating, review["Review"].toString(),
                review["Author"].toString(), review["Book Type"].toString(), review["Date finished"].toString(), shared)
        }
        "TV Show" -> {
            reviewPost = TVShowReview("TV Show", review["TV Show Title"].toString(), review["Year Released"].toString(),
            review["Genre"].toString(), rating, review["Review"].toString(),
            review["Director"].toString(), review["Streaming Service"].toString(), review["Date finished"].toString(), shared)
    }
        "Movie" -> {
            reviewPost = MovieReview("Movie", review["Movie Title"].toString(), review["Year Released"].toString(),
                review["Genre"].toString(), rating, review["Review"].toString(),

                review["Director"].toString(), review["Streaming Service"].toString(), review["Date watched"].toString(), shared)
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