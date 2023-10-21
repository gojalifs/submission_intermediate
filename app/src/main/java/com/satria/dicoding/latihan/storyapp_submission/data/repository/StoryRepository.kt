package com.satria.dicoding.latihan.storyapp_submission.data.repository

import android.util.Log
import androidx.lifecycle.liveData
import com.satria.dicoding.latihan.storyapp_submission.data.ResultState
import com.satria.dicoding.latihan.storyapp_submission.data.api.ApiService
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import java.io.File
import java.net.UnknownHostException

class StoryRepository private constructor(private val apiService: ApiService) {
    fun getStories() = liveData {
        emit(ResultState.Loading)

        try {
            val storyResponse = apiService.getStories()

            emit(ResultState.Success(storyResponse))
        } catch (e: HttpException) {
            val errorBody = e.response()?.message()

            when (val code = e.code()) {
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

    fun addStory(image: File, description: String) = liveData {
        emit(ResultState.Loading)
        val requestBody = description.toRequestBody("text/plain".toMediaType())
        val requestImageFile = image.asRequestBody("image/jpeg".toMediaType())
        val multipartBody = MultipartBody.Part.createFormData(
            "photo", image.name, requestImageFile
        )
        try {
            val successResponse = apiService.addStory(multipartBody, requestBody)
            emit(ResultState.Success(successResponse))
        } catch (e: HttpException) {
            val errorBody = e.response()?.message()

            when (val code = e.code()) {
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
            Log.d("Upload Error", "addStory: Error $e")
        }
    }

    companion object {
        @Volatile
        private var instance: StoryRepository? = null
        fun getInstance(apiService: ApiService) = instance ?: synchronized(this) {
            instance ?: StoryRepository(apiService)
        }.also { instance = it }
    }
}