package com.satria.dicoding.latihan.storyapp_submission.view.new_story

import androidx.lifecycle.ViewModel
import com.satria.dicoding.latihan.storyapp_submission.data.repository.StoryRepository
import java.io.File

class NewStoryViewModel(private val storyRepository: StoryRepository) : ViewModel() {
    fun addNewStory(image: File, description: String) = storyRepository.addStory(image, description)
}