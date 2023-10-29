package com.satria.dicoding.latihan.storyapp_submission.view.new_story

import android.Manifest
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.CompoundButton
import android.widget.Toast
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.OnTokenCanceledListener
import com.satria.dicoding.latihan.storyapp_submission.R
import com.satria.dicoding.latihan.storyapp_submission.data.ResultState
import com.satria.dicoding.latihan.storyapp_submission.data.factory.NewStoryViewModelFactory
import com.satria.dicoding.latihan.storyapp_submission.databinding.ActivityNewStoryBinding
import com.satria.dicoding.latihan.storyapp_submission.utils.Utils
import com.satria.dicoding.latihan.storyapp_submission.utils.Utils.reduceFileSize
import java.util.concurrent.TimeUnit

class NewStoryActivity : AppCompatActivity(), View.OnClickListener,
    CompoundButton.OnCheckedChangeListener {
    private lateinit var binding: ActivityNewStoryBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    private var lon: Double? = null
    private var lat: Double? = null
    private var isLocationAdded = false

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
        binding.checkAddMyLocation.setOnCheckedChangeListener(this)

        binding.edtDescription.addTextChangedListener {
            binding.uploadButton.isEnabled = !it.isNullOrEmpty()
        }

        createLocationCallback()

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
    }

    override fun onPause() {
        super.onPause()
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    override fun onClick(v: View) {
        when (v) {
            binding.galleryButton -> {
                launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            }

            binding.cameraButton -> {
                if (!checkPermission(CAMERA_PERMISSION)) {
                    requestPermissionLauncher.launch(arrayOf(CAMERA_PERMISSION))
                } else {
                    takeCamera()
                }
            }

            binding.uploadButton -> {
                if (currentImageUri == null) {
                    showToast(getString(R.string.please_add_image))
                    return
                }
                if (binding.edtDescription.text.isNullOrEmpty()) {
                    showToast(getString(R.string.please_add_description))
                    return
                }
                if (isLocationAdded) {
                    if (lat != null && lon != null) {
                        addStory()
                    } else {
                        showToast(getString(R.string.waiting_for_your_location_data_retrieved))
                    }
                } else {
                    addStory()
                }
            }
        }
    }

    override fun onCheckedChanged(p0: CompoundButton?, isChecked: Boolean) {
        if (isChecked) {
            getMyLocation()
            createLocationRequest()
            isLocationAdded = true
            if (lat == null || lon == null) {
                binding.uploadButton.text = getString(R.string.getting_your_location)
                binding.uploadButton.isEnabled = false
            }
        } else {
            isLocationAdded = false
            with(binding) {
                checkAddMyLocation.isChecked = false
                uploadButton.text = getString(R.string.upload_story)
                if (edtDescription.text.isNotEmpty() && currentImageUri != null) {
                    uploadButton.isEnabled = true
                }
            }
        }
    }

    private fun createLocationCallback() {
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {}
        }
    }

    private fun getMyLocation() {
        if (checkPermission(FINE_LOCATION_PERMISSION) && checkPermission(COARSE_LOCATION_PERMISSION)) {
            fusedLocationClient.getCurrentLocation(
                Priority.PRIORITY_HIGH_ACCURACY,
                object : CancellationToken() {
                    override fun onCanceledRequested(p0: OnTokenCanceledListener) =
                        CancellationTokenSource().token

                    override fun isCancellationRequested() = false
                })
                .addOnSuccessListener { location: Location? ->
                    if (location == null)
                        showToast(getString(R.string.cannot_get_location))
                    else {
                        lat = location.latitude
                        lon = location.longitude
                        showToast(getString(R.string.location_retrieved))
                        with(binding) {
                            uploadButton.text = getString(R.string.upload_story)
                            if (edtDescription.text.isNotEmpty() && currentImageUri != null) {
                                uploadButton.isEnabled = true
                            }
                        }
                    }
                }

        } else {
            binding.checkAddMyLocation.isChecked = false
            requestPermissionLauncher.launch(arrayOf(FINE_LOCATION_PERMISSION))
        }
    }

    private fun checkPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            this, permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions[FINE_LOCATION_PERMISSION] ?: false -> {
                getMyLocation()
            }

            permissions[COARSE_LOCATION_PERMISSION] ?: false -> {
                getMyLocation()
            }

            permissions[CAMERA_PERMISSION] ?: false -> {
                takeCamera()
            }
        }
    }

    private val resolutionLauncher =
        registerForActivityResult(
            ActivityResultContracts.StartIntentSenderForResult()
        ) { result ->
            when (result.resultCode) {
                RESULT_OK ->
                    Log.i(
                        "New Story Activity",
                        "onActivityResult: All location settings are satisfied."
                    )

                RESULT_CANCELED ->
                    showToast(getString(R.string.must_activate_gps))
            }
        }

    private fun createLocationRequest() {
        locationRequest = LocationRequest.Builder(TimeUnit.SECONDS.toMillis(1))
            .setMaxUpdateDelayMillis(TimeUnit.SECONDS.toMillis(1))
            .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
            .build()

        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
        val client = LocationServices.getSettingsClient(this)
        client.checkLocationSettings(builder.build())
            .addOnSuccessListener { showToast(getString(R.string.getting_your_location)) }
            .addOnFailureListener { exception ->
                if (exception is ResolvableApiException) {
                    try {
                        resolutionLauncher.launch(
                            IntentSenderRequest.Builder(exception.resolution).build()
                        )
                    } catch (sendEx: IntentSender.SendIntentException) {
                        showToast(sendEx.message.toString())
                    }
                }
            }
    }

    private fun addStory() {
        showLoading(true)
        currentImageUri?.let {
            val imageFile = Utils.getFileFromUri(it, this).reduceFileSize()
            val description = binding.edtDescription.text.toString()
            viewModel.addNewStory(imageFile, description, lat, lon).observe(this) { state ->
                if (state != null) {
                    when (state) {
                        is ResultState.Loading -> {
                            showLoading(true)
                        }

                        is ResultState.Success -> {
                            showLoading(false)
                            val intent = Intent()
                            intent.putExtra(EXTRA_DATA, true)
                            setResult(RESULT_CODE, intent)
                            finish()
                        }

                        is ResultState.Error -> {
                            showLoading(false)
                            showToast(state.error)
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
        private const val CAMERA_PERMISSION = Manifest.permission.CAMERA
        private const val FINE_LOCATION_PERMISSION = Manifest.permission.ACCESS_FINE_LOCATION
        private const val COARSE_LOCATION_PERMISSION = Manifest.permission.ACCESS_COARSE_LOCATION
    }
}
