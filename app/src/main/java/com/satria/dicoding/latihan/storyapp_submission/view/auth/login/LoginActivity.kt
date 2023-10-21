package com.satria.dicoding.latihan.storyapp_submission.view.auth.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import com.satria.dicoding.latihan.storyapp_submission.R
import com.satria.dicoding.latihan.storyapp_submission.data.ResultState
import com.satria.dicoding.latihan.storyapp_submission.data.factory.AuthViewModelFactory
import com.satria.dicoding.latihan.storyapp_submission.data.factory.MainViewModelFactory
import com.satria.dicoding.latihan.storyapp_submission.data.prefs.SessionPreferences
import com.satria.dicoding.latihan.storyapp_submission.data.prefs.dataStore
import com.satria.dicoding.latihan.storyapp_submission.databinding.ActivityLoginBinding
import com.satria.dicoding.latihan.storyapp_submission.view.auth.AuthViewModel
import com.satria.dicoding.latihan.storyapp_submission.view.auth.register.RegisterActivity
import com.satria.dicoding.latihan.storyapp_submission.view.home.HomeActivity
import com.satria.dicoding.latihan.storyapp_submission.view.init.MainViewModel

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val viewModel by viewModels<AuthViewModel> {
        AuthViewModelFactory.getInstance()
    }
    private lateinit var sessionViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val preferences = SessionPreferences.getInstance(applicationContext.dataStore)

        sessionViewModel = ViewModelProvider(
            this,
            MainViewModelFactory(preferences)
        )[MainViewModel::class.java]

        binding.btnLogin.setOnClickListener {
            if (!isInputValid()) {
                showToast("Please fill the form correctly")
                binding.btnLogin.isEnabled = false
                return@setOnClickListener
            }
            login()

        }

        binding.btnRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        binding.edtEmail.addTextChangedListener {
            binding.btnLogin.isEnabled = isInputValid()
        }
        binding.edtPassword.addTextChangedListener {
            binding.btnLogin.isEnabled = isInputValid()
        }

    }

    private fun login() {
        val email = binding.edtEmail.text.toString().trim()
        val password = binding.edtPassword.text.toString().trim()

        viewModel.login(email, password).observe(this) { state ->
            if (state != null) {
                when (state) {
                    is ResultState.Loading -> {
                        showLoading(true)
                        binding.btnLogin.isEnabled = false
                    }

                    is ResultState.Success -> {
                        showLoading(false)
                        binding.btnLogin.isEnabled = true
                        if (state.data.loginResult?.token != null) {
                            sessionViewModel.saveToken(state.data.loginResult.token)
                        }

                        sessionViewModel.getToken()
                        showToast("Login Success")
                        val intent = Intent(this, HomeActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                    }

                    is ResultState.Error -> {
                        showLoading(false)
                        binding.btnLogin.isEnabled = true
                        showToast(state.error)
                    }
                }
            }
        }
    }

    private fun isInputValid(): Boolean {
        val mailText = binding.edtEmail.text.toString()
        val mailError = binding.edtEmail.error
        val passText = binding.edtPassword.text.toString()
        val passError = binding.edtPassword.error

        return (mailText.isNotEmpty() && mailError.isNullOrEmpty() && passText.isNotEmpty() && passError.isNullOrEmpty())
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.btnLogin.isEnabled = false
            binding.btnLogin.text = null
            binding.progressCircular.visibility = View.VISIBLE
        } else {
            binding.progressCircular.visibility = View.INVISIBLE
            binding.btnLogin.text = getString(R.string.register)
            binding.btnLogin.isEnabled = true
        }
    }
}