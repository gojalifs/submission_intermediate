package com.satria.dicoding.latihan.storyapp_submission.view.map

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.satria.dicoding.latihan.storyapp_submission.R
import com.satria.dicoding.latihan.storyapp_submission.data.ResultState
import com.satria.dicoding.latihan.storyapp_submission.data.factory.MapViewModelFactory
import com.satria.dicoding.latihan.storyapp_submission.databinding.ActivityMapsBinding

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private val viewModel by viewModels<MapViewModel> {
        MapViewModelFactory.getInstance(applicationContext)
    }
    private val latLngBoundsBuilder = LatLngBounds.builder()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.uiSettings.isZoomControlsEnabled = true

        addMarkers()
    }

    private fun addMarkers() {
        viewModel.getStories().observe(this) { result ->
            when (result) {
                is ResultState.Success -> {
                    showLoading(false)
                    val stories = result.data.listStory
                    stories.forEach { story ->
                        val lat = story.lat?.toDouble() ?: 0.0
                        val long = story.lon?.toDouble() ?: 0.0
                        val latLng = LatLng(lat, long)
                        mMap.addMarker(
                            MarkerOptions()
                                .position(latLng)
                                .title("Story By ${story.name}")
                        )
                        latLngBoundsBuilder.include(latLng)
                    }

                    val bounds = latLngBoundsBuilder.build()
                    mMap.animateCamera(
                        CameraUpdateFactory.newLatLngBounds(
                            bounds,
                            resources.displayMetrics.widthPixels,
                            resources.displayMetrics.heightPixels,
                            300
                        )
                    )
                }

                is ResultState.Loading -> showLoading(true)
                is ResultState.Error -> {
                    showLoading(false)
                    Toast.makeText(this, getString(R.string.error_getting_data), Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        with(binding) {
            if (isLoading) {
                progressIndicator.visibility = View.VISIBLE
            } else {
                progressIndicator.visibility = View.INVISIBLE
            }
        }
    }

}