package com.dicoding.storyapp.ui.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.storyapp.data.ResultState
import com.dicoding.storyapp.data.response.LoginResponse
import com.dicoding.storyapp.repository.StoryRepository
import kotlinx.coroutines.launch
import retrofit2.HttpException

class LoginViewModel(
    private val storyRepository: StoryRepository,
):ViewModel(){

    private val _responseResult = MutableLiveData<ResultState<LoginResponse>>()
    val responseResult = _responseResult

    fun submitLogin(email:String, password:String){
        viewModelScope.launch {
            try {
                _responseResult.value = ResultState.Loading
                val response = storyRepository.login(email,password)
                if(response.loginResult.token.isNotEmpty()){
                    storyRepository.saveToken(response.loginResult.token)
                    _responseResult.value = ResultState.Success(response)
                }
            } catch (e:HttpException){
                val errorBody = e.response()?.errorBody()?.string()
                _responseResult.value = ResultState.Error(errorBody?:e.message())
            }
        }
    }
}