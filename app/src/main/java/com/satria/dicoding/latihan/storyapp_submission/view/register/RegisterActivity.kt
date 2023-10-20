package com.satria.dicoding.latihan.storyapp_submission.view.register

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.satria.dicoding.latihan.storyapp_submission.R
import com.satria.dicoding.latihan.storyapp_submission.data.ResultState
import com.satria.dicoding.latihan.storyapp_submission.data.factory.RegisterViewModelFactory
import com.satria.dicoding.latihan.storyapp_submission.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private val viewModel by viewModels<RegisterViewModel> {
        RegisterViewModelFactory.getInstance()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener { finish() }

        binding.btnRegister.setOnClickListener {
            if (!isInputValid()) {
                showToast("Please fill the form correctly")
                binding.btnRegister.isEnabled = false
                return@setOnClickListener
            }
            register()
        }

        binding.edtName.addTextChangedListener {
            binding.btnRegister.isEnabled = isInputValid()

        }
        binding.edtEmail.addTextChangedListener {
            binding.btnRegister.isEnabled = isInputValid()
        }
        binding.edtPassword.addTextChangedListener {
            binding.btnRegister.isEnabled = isInputValid()
        }
    }

    private fun isInputValid(): Boolean {
        val nameText = binding.edtName.text.toString()
        val nameError = binding.edtName.error
        val mailText = binding.edtEmail.text.toString()
        val mailError = binding.edtEmail.error
        val passText = binding.edtPassword.text.toString()
        val passError = binding.edtPassword.error

        return (nameText.isNotEmpty() && nameError.isNullOrEmpty() && mailText.isNotEmpty() &&
                mailError.isNullOrEmpty() && passText.isNotEmpty() && passError.isNullOrEmpty())
    }

    private fun register() {
        val name = binding.edtName.text.toString().trim()
        val email = binding.edtEmail.text.toString().trim()
        val password = binding.edtPassword.text.toString().trim()

        viewModel.register(name, email, password).observe(this) { state ->
            if (state != null) {
                when (state) {
                    is ResultState.Loading -> {
                        showLoading(true)
                        binding.btnRegister.isEnabled = false
                    }

                    is ResultState.Success -> {
                        showLoading(false)
                        binding.btnRegister.isEnabled = true
                        showToast("Success registering. Please Login")
                        finish()
                    }

                    is ResultState.Error -> {
                        showLoading(false)
                        binding.btnRegister.isEnabled = true
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
        if (isLoading) {
            binding.btnRegister.isEnabled = false
            binding.btnRegister.text = null
            binding.progressCircular.visibility = View.VISIBLE
        } else {
            binding.progressCircular.visibility = View.INVISIBLE
            binding.btnRegister.text = getString(R.string.register)
            binding.btnRegister.isEnabled = true
        }
    }
}