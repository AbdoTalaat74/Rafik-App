package com.example.rafik.ui.organic_fertilizer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.rafik.R
import com.example.rafik.databinding.FragmentOrganicFertilizerBinding
import com.example.rafik.ui.initToolbar

class OrganicFertilizerFragment:Fragment() {
    private lateinit var binding:FragmentOrganicFertilizerBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentOrganicFertilizerBinding.inflate(layoutInflater)

        initToolbar(binding.topAppBar, getString(R.string.organic_fertilizer), this)
        return binding.root
    }
}