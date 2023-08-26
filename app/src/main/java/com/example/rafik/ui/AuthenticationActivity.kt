package com.example.rafik.ui

import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import com.example.rafik.R
import com.example.rafik.databinding.ActivityAuthenticationBinding
import com.example.rafik.ui.settings.DarkModePrefManager
import com.example.rafik.ui.settings.LanguagePrefManger
import com.example.rafik.viewModel.InitViewModel
import com.example.rafik.viewModel.LoginViewModel
import com.google.android.material.snackbar.Snackbar
import java.util.Locale



@Suppress("DEPRECATION")
class AuthenticationActivity : AppCompatActivity() {
    private val TAG = "AuthenticationActivity"

    private lateinit var binding: ActivityAuthenticationBinding
    private val loginViewModel by viewModels<LoginViewModel>()
    private val initViewModel: InitViewModel by viewModels()
    private lateinit var intent: Intent

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
        binding = ActivityAuthenticationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val connectivityManager =
            getSystemService(ConnectivityManager::class.java) as ConnectivityManager
        connectivityManager.requestNetwork(
            initViewModel.networkRequest,
            initViewModel.networkCallback
        )
        initViewModel.internetStatus(initViewModel.isInternetAvailable(this))

        intent = Intent(this, MainActivity::class.java)
        initViewModel.isInternetAvailable.observe(this) { isInternetAvailable ->
            when (isInternetAvailable) {
                true -> {
                    // Internet is available, perform your tasks here
                    Log.i("AuthenticationActivity", "Internet is available")
                }

                false -> {
                    // No internet connection, handle the scenario accordingly
                    Snackbar.make(
                        binding.coordinatorLayout,
                        resources.getString(R.string.checkYourInternet),
                        Snackbar.LENGTH_SHORT
                    ).show()
                    Log.i("AuthenticationActivity", " No internet connection")
                }
            }
        }

        loginViewModel.authenticationState.observe(this) {
            when (it) {
                LoginViewModel.AuthenticationState.AUTHENTICATED -> {
                    startActivity(intent)
                    finish()
                }

                LoginViewModel.AuthenticationState.UNAUTHENTICATED -> Log.i(TAG, "UNAUTHENTICATED")
                LoginViewModel.AuthenticationState.INVALID_AUTHENTICATION -> Log.i(
                    TAG,
                    "INVALID_AUTHENTICATION"
                )

                null -> Log.i(TAG, "INVALID_AUTHENTICATION")
            }
        }
    }
}