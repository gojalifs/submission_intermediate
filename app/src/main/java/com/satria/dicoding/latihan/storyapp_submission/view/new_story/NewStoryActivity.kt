package com.satria.dicoding.latihan.storyapp_submission.view.new_story

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.satria.dicoding.latihan.storyapp_submission.R
import com.satria.dicoding.latihan.storyapp_submission.data.ResultState
import com.satria.dicoding.latihan.storyapp_submission.data.factory.NewStoryViewModelFactory
import com.satria.dicoding.latihan.storyapp_submission.databinding.ActivityNewStoryBinding
import com.satria.dicoding.latihan.storyapp_submission.utils.Utils
import com.satria.dicoding.latihan.storyapp_submission.utils.Utils.reduceFileSize

class NewStoryActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityNewStoryBinding
    private val viewModel by viewModels<NewStoryViewModel> {
        NewStoryViewModelFactory.getInstance(applicationContext)
    }

    private var currentImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.cameraButton.setOnClickListener(this)
        binding.galleryButton.setOnClickListener(this)
        binding.uploadButton.setOnClickListener(this)
    }


    override fun onClick(v: View) {
        when (v) {
            binding.galleryButton -> {
                launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            }

            binding.cameraButton -> {
                if (!grantPermission()) {
                    requestPermissionLauncher.launch(REQUIRED_PERMISSION)
                }
                takeCamera()
            }

            binding.uploadButton -> {
                addStory()
            }
        }
    }

    private fun grantPermission() =
        ContextCompat.checkSelfPermission(
            this,
            REQUIRED_PERMISSION
        ) == PackageManager.PERMISSION_GRANTED

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            Toast.makeText(this, "Permission request granted", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(this, "Permission request denied", Toast.LENGTH_LONG).show()
        }
    }

    private fun addStory() {
        showLoading(true)
        currentImageUri?.let {
            val imageFile = Utils.getFileFromUri(it, this).reduceFileSize()
            val description = binding.edtDescription.text.toString()


            viewModel.addNewStory(imageFile, description).observe(this) { state ->
                if (state != null) {
                    when (state) {
                        is ResultState.Loading -> {}

                        is ResultState.Success -> {
                            showLoading(false)
                            val intent = Intent()
                            intent.putExtra(EXTRA_DATA, true)
                            setResult(RESULT_CODE, intent)
                            finish()
                        }

                        is ResultState.Error -> {
                            showLoading(false)
                        }
                    }
                }
            }
        } ?: {
            showToast("Please select image or take new image")
            showLoading(false)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        with(binding) {
            if (isLoading) {
                progressIndicator.visibility = View.VISIBLE
                uploadButton.isEnabled = false
                uploadButton.text = ""
            } else {
                progressIndicator.visibility = View.INVISIBLE
                uploadButton.isEnabled = true
                uploadButton.text = getString(R.string.add_new_story)
            }
        }
    }

    private fun takeCamera() {
        currentImageUri = Utils.getImageUri(this)
        launchIntentCamera.launch(currentImageUri)
    }

    private val launchIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            showImage()
        }
    }

    private val launcherGallery =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                currentImageUri = uri
                showImage()
            } else {
                showToast("No media selected")
            }
        }

    private fun showImage() {
        currentImageUri?.let {
            binding.previewImageView.setImageURI(it)
        }
    }

    private fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    companion object {
        const val RESULT_CODE = 101
        const val EXTRA_DATA = "extra_data"
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
    }
}
