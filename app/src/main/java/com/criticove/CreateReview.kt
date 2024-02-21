package com.criticove

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.RadioGroup
import android.widget.FrameLayout
import android.widget.RadioButton

class CreateReview : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.create_reviews)

        val radioButtons = findViewById<RadioGroup>(R.id.radioButtons)
        val inflater = LayoutInflater.from(this)
        val book = inflater.inflate(R.layout.book_form, null)
        val tvshow = inflater.inflate(R.layout.tv_form, null)
        val movie = inflater.inflate(R.layout.movie_form, null)
        val container = findViewById<FrameLayout>(R.id.container)
        container.addView(book)

        radioButtons.setOnCheckedChangeListener { group, checkedId ->
            val radioButton = findViewById<RadioButton>(checkedId)
            container.removeAllViews()
            if (checkedId == R.id.bookRadio) {
                container.addView(book)

            } else if (checkedId == R.id.tvRadio) {
                container.addView(tvshow)

            } else if (checkedId == R.id.movieRadio) {
                container.addView(movie)

            }
        }
    }
}