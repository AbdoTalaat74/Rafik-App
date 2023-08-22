package com.example.rafik.ui.training

import android.app.Application
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.rafik.R
import com.example.rafik.databinding.FragmentTrainingBinding
import com.example.rafik.ui.initToolbar

class TrainingFragment : Fragment() {
    private lateinit var binding: FragmentTrainingBinding
    private lateinit var viewModel: TrainingViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentTrainingBinding.inflate(layoutInflater)

        viewModel = ViewModelProvider(
            this,
            TrainingVMFactory(requireContext().applicationContext as Application)
        )[TrainingViewModel::class.java]

        binding.viewModel = viewModel

        initToolbar(binding.topAppBar, getString(R.string.trainings), this)
        initProductTypeSpinner()
        initTrainingPlacesSpinner()

        viewModel.onNavigateUp.observe(viewLifecycleOwner) {
            if (it) {
                findNavController().navigateUp()
            }
        }


        return binding.root
    }


    private fun initProductTypeSpinner() {
        val trainingsType = resources.getStringArray(R.array.product_types)
        val trainingsAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, trainingsType)
        binding.productTypeSpinner.adapter = trainingsAdapter
        binding.productTypeSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View,
                    position: Int,
                    id: Long
                ) {
                    Log.i("initAreaSpinner", "area: ${trainingsType[position]}")
                    viewModel.postProductType(trainingsType[position])
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    //todo
                }
            }
    }

    private fun initTrainingPlacesSpinner(){
        val trainingPlaces = resources.getStringArray(R.array.training_places)
        val placesAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, trainingPlaces)
        binding.trainingPlacesSpinner.adapter = placesAdapter

        binding.trainingPlacesSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View,
                    position: Int,
                    id: Long
                ) {
                    Log.i("initAreaSpinner", "area: ${trainingPlaces[position]}")
                    viewModel.postTrainingPlace(trainingPlaces[position])
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    //todo
                }
            }

    }

}