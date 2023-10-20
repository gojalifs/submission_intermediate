package com.satria.dicoding.latihan.storyapp_submission.view.home

import androidx.lifecycle.ViewModel
import com.satria.dicoding.latihan.storyapp_submission.data.repository.StoryRepository

class HomeViewModel(private val storyRepository: StoryRepository) : ViewModel() {
//    private val _stories = MutableLiveData<List<ListStoryItem>>()
//    var stories : LiveData<List<ListStoryItem>> = _stories
//
    fun getStories() = storyRepository.getStories()
}