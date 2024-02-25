package com.criticove

import android.app.Activity
import android.os.Bundle
import android.content.Intent
import android.os.Handler
import android.os.Looper
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen

class SplashScreen : Activity() {
    private val SPLASH_DELAY: Long = 2000 // 2 seconds delay

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        installSplashScreen()
//        setContentView(R.layout.splash_screen)
//
//        Handler(Looper.getMainLooper()).postDelayed({
//            val intent = Intent(this, Signup::class.java)
//            startActivity(intent)
//            finish()
//        }, SPLASH_DELAY)
    }



}