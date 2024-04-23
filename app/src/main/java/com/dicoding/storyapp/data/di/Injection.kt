package com.dicoding.storyapp.data.di

import android.content.Context
import com.dicoding.storyapp.data.retrofit.ApiConfig
import com.dicoding.storyapp.database.StoryDatabase
import com.dicoding.storyapp.repository.StoryRepository
import com.dicoding.storyapp.utils.SettingPreferences
import com.dicoding.storyapp.utils.dataStore

object Injection {
    fun provideRepository(context: Context): StoryRepository {
        val database = StoryDatabase.getDatabase(context)
        val pref = SettingPreferences.getInstance(context.dataStore)
        val apiService = ApiConfig.getApiService(pref)
        return StoryRepository.getInstance(apiService, pref,database)
    }
}