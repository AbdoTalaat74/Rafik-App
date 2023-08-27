package com.fertilizers.rafik.ui.overview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.fertilizers.rafik.R
import com.fertilizers.rafik.databinding.FragmentOverviewBinding
import com.fertilizers.rafik.domian.entity.CropType
import com.fertilizers.rafik.domian.entity.FertilizerType
import com.fertilizers.rafik.domian.entity.TrainingArea
import com.google.android.material.snackbar.Snackbar

class OverviewFragment : Fragment() {
    private lateinit var binding: FragmentOverviewBinding
    private lateinit var viewModel: OverviewViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentOverviewBinding.inflate(layoutInflater)

        viewModel = ViewModelProvider(this)[OverviewViewModel::class.java]
        initOverviewToolBar()


        viewModel.fertilizerTypes.observe(viewLifecycleOwner) {
            if (it == null) {
                Snackbar.make(
                    binding.coordinatorlayout,
                    getString(R.string.checkYourInternet),
                    Snackbar.LENGTH_LONG
                ).show()
            }else{
                initFertilizeTypeSpinner(it)
            }
        }

        viewModel.cropType.observe(viewLifecycleOwner) {
            if (it == null) {
                Snackbar.make(
                    binding.coordinatorlayout,
                    getString(R.string.checkYourInternet),
                    Snackbar.LENGTH_LONG
                ).show()
            }else{
                initOrganicProductTypeSpinner(it)
            }
        }

        viewModel.trainingAreas.observe(viewLifecycleOwner) {
            if (it == null) {
                Snackbar.make(
                    binding.coordinatorlayout,
                    getString(R.string.checkYourInternet),
                    Snackbar.LENGTH_LONG
                ).show()
            }else{
                initTrainingPlacesSpinner(it)
            }
        }







        return binding.root
    }


    private fun initTrainingPlacesSpinner(data: List<TrainingArea>) {
        val placesAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, data)
        binding.trainingSpinner.adapter = placesAdapter
    }


    private fun initOrganicProductTypeSpinner(data:List<CropType>) {
        val productAdapter =
            ArrayAdapter(requireContext(), R.layout.dropdown_item, data)
        binding.organicProductsSpinner.adapter = productAdapter

    }

    private fun initFertilizeTypeSpinner(data:List<FertilizerType>) {
        val productAdapter =
            ArrayAdapter(requireContext(), R.layout.dropdown_item, data)
        binding.fertilizerSpinner.adapter = productAdapter

    }


    private fun initOverviewToolBar(
    ) {
        binding.topAppBar.setNavigationIcon(R.drawable.ic_back_arrow)
        binding.topAppBar.title = getString(R.string.overview)
        binding.topAppBar.setTitleTextColor(this.resources.getColor(R.color.base_green_color))
        binding.topAppBar.isTitleCentered = true
        binding.topAppBar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }


}