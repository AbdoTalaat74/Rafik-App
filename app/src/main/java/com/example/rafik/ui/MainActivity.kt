package com.example.rafik.ui

import android.content.res.Configuration
import android.content.res.Resources
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.rafik.R
import com.example.rafik.databinding.ActivityMainBinding
import com.example.rafik.ui.settings.DarkModePrefManager
import com.example.rafik.ui.settings.LanguagePrefManger
import com.example.rafik.viewModel.InitViewModel
import com.google.android.material.snackbar.Snackbar
import java.util.Locale


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val initViewModel: InitViewModel by viewModels()
    private var wasFalse = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (DarkModePrefManager(applicationContext).isNightMode()) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
        val myLocale = Locale(LanguagePrefManger(this).getLanguage())
        val res: Resources = resources
        val conf: Configuration = res.configuration
        conf.setLocale(myLocale)
        Locale.setDefault(myLocale)
        conf.setLayoutDirection(myLocale)
        res.updateConfiguration(conf, res.displayMetrics)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val connectivityManager =
            getSystemService(ConnectivityManager::class.java) as ConnectivityManager
        connectivityManager.requestNetwork(
            initViewModel.networkRequest, initViewModel.networkCallback
        )
        initViewModel.internetStatus(initViewModel.isInternetAvailable(this))
        initViewModel.isInternetAvailable.observe(this) { isInternetAvailable ->
            when (isInternetAvailable) {
                true -> {
                    // Internet is available, perform your tasks here
                    hideProgressBar()
                    if (!wasFalse) {
                        Snackbar.make(
                            binding.frameLayout,
                            resources.getString(R.string.yourInternetIsBack), Snackbar.LENGTH_SHORT
                        ).show()
                    }
                    Log.i("MainActivity", "Internet is available")
                    wasFalse = true
                }

                false -> {
                    wasFalse = false
                    // No internet connection, handle the scenario accordingly
                    showProgressBar()
                    Snackbar.make(
                        binding.frameLayout,
                        resources.getString(R.string.checkYourInternet), Snackbar.LENGTH_SHORT
                    ).show()
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