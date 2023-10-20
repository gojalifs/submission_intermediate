package com.satria.dicoding.latihan.storyapp_submission.data.di

import com.satria.dicoding.latihan.storyapp_submission.data.api.ApiConfig
import com.satria.dicoding.latihan.storyapp_submission.data.repository.LoginRepository
import com.satria.dicoding.latihan.storyapp_submission.data.repository.RegisterRepository

object Injection {
    fun provideRegisterRepository(): RegisterRepository {
        val apiService = ApiConfig.getApiService()
        return RegisterRepository.getInstance(apiService)
    }
    fun provideLoginRepository():LoginRepository{
        val apiService = ApiConfig.getApiService()
        return LoginRepository.getInstance(apiService)
    }
}