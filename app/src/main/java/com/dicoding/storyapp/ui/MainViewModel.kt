package com.dicoding.storyapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.dicoding.storyapp.utils.SettingPreferences
import kotlinx.coroutines.launch

class MainViewModel(
    private val pref: SettingPreferences
): ViewModel(){
    fun getToken() = pref.getToken().asLiveData()

    fun getThemeSetting() = pref.getThemeSetting().asLiveData()

    fun setThemeSetting(isDarkModeActive:Boolean){
        viewModelScope.launch {
            pref.saveThemeSetting(isDarkModeActive)
        }
    }

    fun getLanguageSetting() = pref.getLanguageSetting().asLiveData()

    fun setLanguageSetting(language:String){
        viewModelScope.launch {
            pref.saveLanguageSetting(language)
        }
    }

    fun logout(){
        viewModelScope.launch {
            pref.clearToken()
        }
    }

}