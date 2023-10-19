package com.satria.dicoding.latihan.storyapp_submission

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.satria.dicoding.latihan.storyapp_submission.databinding.ActivityMainBinding
import com.satria.dicoding.latihan.storyapp_submission.view.login.LoginActivity

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.mybutton.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }
}