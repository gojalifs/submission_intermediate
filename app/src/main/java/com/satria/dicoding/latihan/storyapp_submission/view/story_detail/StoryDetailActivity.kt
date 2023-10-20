package com.satria.dicoding.latihan.storyapp_submission.view.story_detail

import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.satria.dicoding.latihan.storyapp_submission.databinding.ActivityStoryDetailBinding
import com.satria.dicoding.latihan.storyapp_submission.model.api_response.ListStoryItem

class StoryDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStoryDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoryDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val storyData = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra(EXTRA_DATA, ListStoryItem::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(EXTRA_DATA)
        }

        Glide.with(applicationContext).load(storyData?.photoUrl).into(binding.imStoryImage)
        binding.tvUserName.text = storyData?.name
        binding.tvDescription.text = storyData?.description

        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    companion object {
        const val EXTRA_DATA = "extra_data"
    }
}