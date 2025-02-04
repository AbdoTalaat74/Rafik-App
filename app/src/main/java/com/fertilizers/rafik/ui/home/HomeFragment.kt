package com.fertilizers.rafik.ui.home

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.fertilizers.rafik.R
import com.fertilizers.rafik.databinding.FragmentHomeScreenBinding

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeScreenBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeScreenBinding.inflate(layoutInflater)
        initHomeToolBar()
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

        binding.organicFertilizerBtn.setOnClickListener {
            this.findNavController()
                .navigate(HomeFragmentDirections.actionHomeFragmentToOrganicFertilizerFragment())
        }
        binding.manufactureTrainingBtn.setOnClickListener {
            this.findNavController()
                .navigate(HomeFragmentDirections.actionHomeFragmentToTrainingFragment())
        }
        binding.productForSaleBtn.setOnClickListener {
            this.findNavController()
                .navigate(HomeFragmentDirections.actionHomeFragmentToSellProductFragment())
        }

        // assign the on menu item click listener
        binding.topAppBar.setOnMenuItemClickListener {
            when (it.itemId) {

                R.id.rafik -> {
                    findNavController().navigate(R.id.action_homeFragment_to_overviewFragment)
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

    private fun initHomeToolBar(
    ) {
        binding.topAppBar.setNavigationIcon(R.drawable.ic_settings2)
        binding.topAppBar.title = getString(R.string.home_screen)
        binding.topAppBar.setTitleTextColor(this.resources.getColor(R.color.base_green_color))
        binding.topAppBar.isTitleCentered = true
        binding.topAppBar.setNavigationOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_settingsFragment)
        }
    }
}