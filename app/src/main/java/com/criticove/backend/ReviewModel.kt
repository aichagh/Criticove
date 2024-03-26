package com.criticove.backend

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class userModel: ViewModel {
    //private val _userID: MutableStateFlow<String> = MutableStateFlow("ZFZrCVjIR0P76TqT5lxX0W3dUI93")
    var userID: String = "ZFZrCVjIR0P76TqT5lxX0W3dUI93"

    private val _reviewList: MutableStateFlow<MutableList<Review>> = MutableStateFlow(mutableListOf())
    val reviewList: StateFlow<MutableList<Review>> = _reviewList

    private val _selReview: MutableStateFlow<Review> = MutableStateFlow(Review("Book",
        "test", "test", "test", 4, "test", "test"))
    val selReview: StateFlow<Review> = _selReview

    private val _selFriendReview: MutableStateFlow<Review> = MutableStateFlow(Review("Book",
        "test", "test", "test", 4, "test", "test"))
    val selFriendReview: StateFlow<Review> = _selFriendReview

    private val _friendMap: MutableStateFlow<MutableMap<String, String>> = MutableStateFlow(mutableMapOf<String, String>())
    val friendMap: StateFlow<MutableMap<String, String>> = _friendMap

    private val _userMap: MutableStateFlow<MutableMap<String, String>> = MutableStateFlow(mutableMapOf<String, String>())
    val userMap: StateFlow<MutableMap<String, String>> = _userMap

    private val _friendReviews: MutableStateFlow<MutableMap<Pair<String, String>, List<Review>>> = MutableStateFlow(mutableMapOf())
    val friendReviews: StateFlow<MutableMap<Pair<String, String>, List<Review>>> = _friendReviews


    fun getSelReview(reviewID: String, friendID: String = "none") {
        println("this is the user id : ${userID}")
        var reviewsRef = FirebaseDatabase.getInstance().getReference("Users/${userID}/Reviews/${reviewID}")

        if (friendID != "none") {
            println("Getting a friend's review")
            reviewsRef = FirebaseDatabase.getInstance().getReference("Users/${friendID}/Reviews/${reviewID}")
        }

        // var selReviewQuery = reviewsRef.orderByChild("title").equalTo(reviewTitle)

        println("the users ${this.userID} reviews keys and their corresponding values: ,")
        reviewsRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(reviewSnapshot: DataSnapshot) {
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
                            review["reviewID"].toString(), review["author"].toString(),
                            review["booktype"].toString(),
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
                            review["reviewID"].toString(),
                            review["director"].toString(),
                            review["streamingservice"].toString(),
                            review["datewatched"].toString(), s
                        )
                    }

                    "TV Show" -> {
                        reviewPost = TVShowReview(
                            "TV Show", review["title"].toString(), review["date"].toString(),
                            review["genre"].toString(), r, review["paragraph"].toString(),
                            review["reviewID"].toString(), review["director"].toString(),
                            review["streamingservice"].toString(),
                            review["datefinished"].toString(), s
                        )
                        }
                }
                _selReview.update{reviewPost}
                println("selReview in the event handler $selReview")
                println("this is the review back to a structure $reviewPost")
                println("this is the reviews title ${reviewPost.title}")
                println("this is the reviews date ${reviewPost.date}")
                println("this is the reviews par ${reviewPost.paragraph}")
                println("this is the reviews ID ${reviewPost.reviewID}")
            }

            override fun onCancelled(error: DatabaseError) {
                // Log.w(TAG, "review:onCancelled", databaseError.toException())
            }
        })
    }

    fun addFriend(friendusername: String) {
        val friendID = userMap.value.entries.find { it.value == friendusername }?.key
        var friendsRef = FirebaseDatabase.getInstance().getReference("Users/${userID}/Friends")
        if (friendID != null) {
            friendsRef.child("$friendID").setValue(friendusername)
        }
    }

    fun delFriend(friendusername: String) {
        val friendID = userMap.value.entries.find { it.value == friendusername }?.key
        if (friendID != null) {
            var friendsRef = FirebaseDatabase.getInstance().getReference("Users/${userID}/Friends/${friendID}")
            friendsRef.removeValue()
                .addOnSuccessListener {
                    println("removed friend $friendusername")
                }
            .addOnFailureListener { error ->
                println("removed friend didnt work $error")
            }
        }
    }
@Composable
    fun filter(type: String, bookmarks: Boolean = false):  List<Review> {
        val reviewlist by reviewList.collectAsState()
        if (!bookmarks) {
            return reviewlist.filter { it.type == type }
        } else {
            return reviewlist.filter { it.bookmarked }
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
        getfriendReviews()
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
                        val bmDB = review["bookmarked"]
                        val bm = when (bmDB) {
                            is Boolean -> bmDB
                            else -> false
                        }
                        println("this is bookmarked $bm")

                        when (review["type"]) {
                            "Book" -> {
                                reviewPost = BookReview(
                                    "Book", review["title"].toString(), review["date"].toString(),
                                    review["genre"].toString(), r , review["paragraph"].toString(),
                                    review["reviewID"].toString(), review["author"].toString(),
                                    review["booktype"].toString(),
                                    review["datefinished"].toString(), s, bm
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
                                    review["reviewID"].toString(),
                                    review["director"].toString(),
                                    review["streamingservice"].toString(),
                                    review["datewatched"].toString(), s, bm
                                )
                            }

                            "TV Show" -> {
                                reviewPost = TVShowReview(
                                    "TV Show", review["title"].toString(), review["date"].toString(),
                                    review["genre"].toString(), r, review["paragraph"].toString(),
                                    review["reviewID"].toString(), review["director"].toString(),
                                    review["streamingservice"].toString(),
                                    review["datefinished"].toString(), s, bm
                                )
                            }
                        }
                        newReviewList.add(reviewPost)
                        _reviewList.update{newReviewList}
                        println("reviewList in the event handelr $reviewList")
                        println("this is the review back to a structure $reviewPost")
                        println("this is the reviews title ${reviewPost.title}")
                        println("this is the reviews date ${reviewPost.date}")
                        println("this is the reviews bookmarked ${reviewPost.bookmarked}")
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
                val newfriendMap: MutableMap<Pair<String, String>, List<Review>> = mutableMapOf<Pair<String, String>, List<Review>>()
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
                                                review["reviewID"].toString(), review["author"].toString(),
                                                review["booktype"].toString(),
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
                                                review["reviewID"].toString(),
                                                review["director"].toString(),
                                                review["streamingservice"].toString(),
                                                review["datewatched"].toString(), s
                                            )
                                        }
                                        "TV Show" -> {
                                            reviewPost = TVShowReview(
                                                "TV Show", review["title"].toString(), review["date"].toString(),
                                                review["genre"].toString(), r, review["paragraph"].toString(),
                                                review["reviewID"].toString(), review["director"].toString(),
                                                review["streamingservice"].toString(),
                                                review["datefinished"].toString(), s
                                            )
                                        }
                                    }
                                    if (s) {
                                        newReviewList.add(reviewPost)
                                        newfriendMap[Pair(frienduserName, friendID)] = newReviewList
                                        _friendReviews.update { newfriendMap }
                                        _friendReviews.value.forEach { (frienuserName, reviews) ->
                                            reviews.forEach { review ->
                                                println("in getfriendreviews Friend ID: $friendID, $frienduserName and Title: ${review.title} ")
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

    //var friends: List<String>
    fun getCurUser() {
        val user = Firebase.auth.currentUser
        if (user != null) {
            this.userID = user.uid
            println("in the constructor user id is $userID")
        }
    }

    constructor() {

    }

}

open class Review(val type: String, val title: String, val date: String, val genre: String, val rating: Int, val paragraph: String, val reviewID: String,
    val shared: Boolean = false, val bookmarked: Boolean = false) {
}

class BookReview(type: String, title:String, date:String, genre: String, rating: Int, paragraph: String, reviewID: String,
                 val author: String, val booktype: String, val datefinished: String, shared: Boolean = false, bookmarked: Boolean = false): Review(type, title, date, genre, rating, paragraph, reviewID, shared, bookmarked) {
}
class TVShowReview(type: String, title:String, date:String, genre: String, rating: Int, paragraph: String, reviewID: String,
                 val director: String, val streamingservice: String, val datefinished: String, shared: Boolean = false, bookmarked: Boolean = false): Review(type, title, date, genre, rating, paragraph, reviewID, shared, bookmarked) {
}

class MovieReview(type: String, title:String, date:String, genre: String, rating: Int, paragraph: String, reviewID: String,
                   val director: String, val streamingservice: String, val datewatched: String, shared: Boolean = false, bookmarked: Boolean = false): Review(type, title, date, genre, rating, paragraph, reviewID, shared, bookmarked) {
}

fun SubmittedReview(type: String, rating: Int, shared: Boolean, review: MutableMap<String, String>, bookmarked: Boolean = false) {
    val user = Firebase.auth.currentUser
    lateinit var userID : String
    if (user != null) {
        userID = user.uid
        println("the user id is $userID")
    }
    //userID = "ZFZrCVjIR0P76TqT5lxX0W3dUI93"
    println("Here is review para in submitRev: ${review["Review"]}")

    var reviewsRef = FirebaseDatabase.getInstance().getReference("Users/$userID/Reviews")
    var newReview = reviewsRef.push()
    var newReviewID = newReview.key!!
    lateinit var reviewPost: Review
    when (type) {
        "Book" -> {
            reviewPost = BookReview("Book", review["Book Title"].toString(), review["Year Published"].toString(),
                review["Genre"].toString(), rating, review["Review"].toString(), newReviewID,
                review["Author"].toString(), review["Book Type"].toString(), review["Date finished"].toString(), shared, bookmarked)
        }
        "TV Show" -> {
            reviewPost = TVShowReview("TV Show", review["TV Show Title"].toString(), review["Year Released"].toString(),
            review["Genre"].toString(), rating, review["Review"].toString(), newReviewID,
            review["Director"].toString(), review["Streaming Service"].toString(), review["Date finished"].toString(), shared, bookmarked)
    }
        "Movie" -> {
            reviewPost = MovieReview("Movie", review["Movie Title"].toString(), review["Year Released"].toString(),
                review["Genre"].toString(), rating, review["Review"].toString(), newReviewID,
                review["Director"].toString(), review["Streaming Service"].toString(), review["Date watched"].toString(), shared, bookmarked)
        }
    }

    println("this is the review i just shared${reviewPost.title}")
    newReview.setValue(reviewPost)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                println("shared review")
            } else {
                Log.e("", "Post Failed", task.exception)
            }
        }
}

fun updateSelReview(reviewID: String, review: String) {
    val user = Firebase.auth.currentUser
    lateinit var userID : String
    if (user != null) {
        userID = user.uid
        println("the user id is $userID, and reviewId is $reviewID")
    }

    var reviewsRef = FirebaseDatabase.getInstance().getReference("Users/$userID/Reviews/$reviewID")
    reviewsRef.child("paragraph").setValue(review)
    println("successfully updated review to $review")

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
    println("successfully deleted")

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
                    var typeType = it.child("streamingService").value.toString()

                    reviewData = mutableMapOf("Title" to title, "Director" to author,
                        "Date Published" to date, "Genre" to genre, "Streaming Service" to typeType,
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

fun changeBookmark(reviewID: String, bm: Boolean) {
    val user = Firebase.auth.currentUser
    lateinit var userID: String
    if (user != null) {
        userID = user.uid
        println("the user id is $userID, and reviewId is $reviewID")
    }
    var reviewsRef = FirebaseDatabase.getInstance().getReference("Users/$userID/Reviews")
    val reviewRef = reviewsRef.child(reviewID)
    val bookmarkVal = mapOf<String, Any>("bookmarked" to bm)
    reviewRef.updateChildren(bookmarkVal)
        .addOnSuccessListener {
        println("Bookmark value is $bm")
        }
        .addOnFailureListener { error ->
            println("bookmark didnt work with error $error")
        }
}
