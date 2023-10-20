package com.satria.dicoding.latihan.storyapp_submission.view.home

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.satria.dicoding.latihan.storyapp_submission.data.ResultState
import com.satria.dicoding.latihan.storyapp_submission.data.factory.HomeViewModelFactory
import com.satria.dicoding.latihan.storyapp_submission.databinding.ActivityHomeBinding
import com.satria.dicoding.latihan.storyapp_submission.model.api_response.ListStoryItem

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private val viewModel by viewModels<HomeViewModel> {
        HomeViewModelFactory.getInstance(applicationContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val layoutManager = LinearLayoutManager(this)
        binding.rvStory.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvStory.addItemDecoration(itemDecoration)

        viewModel.getStories().observe(this) { state ->
            if (state != null) {
                when (state) {
                    is ResultState.Loading -> {
                        showLoading(true)
                        /// Todo: add shimmer
                    }

                    is ResultState.Success -> {
                        showLoading(false)
                        val stories = state.data.listStory
                        setStories(stories ?: emptyList<ListStoryItem>())
                    }

                    is ResultState.Error -> {
                        showLoading(false)
                        /// TODO: add error
                    }
                }
            }
        }
    }

    private fun setStories(stories: List<ListStoryItem?>) {
        val adapter = StoryAdapter()
        adapter.submitList(stories)
        binding.rvStory.adapter = adapter
    }

    private fun showLoading(isLoading: Boolean? = true) {

    }
}