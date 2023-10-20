package com.satria.dicoding.latihan.storyapp_submission.view.login

import androidx.lifecycle.ViewModel
import com.satria.dicoding.latihan.storyapp_submission.data.repository.LoginRepository

class LoginViewModel(private val loginRepository: LoginRepository) : ViewModel() {
    fun login(email: String, password: String) = loginRepository.login(email, password)
}