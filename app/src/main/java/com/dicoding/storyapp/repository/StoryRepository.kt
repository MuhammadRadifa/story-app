package com.dicoding.storyapp.repository

import androidx.lifecycle.LiveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.dicoding.storyapp.data.StoryRemoteMediator
import com.dicoding.storyapp.data.response.Story
import com.dicoding.storyapp.data.retrofit.ApiService
import com.dicoding.storyapp.database.StoryDatabase
import com.dicoding.storyapp.utils.SettingPreferences
import okhttp3.MultipartBody
import okhttp3.RequestBody

class StoryRepository private constructor(
    private val apiService: ApiService,
    private val pref: SettingPreferences,
    private val database: StoryDatabase
) {
    fun getToken() = pref.getToken()

    suspend fun saveToken(token: String) = pref.saveToken(token)


    @OptIn(ExperimentalPagingApi::class)
    fun getStory(): LiveData<PagingData<Story>> {
        return Pager(
            config = PagingConfig(
                pageSize = 3
            ),
            pagingSourceFactory = {
                database.storyDao().getAllStory()
            } ,
            remoteMediator = StoryRemoteMediator(database, apiService)
        ).liveData
    }

    suspend fun getStoryWidget() = apiService.getWidgetStories()

    suspend fun login(email: String, password: String) = apiService.login(email, password)

    suspend fun register(name: String, email: String, password: String) = apiService.register(name, email, password)

    suspend fun getDetailStory(id: String) = apiService.getDetailStory(id)

    suspend fun addStory(
        multipartBody: MultipartBody.Part,
        requestBodyDescription: RequestBody,
        latRequestBody:RequestBody?,
        lonRequestBody:RequestBody?
    ) = apiService.uploadImage(multipartBody, requestBodyDescription, latRequestBody, lonRequestBody)

    suspend fun getStoriesWithLocation() = apiService.getStoriesWithLocation()

    companion object {
        @Volatile
        private var instance: StoryRepository? = null

        fun getInstance(apiService: ApiService, pref: SettingPreferences,database: StoryDatabase): StoryRepository =
            instance ?: synchronized(this) {
                instance ?: StoryRepository(apiService, pref,database).also { instance = it }
            }
    }
}
