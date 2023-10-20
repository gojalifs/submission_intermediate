package com.satria.dicoding.latihan.storyapp_submission.data.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.satria.dicoding.latihan.storyapp_submission.data.di.Injection
import com.satria.dicoding.latihan.storyapp_submission.data.repository.RegisterRepository
import com.satria.dicoding.latihan.storyapp_submission.view.register.RegisterViewModel

class RegisterViewModelFactory private constructor(private val registerRepository: RegisterRepository) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
            return RegisterViewModel(registerRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class ${modelClass.name}")
    }

    companion object {
        @Volatile
        private var instance: RegisterViewModelFactory? = null

        fun getInstance(): RegisterViewModelFactory = instance ?: synchronized(this) {
            instance ?: RegisterViewModelFactory(Injection.provideRegisterRepository())
        }.also { instance = it }
    }
}