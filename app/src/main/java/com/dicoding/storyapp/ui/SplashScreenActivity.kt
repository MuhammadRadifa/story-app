package com.dicoding.storyapp.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.dicoding.storyapp.R
import com.dicoding.storyapp.helper.ViewModelFactory
import com.dicoding.storyapp.ui.home.HomeActivity
import com.dicoding.storyapp.ui.login.LoginActivity
import com.dicoding.storyapp.utils.setLocale

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        supportActionBar?.hide()

        val factory: ViewModelFactory = ViewModelFactory.getInstance(this)
        val mainViewModel: MainViewModel by viewModels<MainViewModel> {
            factory
        }

        Handler(Looper.getMainLooper()).postDelayed(
            {
                mainViewModel.getThemeSetting().observe(this){
                        isDarkModeActive ->
                    AppCompatDelegate.setDefaultNightMode(if (isDarkModeActive) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO)
                }

                mainViewModel.getLanguageSetting().observe(this){
                    language ->
                    setLocale(this,language)
//                    recreate()
                }
                mainViewModel.getToken().observe(this){
                    token ->
                    val intentActivity = Intent(this, if (token == null) LoginActivity::class.java else HomeActivity::class.java)
                    startActivity(intentActivity)
                    finish()
                }

            },
            SPLASH_TIME_OUT
        )

    }

    companion object {
        const val SPLASH_TIME_OUT = 3000L
    }
}