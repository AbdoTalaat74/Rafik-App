package com.example.rafik.ui

import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import com.example.rafik.R
import com.example.rafik.databinding.ActivityMainBinding
import com.example.rafik.viewModel.InitViewModel
import com.google.android.material.snackbar.Snackbar


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val initViewModel: InitViewModel by viewModels()
    private var wasFalse = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val connectivityManager = getSystemService(ConnectivityManager::class.java) as ConnectivityManager
        connectivityManager.requestNetwork(initViewModel.networkRequest, initViewModel.networkCallback)
        initViewModel.internetStatus(initViewModel.isInternetAvailable(this))
        initViewModel.isInternetAvailable.observe(this) { isInternetAvailable ->
            when (isInternetAvailable) {
                true -> {
                    // Internet is available, perform your tasks here
                    hideProgressBar()
                    if (!wasFalse) { Snackbar.make(binding.frameLayout, resources.getString(R.string.yourInternetIsBack), Snackbar.LENGTH_SHORT).show() }
                    Log.i("MainActivity", "Internet is available")
                    wasFalse = true
                }

                false -> {
                    wasFalse = false
                    // No internet connection, handle the scenario accordingly
                    showProgressBar()
                    Snackbar.make(binding.frameLayout, resources.getString(R.string.checkYourInternet), Snackbar.LENGTH_SHORT).show()
                    Log.i("MainActivity", " No internet connection")
                }
            }
        }
    }

    private fun showProgressBar() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        binding.progressBar.visibility = View.GONE
    }
}