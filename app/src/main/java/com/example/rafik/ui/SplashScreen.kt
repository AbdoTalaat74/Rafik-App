package com.example.rafik.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.rafik.R
import com.example.rafik.databinding.FragmentSplashScreenBinding
import com.example.rafik.ui.settings.DarkModePrefManager
import com.google.firebase.auth.FirebaseAuth

@SuppressLint("CustomSplashScreen")
class SplashScreen : Fragment() {
    private lateinit var binding: FragmentSplashScreenBinding
    private val mAuth = FirebaseAuth.getInstance()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (DarkModePrefManager(requireContext()).isNightMode()) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
        binding = FragmentSplashScreenBinding.inflate(layoutInflater)
        val mAuthListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            if (firebaseAuth.currentUser != null) {
                navigateToHomeFragment()
            }
        }
        if (mAuth.currentUser != null) {
            mAuth.addAuthStateListener(mAuthListener)
        } else {
            navigateToRegisterFragment()
        }
        return binding.root
    }


    private fun navigateToHomeFragment() {
        Handler(Looper.getMainLooper()).postDelayed({
            Navigation.findNavController(requireView())
                .navigate(R.id.action_splashScreen_to_homeFragment)
        }, 2000)
    }

    private fun navigateToRegisterFragment() {
        Handler(Looper.getMainLooper()).postDelayed({
            Navigation.findNavController(requireView())
                .navigate(R.id.action_splashScreen_to_signInFragment)
        }, 2000)
    }

}