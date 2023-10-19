package com.satria.dicoding.latihan.storyapp_submission.data.repository

import android.util.Log
import androidx.lifecycle.liveData
import com.satria.dicoding.latihan.storyapp_submission.data.ResultState
import com.satria.dicoding.latihan.storyapp_submission.data.api.ApiService
import retrofit2.HttpException

class RegisterRepository private constructor(private val apiService: ApiService) {
    fun register(name: String, email: String, password: String) = liveData {
        emit(ResultState.Loading)

        try {
            val registerResponse = apiService.register(name, email, password)
            val message = registerResponse.message
            emit(ResultState.Success(message))
            Log.d("Success Registering", "register: success register")
        } catch (e: HttpException) {
            val errorBody = e.response()?.message()
            val code = e.code()
            Log.d("Error Registering Code", "register: ${e.code()} $errorBody")
            when (code) {
                in 300..399 -> {
                    emit(ResultState.Error("Need To Reconfigure. Please Contact Administrator"))
                }

                in 400..499 -> {
                    emit(ResultState.Error("Request Error"))
                }

                in 500..599 -> {
                    emit(ResultState.Error("Server Error"))
                }
            }
        }
    }

    companion object {
        @Volatile
        private var instance: RegisterRepository? = null
        fun getInstance(apiService: ApiService) = instance ?: synchronized(this) {
            instance ?: RegisterRepository(apiService)
        }.also { instance = it }
    }
}