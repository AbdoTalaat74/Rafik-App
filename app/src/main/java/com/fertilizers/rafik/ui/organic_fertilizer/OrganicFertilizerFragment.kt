package com.fertilizers.rafik.ui.organic_fertilizer

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
import com.fertilizers.rafik.databinding.FragmentOrganicFertilizerBinding
import com.fertilizers.rafik.domian.entity.FertilizerType
import com.fertilizers.rafik.ui.initToolbar
import com.fertilizers.rafik.utils.Constants
import com.google.android.material.snackbar.Snackbar

class OrganicFertilizerFragment : Fragment() {
    private lateinit var binding: FragmentOrganicFertilizerBinding
    private lateinit var viewModel: OrganicFertilizerViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOrganicFertilizerBinding.inflate(layoutInflater)
        initToolbar(binding.topAppBar, getString(R.string.organic_fertilizer), this)

        acreFocusListener()
        caratFocusListener()
        cultivationFocusListener()
        viewModel = ViewModelProvider(
            this, OrganicFertilizerVMFactory(
                requireContext().applicationContext as Application
            )
        )[OrganicFertilizerViewModel::class.java]

        binding.viewModel = viewModel

        viewModel.dialogMessage.observe(viewLifecycleOwner) { message ->
            if (!(message.isNullOrBlank())) {
                submitForm()
                AlertDialog.Builder(requireContext())
                    .setTitle(getString(R.string.error))
                    .setMessage(message)
                    .setPositiveButton(getString(R.string.okay)) { _, _ ->
                        // do nothing
                    }
                    .setIcon(R.drawable.ic_warning)
                    .show()
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

                viewModel.navigateUp
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
                        viewModel.setNavigate(true)
                    }, 1500)
                }

                Constants.Request.FAILED -> {
                    Snackbar.make(
                        requireView(),
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

        viewModel.fertilizerTypes.observe(viewLifecycleOwner){

            if (it == null) {
                Snackbar.make(
                    binding.coordinatorlayout,
                    getString(R.string.checkYourInternet),
                    Snackbar.LENGTH_LONG
                ).show()
            }else{
                initFertilizerSpinner(it)
            }

        }
        return binding.root
    }


    private fun acreFocusListener() {
        binding.acreTextField.setOnFocusChangeListener { _, focus ->
            if (!focus) {
                binding.acreTextLayout.error = validateAcre()
            } else {
                binding.acreTextLayout.error = ""
            }
        }
    }


    private fun validateAcre(): String? {
        val acre = binding.acreTextField.text.toString()
        while (acre.isNullOrBlank()) {
            return getString(R.string.this_field_cannot_be_empty)
        }
        return null
    }

    private fun caratFocusListener() {
        binding.caratTextField.setOnFocusChangeListener { _, focus ->
            if (!focus) {
                binding.caratTextLayout.error = validateCarat()
            } else {
                binding.caratTextLayout.error = ""
            }
        }
    }


    private fun validateCarat(): String? {
        val carat = binding.caratTextField.text.toString()
        while (carat.isNullOrBlank()) {
            return getString(R.string.this_field_cannot_be_empty)
        }
        return null
    }

    private fun cultivationFocusListener() {
        binding.cultivationTypeTextField.setOnFocusChangeListener { _, focus ->
            if (!focus) {
                binding.cultivationTypeTextLayout.error = validateCultivationType()
            } else {
                binding.cultivationTypeTextLayout.error = ""
            }
        }
    }


    private fun validateCultivationType(): String? {
        val cultivationType = binding.cultivationTypeTextField.text.toString()
        while (cultivationType.isNullOrBlank()) {
            return getString(R.string.please_fill_crop_type_field)
        }
        return null
    }


    private fun initFertilizerSpinner(data:List<FertilizerType>) {
        val fertilizerTypesAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, data)
        binding.fertilizerTypeSpinner.adapter = fertilizerTypesAdapter


        binding.fertilizerTypeSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>, view: View?, position: Int, id: Long
                ) {
                    viewModel.postFertilizer(data[position].name)
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    //todo
                }

            }

    }


    private fun submitForm() {
        binding.acreTextLayout.error = validateAcre()
        binding.caratTextLayout.error = validateCarat()
        binding.cultivationTypeTextLayout.error = validateCultivationType()
    }


}