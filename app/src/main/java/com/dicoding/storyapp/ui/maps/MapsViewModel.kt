package com.dicoding.storyapp.ui.maps

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.storyapp.data.ResultState

import com.dicoding.storyapp.data.response.Story
import com.dicoding.storyapp.repository.StoryRepository
import kotlinx.coroutines.launch

class MapsViewModel(
    private val storyRepository: StoryRepository
): ViewModel(){

    private val _storyLocation = MutableLiveData<ResultState<List<Story>>>()
    val storyLocation: MutableLiveData<ResultState<List<Story>>> = _storyLocation

    init {
        getStoryWithLocation()
    }

    private fun getStoryWithLocation(){
        viewModelScope.launch {
            try {
                _storyLocation.value = ResultState.Loading
                val response = storyRepository.getStoriesWithLocation()
                if (response.listStory.isNotEmpty()){
                    _storyLocation.value = ResultState.Success(response.listStory)
                }else{
                    _storyLocation.value = ResultState.Error("Data not found")
                }
            }catch(e: Exception){
                _storyLocation.value = ResultState.Error(e.message.toString())
            }
        }
            }
}