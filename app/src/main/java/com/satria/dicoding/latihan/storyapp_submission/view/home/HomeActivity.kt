package com.satria.dicoding.latihan.storyapp_submission.view.home

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.satria.dicoding.latihan.storyapp_submission.R
import com.satria.dicoding.latihan.storyapp_submission.data.factory.HomeViewModelFactory
import com.satria.dicoding.latihan.storyapp_submission.databinding.ActivityHomeBinding
import com.satria.dicoding.latihan.storyapp_submission.view.map.MapsActivity
import com.satria.dicoding.latihan.storyapp_submission.view.new_story.NewStoryActivity
import com.satria.dicoding.latihan.storyapp_submission.view.setting.SettingActivity

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private val viewModel by viewModels<HomeViewModel> {
        HomeViewModelFactory.getInstance(applicationContext)
    }

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
                R.id.menu_setting -> {
                    val intent = Intent(this, SettingActivity::class.java)
                    startActivity(intent)
                    true
                }

                R.id.menu_map -> {
                    val intent = Intent(this, MapsActivity::class.java)
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

    override fun onRestoreInstanceState(
        savedInstanceState: Bundle?,
        persistentState: PersistableBundle?
    ) {
        AppCompatDelegate.getApplicationLocales()

        super.onRestoreInstanceState(savedInstanceState, persistentState)
    }

    private fun getStories() {
        val adapter = StoryAdapter()
        binding.rvStory.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter { adapter.retry() }
        )

        viewModel.getStories().observe(this) { resultState ->
            adapter.submitData(lifecycle, resultState)
        }
        binding.swipeRefresh.visibility = View.VISIBLE
        binding.rvStoryShimmer.visibility = View.GONE
    }
}