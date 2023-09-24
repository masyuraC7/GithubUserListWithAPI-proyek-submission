package com.mc7.aplikasigithubuser.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.mc7.aplikasigithubuser.R

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val wdw = WindowCompat.getInsetsController(window, window.decorView)
        // to swipe system bar hide
        WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        wdw.hide(WindowInsetsCompat.Type.systemBars())
        setContentView(R.layout.activity_splash_screen)

        Handler(Looper.getMainLooper()).postDelayed({
            val intentToMainActivity =
                Intent(this, MainActivity::class.java)
            startActivity(intentToMainActivity)
            finish()
        }, 3000)
    }
}