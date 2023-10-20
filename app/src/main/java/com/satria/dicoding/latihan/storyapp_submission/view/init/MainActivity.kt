package com.satria.dicoding.latihan.storyapp_submission.view.init

import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NO_HISTORY
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.satria.dicoding.latihan.storyapp_submission.data.factory.MainViewModelFactory
import com.satria.dicoding.latihan.storyapp_submission.data.prefs.SessionPreferences
import com.satria.dicoding.latihan.storyapp_submission.data.prefs.dataStore
import com.satria.dicoding.latihan.storyapp_submission.databinding.ActivityMainBinding
import com.satria.dicoding.latihan.storyapp_submission.view.home.HomeActivity
import com.satria.dicoding.latihan.storyapp_submission.view.login.LoginActivity


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val preferences = SessionPreferences.getInstance(applicationContext.dataStore)
        val sessionViewModel by viewModels<MainViewModel> {
            MainViewModelFactory(preferences)
        }

        sessionViewModel.getToken().observe(this) {
            if (it != null) {
                val intent = Intent(this, HomeActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                intent.addFlags(FLAG_ACTIVITY_NO_HISTORY)
                startActivity(intent)
            } else {
                val intent = Intent(this, LoginActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                }
                startActivity(intent)
            }
        }
    }
}