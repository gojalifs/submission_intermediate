package com.satria.dicoding.latihan.storyapp_submission.view.story_detail

import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.satria.dicoding.latihan.storyapp_submission.R
import com.satria.dicoding.latihan.storyapp_submission.databinding.ActivityStoryDetailBinding
import com.satria.dicoding.latihan.storyapp_submission.model.api_response.ListStoryItem

class StoryDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStoryDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoryDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.customAppBar.customAppBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.round_arrow_back_ios_new_24)

        val storyData = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra(EXTRA_DATA, ListStoryItem::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(EXTRA_DATA)
        }

        Glide.with(applicationContext).load(storyData?.photoUrl).into(binding.imStoryImage)
        binding.tvUserName.text = storyData?.name
        binding.tvDescription.text = storyData?.description

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    companion object {
        const val EXTRA_DATA = "extra_data"
    }
}