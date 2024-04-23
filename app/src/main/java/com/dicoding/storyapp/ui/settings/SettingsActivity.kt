package com.dicoding.storyapp.ui.settings

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.dicoding.storyapp.R
import com.dicoding.storyapp.databinding.ActivitySettingsBinding
import com.dicoding.storyapp.helper.ViewModelFactory
import com.dicoding.storyapp.ui.MainViewModel
import com.dicoding.storyapp.ui.login.LoginActivity
import com.dicoding.storyapp.utils.setLocale

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
    private val factory: ViewModelFactory = ViewModelFactory.getInstance(this)
    private val mainViewModel: MainViewModel by viewModels<MainViewModel> {
        factory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setCustomView(R.layout.app_bar)
        supportActionBar?.setDisplayShowCustomEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setBackgroundDrawable(ColorDrawable(getColor(R.color.orange)))



        binding.apply {
            mainViewModel.getThemeSetting().observe(this@SettingsActivity){
                switchDarkMode.isChecked = it
            }

            mainViewModel.getLanguageSetting().observe(this@SettingsActivity){
                switchLanguage.isChecked = it == "en"
            }

            switchLanguage.setOnCheckedChangeListener { _, isChecked ->
                setLocale(this@SettingsActivity, if (isChecked) "en" else "in")
                switchLanguage.isChecked = isChecked
                mainViewModel.setLanguageSetting(if (isChecked) "en" else "in")
            }
            switchDarkMode.setOnCheckedChangeListener{
                _, isChecked ->

                AppCompatDelegate.setDefaultNightMode(if (isChecked) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO)
                switchDarkMode.isChecked = isChecked
                mainViewModel.setThemeSetting(switchDarkMode.isChecked)
            }
            val intentLogout = Intent(this@SettingsActivity, LoginActivity::class.java)
            actionLogout.setOnClickListener {
                mainViewModel.logout()

                intentLogout.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intentLogout)
                finish()
            }
        }
    }

}