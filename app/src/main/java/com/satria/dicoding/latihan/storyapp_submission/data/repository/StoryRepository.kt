package com.satria.dicoding.latihan.storyapp_submission.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.satria.dicoding.latihan.storyapp_submission.data.ResultState
import com.satria.dicoding.latihan.storyapp_submission.data.api.ApiService
import com.satria.dicoding.latihan.storyapp_submission.data.local.StoryDatabase
import com.satria.dicoding.latihan.storyapp_submission.data.remote_mediator.StoryRemoteMediator
import com.satria.dicoding.latihan.storyapp_submission.model.api_response.AllStoryResponse
import com.satria.dicoding.latihan.storyapp_submission.model.api_response.ListStoryItem
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import java.io.File
import java.net.UnknownHostException

class StoryRepository private constructor(
    private val storyDatabase: StoryDatabase,
    private val apiService: ApiService
) {

    fun getStories(): LiveData<PagingData<ListStoryItem>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(pageSize = 10, initialLoadSize = 15),
            remoteMediator = StoryRemoteMediator(storyDatabase, apiService),
            pagingSourceFactory = { storyDatabase.storyDao().getStories() },
        ).liveData
    }

    fun getStoriesByMap() = liveData {
        emit(ResultState.Loading)

        try {
            val storyResponse: AllStoryResponse = apiService.getStoriesWithLocation()

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

    fun addStory(image: File, description: String, lat: Double?, long: Double?) = liveData {
        emit(ResultState.Loading)
        val descriptionBody = description.toRequestBody("text/plain".toMediaType())
        val requestImageFile = image.asRequestBody("image/jpeg".toMediaType())
        val multipartBody = MultipartBody.Part.createFormData(
            "photo", image.name, requestImageFile
        )
        try {
            val successResponse = if (lat == null || long == null) {
                apiService.addStory(multipartBody, descriptionBody, lat = null, lon = null)
            } else {
                apiService.addStory(multipartBody, descriptionBody, lat, long)
            }
            emit(ResultState.Success(successResponse))
        } catch (e: HttpException) {
            val errorBody = e.response()?.message()

            when (val code = e.code()) {
                in 300..399 -> {
                    emit(ResultState.Error("Need To Reconfigure. Please Contact Administrator"))
                }

                in 400..499 -> {
                    emit(ResultState.Error("Request Error. code $code ${e.response()?.errorBody()}"))
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
        private var dbInstance: StoryDatabase? = null
        fun getInstance(apiService: ApiService, storyDatabase: StoryDatabase) =
            instance ?: synchronized(this) {
                instance ?: StoryRepository(storyDatabase, apiService)
            }.also { instance = it }
    }
}