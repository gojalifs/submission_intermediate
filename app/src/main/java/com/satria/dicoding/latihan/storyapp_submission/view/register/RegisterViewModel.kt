package com.satria.dicoding.latihan.storyapp_submission.view.register

import androidx.lifecycle.ViewModel
import com.satria.dicoding.latihan.storyapp_submission.data.repository.RegisterRepository

class RegisterViewModel(private val registerRepository: RegisterRepository) : ViewModel() {
    fun register(name: String, email: String, password: String) =
        registerRepository.register(name, email, password)
}