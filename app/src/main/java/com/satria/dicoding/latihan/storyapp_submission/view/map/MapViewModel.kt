package com.satria.dicoding.latihan.storyapp_submission.view.map

import androidx.lifecycle.ViewModel
import com.satria.dicoding.latihan.storyapp_submission.data.repository.StoryRepository

class MapViewModel(private val storyRepository: StoryRepository) : ViewModel() {
    fun getStories() = storyRepository.getStoriesByMap()
}