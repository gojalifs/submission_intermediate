package com.satria.dicoding.latihan.storyapp_submission.view.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.satria.dicoding.latihan.storyapp_submission.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}