package com.example.rafik.ui.home

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.rafik.R
import com.example.rafik.databinding.FragmentHomeScreenBinding
import com.google.firebase.auth.FirebaseAuth

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeScreenBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeScreenBinding.inflate(layoutInflater)
        binding.contactUsLayout.setOnClickListener {
            val animation = AnimationUtils.loadAnimation(requireContext(), R.anim.click_animation)


            animation.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation?) {
                    // Animation started
                }

                override fun onAnimationEnd(animation: Animation?) {
                    // Animation ended, navigate to the RevenuesAndExpensesFragment
                    openWhatsAppWithChat()
                }

                override fun onAnimationRepeat(animation: Animation?) {
                    // Animation repeated
                }
            })
            it.startAnimation(animation)
        }
//        binding.produceOrganicProductBtn.setOnClickListener {
//
//        }
        binding.organicFertilizerBtn.setOnClickListener {
            this.findNavController().navigate(R.id.organicFertilizerFragment)
        }

        binding.manufactureTrainingBtn.setOnClickListener {
            this.findNavController().navigate(R.id.trainingFragment)
        }

        binding.productForSaleBtn.setOnClickListener {
            this.findNavController().navigate(R.id.sellProductFragment)
        }

        // assign the on menu item click listener
        binding.topAppBar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.setting -> {
                    findNavController().navigate(R.id.action_homeFragment_to_settingsFragment)
                    true
                }

                else -> false
            }
        }

        return binding.root
    }

    private fun openWhatsAppWithChat() {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse("https://api.whatsapp.com/send?phone=201093111955")
        startActivity(intent)
    }
}