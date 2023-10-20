package com.satria.dicoding.latihan.storyapp_submission.data.di

import android.content.Context
import com.satria.dicoding.latihan.storyapp_submission.data.api.ApiConfig
import com.satria.dicoding.latihan.storyapp_submission.data.prefs.SessionPreferences
import com.satria.dicoding.latihan.storyapp_submission.data.prefs.dataStore
import com.satria.dicoding.latihan.storyapp_submission.data.repository.LoginRepository
import com.satria.dicoding.latihan.storyapp_submission.data.repository.RegisterRepository
import com.satria.dicoding.latihan.storyapp_submission.data.repository.StoryRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideRegisterRepository(): RegisterRepository {
        val apiService = ApiConfig.getApiService()
        return RegisterRepository.getInstance(apiService)
    }

    fun provideLoginRepository(): LoginRepository {
        val apiService = ApiConfig.getApiService()
        return LoginRepository.getInstance(apiService)
    }

    fun provideStoryRepository(context: Context): StoryRepository {
        val pref = SessionPreferences.getInstance(context.dataStore)
        val token = runBlocking { pref.getToken().first() }
        val apiService = ApiConfig.getApiService(token)
        return StoryRepository.getInstance(apiService)
    }
}