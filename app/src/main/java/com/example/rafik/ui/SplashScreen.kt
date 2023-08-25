package com.example.rafik.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import com.example.rafik.databinding.ActivitySplashScreenBinding
import com.example.rafik.ui.settings.DarkModePrefManager
import com.example.rafik.ui.settings.LanguagePrefManger
import com.google.firebase.auth.FirebaseAuth
import java.util.Locale

@Suppress("DEPRECATION")
@SuppressLint("CustomSplashScreen")
class SplashScreen : AppCompatActivity() {
    private lateinit var binding: ActivitySplashScreenBinding
    private val mAuth = FirebaseAuth.getInstance()
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
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (mAuth.currentUser != null) {
            Log.i("SplashScreen", "intent to MainActivity")
            intent =
                Intent(this, MainActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            start()
        } else {
            Log.i("SplashScreen", "intent to AuthenticationActivity")
            intent = Intent(
                this,
                AuthenticationActivity::class.java
            ).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            start()
        }
    }

    private fun start() {
        Log.i("SplashScreen", "start called")
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(intent)
            finish()
        }, 1500) // 1500 is the delayed time in milliseconds.
    }
}