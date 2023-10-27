package com.satria.dicoding.latihan.storyapp_submission.view.setting

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.satria.dicoding.latihan.storyapp_submission.data.factory.MainViewModelFactory
import com.satria.dicoding.latihan.storyapp_submission.data.prefs.SessionPreferences
import com.satria.dicoding.latihan.storyapp_submission.data.prefs.dataStore
import com.satria.dicoding.latihan.storyapp_submission.databinding.ActivitySettingBinding
import com.satria.dicoding.latihan.storyapp_submission.view.auth.login.LoginActivity
import com.satria.dicoding.latihan.storyapp_submission.view.init.MainViewModel


class SettingActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivitySettingBinding
    private lateinit var sessionViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val preferences = SessionPreferences.getInstance(applicationContext.dataStore)

        sessionViewModel = ViewModelProvider(
            this, MainViewModelFactory(preferences)
        )[MainViewModel::class.java]

        binding.imEn.setOnClickListener(this)
        binding.imId.setOnClickListener(this)
        binding.btnLogout.setOnClickListener(this)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    override fun onClick(p0: View) {
        when (p0) {
            binding.imId -> {}
            binding.imEn -> {}
            binding.btnLogout -> {
                sessionViewModel.deleteToken()

                val intent = Intent(this, LoginActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
                startActivity(intent)
            }
        }
    }
}