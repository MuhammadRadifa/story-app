package com.dicoding.storyapp.ui.home


import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.dicoding.storyapp.data.response.Story
import com.dicoding.storyapp.repository.StoryRepository

class HomeViewModel(
    storyRepository: StoryRepository,
): ViewModel(){

    val storyList: LiveData<PagingData<Story>> =
        storyRepository.getStory().cachedIn(viewModelScope)
}