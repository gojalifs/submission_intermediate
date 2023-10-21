package com.satria.dicoding.latihan.storyapp_submission.view.home

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.satria.dicoding.latihan.storyapp_submission.R
import com.satria.dicoding.latihan.storyapp_submission.data.ResultState
import com.satria.dicoding.latihan.storyapp_submission.data.factory.HomeViewModelFactory
import com.satria.dicoding.latihan.storyapp_submission.data.factory.MainViewModelFactory
import com.satria.dicoding.latihan.storyapp_submission.data.prefs.SessionPreferences
import com.satria.dicoding.latihan.storyapp_submission.data.prefs.dataStore
import com.satria.dicoding.latihan.storyapp_submission.databinding.ActivityHomeBinding
import com.satria.dicoding.latihan.storyapp_submission.model.api_response.ListStoryItem
import com.satria.dicoding.latihan.storyapp_submission.view.init.MainViewModel
import com.satria.dicoding.latihan.storyapp_submission.view.login.LoginActivity
import com.satria.dicoding.latihan.storyapp_submission.view.new_story.NewStoryActivity

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private val viewModel by viewModels<HomeViewModel> {
        HomeViewModelFactory.getInstance(applicationContext)
    }

    private lateinit var sessionViewModel: MainViewModel

    private val resultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == NewStoryActivity.RESULT_CODE && result.data != null) {
            val value = result.data?.getBooleanExtra(NewStoryActivity.EXTRA_DATA, false)
            if (value!!) {
                getStories()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val preferences = SessionPreferences.getInstance(applicationContext.dataStore)
        sessionViewModel = ViewModelProvider(
            this, MainViewModelFactory(preferences)
        )[MainViewModel::class.java]

        val layoutManager = LinearLayoutManager(this)
        binding.rvStory.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvStory.addItemDecoration(itemDecoration)

        binding.fabAddStory.setOnClickListener {
            val intent = Intent(this, NewStoryActivity::class.java)
            resultLauncher.launch(intent)
        }

        binding.appBar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.menu_logout -> {
                    sessionViewModel.deleteToken()
                    val intent = Intent(this, LoginActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
                    startActivity(intent)
                    true
                }

                else -> false
            }
        }

        binding.swipeRefresh.setOnRefreshListener {
            getStories()
            binding.swipeRefresh.isRefreshing = false
        }

        getStories()
    }

    private fun getStories() {
        viewModel.getStories().observe(this) { state ->
            if (state != null) {
                when (state) {
                    is ResultState.Loading -> {
                        showLoading(true)
                    }

                    is ResultState.Success -> {
                        showLoading(false)
                        val stories = state.data.listStory ?: mutableListOf()
                        setStories(stories)
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

    private fun showLoading(isLoading: Boolean) {
        with(binding) {
            if (isLoading) {
                rvStory.visibility = View.INVISIBLE
                rvStoryShimmer.visibility = View.VISIBLE
            } else {
                rvStory.visibility = View.VISIBLE
                rvStoryShimmer.visibility = View.INVISIBLE
            }
        }
    }
}