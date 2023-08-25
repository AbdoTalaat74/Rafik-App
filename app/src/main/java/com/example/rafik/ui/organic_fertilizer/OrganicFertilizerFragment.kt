package com.example.rafik.ui.organic_fertilizer

import android.app.AlertDialog
import android.app.Application
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.rafik.R
import com.example.rafik.databinding.FragmentOrganicFertilizerBinding
import com.example.rafik.ui.initToolbar

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
        fertilizerTypeFocusListener()
        viewModel = ViewModelProvider(
            this, OrganicFertilizerVMFactory(
                requireContext().applicationContext as Application
            )
        )[OrganicFertilizerViewModel::class.java]

        binding.viewModel = viewModel

        viewModel.dialogMessage.observe(viewLifecycleOwner) { message ->
            if (!(message.isNullOrBlank())) {
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
        val name = binding.acreTextField.text.toString()
        while (name == "") {
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
        val name = binding.caratTextField.text.toString()
        while (name == "") {
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
        val name = binding.cultivationTypeTextField.text.toString()
        while (name == "") {
            return getString(R.string.please_fill_crop_type_field)
        }
        return null
    }

    private fun fertilizerTypeFocusListener() {
        binding.fertilizerTypeTextField.setOnFocusChangeListener { _, focus ->
            if (!focus) {
                binding.fertilizerTypeTextLayout.error = validateCultivationType()
            } else {
                binding.fertilizerTypeTextLayout.error = ""
            }
        }
    }



    private fun validateFertilizerType(): String? {
        val name = binding.fertilizerTypeTextField.text.toString()
        while (name == "") {
            return getString(R.string.please_fill_fertilizer_type_field)
        }
        return null
    }


}