package com.dicoding.storyapp.ui.add


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.storyapp.data.ResultState
import com.dicoding.storyapp.data.response.MessageResponse
import com.dicoding.storyapp.repository.StoryRepository
import com.google.gson.Gson
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.HttpException

class AddStoryViewModel(
    private val storyRepository: StoryRepository
):ViewModel() {
    private val _responseResult = MutableLiveData<ResultState<MessageResponse>>()
    val responseResult: MutableLiveData<ResultState<MessageResponse>> = _responseResult

    fun addStory(multipartBody: MultipartBody.Part,requestBodyDescription: RequestBody,latRequestBody:RequestBody?,lonRequestBody:RequestBody?){
        viewModelScope.launch {
            try {
                _responseResult.value = ResultState.Loading
                storyRepository.getToken().collect{
                    token ->
                    if (token != null){
                        val response = storyRepository.addStory(multipartBody, requestBodyDescription,latRequestBody,lonRequestBody)
                        _responseResult.value = ResultState.Success(response)
                    }else{
                        _responseResult.value = ResultState.Error("Token not found")
                    }
                }

            }catch (e:HttpException){
                val errorBody = e.response()?.errorBody()?.string()
                val errorResponse = Gson().fromJson(errorBody, MessageResponse::class.java)
                _responseResult.value = ResultState.Error(errorResponse.message)
            }

        }
    }
}