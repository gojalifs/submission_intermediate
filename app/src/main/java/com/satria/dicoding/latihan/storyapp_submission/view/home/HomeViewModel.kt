package com.satria.dicoding.latihan.storyapp_submission.view.home

import androidx.lifecycle.ViewModel
import com.satria.dicoding.latihan.storyapp_submission.data.repository.StoryRepository

class HomeViewModel(private val storyRepository: StoryRepository) : ViewModel() {
    fun getStories() = storyRepository.getStories()
}