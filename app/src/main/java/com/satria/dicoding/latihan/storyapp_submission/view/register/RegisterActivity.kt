package com.satria.dicoding.latihan.storyapp_submission.view.register

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.satria.dicoding.latihan.storyapp_submission.data.ResultState
import com.satria.dicoding.latihan.storyapp_submission.data.factory.ViewModelFactory
import com.satria.dicoding.latihan.storyapp_submission.databinding.ActivityRegisterBinding
import com.satria.dicoding.latihan.storyapp_submission.view.home.HomeActivity

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private val viewModel by viewModels<RegisterViewModel> {
        ViewModelFactory.getInstance()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener { finish() }

        binding.btnRegister.setOnClickListener {
            register()
        }

    }

    private fun register() {
        val name = binding.edtName.text.toString().trim()
        val email = binding.edtEmail.text.toString().trim()
        val password = binding.edtPassword.text.toString().trim()

        viewModel.register(name, email, password).observe(this) { state ->
            if (state != null) {
                when (state) {
                    is ResultState.Loading -> showLoading(true)
                    is ResultState.Success -> {
                        showLoading(false)
                        val intent = Intent(this, HomeActivity::class.java).apply {
                            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
                        }
                        startActivity(intent)
                    }

                    is ResultState.Error -> {
                        showLoading(false)
                        showToast(state.error)
                    }
                }
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(isLoading: Boolean) {

    }
}