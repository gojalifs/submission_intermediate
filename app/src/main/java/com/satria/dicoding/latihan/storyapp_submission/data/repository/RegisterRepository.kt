package com.satria.dicoding.latihan.storyapp_submission.data.repository

import androidx.lifecycle.liveData
import com.satria.dicoding.latihan.storyapp_submission.data.ResultState
import com.satria.dicoding.latihan.storyapp_submission.data.api.ApiService
import retrofit2.HttpException
import java.net.UnknownHostException

class RegisterRepository private constructor(private val apiService: ApiService) {
    fun register(name: String, email: String, password: String) = liveData {
        emit(ResultState.Loading)

        try {
            val registerResponse = apiService.register(name, email, password)
            val message = registerResponse.message
            emit(ResultState.Success(message))
        } catch (e: HttpException) {
            val errorBody = e.response()?.message()
            val code = e.code()
            when (code) {
                in 300..399 -> {
                    emit(ResultState.Error("Need To Reconfigure. Please Contact Administrator"))
                }

                in 400..499 -> {
                    emit(ResultState.Error("Request Error. code $code $errorBody"))
                }

                in 500..599 -> {
                    emit(ResultState.Error("Server Error"))
                }
            }
        } catch (e: UnknownHostException) {
            emit(ResultState.Error("Error... Please check your connection"))
        } catch (e: Exception) {
            emit(ResultState.Error("Unknown Error"))
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