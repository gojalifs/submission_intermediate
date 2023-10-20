package com.satria.dicoding.latihan.storyapp_submission.data.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.satria.dicoding.latihan.storyapp_submission.data.di.Injection
import com.satria.dicoding.latihan.storyapp_submission.data.repository.StoryRepository
import com.satria.dicoding.latihan.storyapp_submission.view.home.HomeViewModel

class HomeViewModelFactory private constructor(private val storyRepository: StoryRepository) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(storyRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class ${modelClass.name}")
    }

    companion object {
        @Volatile
        private var instance: HomeViewModelFactory? = null

        fun getInstance(context: Context): HomeViewModelFactory = instance ?: synchronized(this) {
            instance ?: HomeViewModelFactory(Injection.provideStoryRepository(context))
        }.also { instance = it }
    }
}