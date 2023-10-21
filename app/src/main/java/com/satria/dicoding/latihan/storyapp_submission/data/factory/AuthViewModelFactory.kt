package com.satria.dicoding.latihan.storyapp_submission.data.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.satria.dicoding.latihan.storyapp_submission.data.di.Injection
import com.satria.dicoding.latihan.storyapp_submission.data.repository.AuthRepository
import com.satria.dicoding.latihan.storyapp_submission.view.auth.AuthViewModel

class AuthViewModelFactory private constructor(private val loginRepository: AuthRepository) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            return AuthViewModel(loginRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class ${modelClass.name}")
    }

    companion object {
        @Volatile
        private var instance: AuthViewModelFactory? = null

        fun getInstance(): AuthViewModelFactory = instance ?: synchronized(this) {
            instance ?: AuthViewModelFactory(Injection.provideLoginRepository())
        }.also { instance = it }
    }
}