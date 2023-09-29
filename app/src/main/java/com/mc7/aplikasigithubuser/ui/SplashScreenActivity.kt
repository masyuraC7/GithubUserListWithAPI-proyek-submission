package com.mc7.aplikasigithubuser.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.mc7.aplikasigithubuser.R
import com.mc7.aplikasigithubuser.ui.viewmodel.SettingsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashScreenActivity : AppCompatActivity() {

    private val settingViewModel: SettingsViewModel by viewModels()

    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val wdw = WindowCompat.getInsetsController(window, window.decorView)
        // to swipe system bar hide
        WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        wdw.hide(WindowInsetsCompat.Type.systemBars())
        setContentView(R.layout.activity_splash_screen)

        settingViewModel.getThemeSettings().observe(this@SplashScreenActivity){ isDarkMode: Boolean ->
            if (isDarkMode) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
        }

        GlobalScope.launch(Dispatchers.Main){
            delay(3000L)
            val intentToMainActivity =
                Intent(this@SplashScreenActivity, MainActivity::class.java)
            startActivity(intentToMainActivity)
            finish()
        }
    }
}