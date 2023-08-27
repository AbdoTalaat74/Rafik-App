package com.fertilizers.rafik.ui.training

import android.app.AlertDialog
import android.app.Application
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.fertilizers.rafik.R
import com.fertilizers.rafik.databinding.FragmentTrainingBinding
import com.fertilizers.rafik.domian.entity.ProductType
import com.fertilizers.rafik.domian.entity.TrainingArea
import com.fertilizers.rafik.ui.initToolbar
import com.fertilizers.rafik.utils.Constants
import com.google.android.material.snackbar.Snackbar

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

        viewModel.trainingAreas.observe(viewLifecycleOwner){
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

        viewModel.productTypes.observe(viewLifecycleOwner){
            if (it == null) {
                Snackbar.make(
                    binding.coordinatorlayout,
                    getString(R.string.checkYourInternet),
                    Snackbar.LENGTH_LONG
                ).show()
            }else{
                initProductTypeSpinner(it)
            }
        }

        viewModel.sendRequest.observe(viewLifecycleOwner) {
            if (it == true) {
                AlertDialog.Builder(requireContext())
                    .setTitle(getString(R.string.confirmation))
                    .setMessage(resources.getString(R.string.send_request_confirmation))
                    .setPositiveButton(resources.getString(R.string.okay)) { _, _ ->
                        viewModel.sendRequest()
                    }
                    .setNegativeButton(getString(R.string.cancel)) { _, _ ->
                        //do nothing
                    }
                    .setIcon(R.drawable.ic_warning)
                    .show()
            }
        }


        viewModel.navigateUp.observe(viewLifecycleOwner) {
            if (it == true) {
                findNavController().navigateUp()
                viewModel.onNavigateUp()
            }
        }

        viewModel.isSuccessfulRequest.observe(viewLifecycleOwner) {
            when (it) {
                Constants.Request.SUCCESS -> {
                    Snackbar.make(
                        binding.coordinatorlayout,
                        getString(R.string.request_sent_successfully),
                        Snackbar.LENGTH_LONG
                    ).show()
                    Handler(Looper.getMainLooper()).postDelayed({
                        viewModel.setNavigateUp(true)
                    }, 1500)
                }

                Constants.Request.FAILED -> {
                    Snackbar.make(
                        binding.coordinatorlayout,
                        getString(R.string.your_request_was_not_sent_please_try_again_later),
                        Snackbar.LENGTH_LONG
                    ).show()
                }

                else -> {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.your_request_was_not_sent_please_try_again_later),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }



        return binding.root
    }

    private fun initProductTypeSpinner(data: List<ProductType>) {
        val productAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, data)
        binding.productTypeSpinner.adapter = productAdapter
        binding.productTypeSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    viewModel.postProductType(data[position].name)
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    //todo
                }
            }


    }

    private fun initTrainingPlacesSpinner(data: List<TrainingArea>) {
        val placesAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, data)
        binding.trainingPlacesSpinner.adapter = placesAdapter

        binding.trainingPlacesSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    viewModel.postTrainingPlace(data[position].name)
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    //todo
                }
            }

    }

}