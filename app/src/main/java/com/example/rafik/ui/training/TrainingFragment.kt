package com.example.rafik.ui.training

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import com.example.rafik.R
import com.example.rafik.databinding.FragmentTrainingBinding
import com.example.rafik.ui.initToolbar

class TrainingFragment: Fragment() {
    private lateinit var binding:FragmentTrainingBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentTrainingBinding.inflate(layoutInflater)

        initToolbar(binding.topAppBar,getString(R.string.trainings),this)

        val trainingsType = resources.getStringArray(R.array.product_types)
        val trainingsAdapter = ArrayAdapter(requireContext(),R.layout.dropdown_item,trainingsType)

        val trainingPlaces = resources.getStringArray(R.array.training_places)
        val placesAdapter = ArrayAdapter(requireContext(),R.layout.dropdown_item,trainingPlaces)

        binding.productTypeSpinner.adapter = trainingsAdapter

        binding.trainingPlacesSpinner.adapter = placesAdapter

        return binding.root
    }
}