package com.satria.dicoding.latihan.storyapp_submission.view.auth

import androidx.lifecycle.ViewModel
import com.satria.dicoding.latihan.storyapp_submission.data.repository.AuthRepository

class AuthViewModel(private val authRepository: AuthRepository) : ViewModel() {
    fun login(email: String, password: String) = authRepository.login(email, password)
    fun register(name: String, email: String, password: String) =
        authRepository.register(name, email, password)
}