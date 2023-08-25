package com.example.rafik.ui.overview

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import com.example.rafik.R
import com.example.rafik.databinding.FragmentOverviewBinding
import com.example.rafik.ui.initToolbar

class OverviewFragment : Fragment() {
    private lateinit var binding: FragmentOverviewBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentOverviewBinding.inflate(layoutInflater)

        initToolbar(binding.topAppBar,getString(R.string.overview),this)

        initTrainingPlacesSpinner()
        initOrganicProductTypeSpinner()
        initFertilizeTypeSpinner()

        return binding.root
    }


    private fun initTrainingPlacesSpinner() {
        val trainingPlaces = resources.getStringArray(R.array.training_places)
        val placesAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, trainingPlaces)
        binding.trainingSpinner.adapter = placesAdapter
    }


    private fun initOrganicProductTypeSpinner() {
        val organicProductTypes = resources.getStringArray(R.array.product_types)
        val productAdapter =
            ArrayAdapter(requireContext(), R.layout.dropdown_item, organicProductTypes)
        binding.organicProductsSpinner.adapter = productAdapter

    }

    private fun initFertilizeTypeSpinner() {
        val fertilizeTypes = resources.getStringArray(R.array.product_types)
        val productAdapter =
            ArrayAdapter(requireContext(), R.layout.dropdown_item, fertilizeTypes)
        binding.fertilizerSpinner.adapter = productAdapter

    }


}