package com.example.rafik.ui.organic_fertilizer

import android.app.AlertDialog
import android.app.Application
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
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

        viewModel = ViewModelProvider(
            this, OrganicFertilizerVMFactory(
                requireContext().applicationContext as Application
            )
        )[OrganicFertilizerViewModel::class.java]

        binding.viewModel = viewModel


//        binding.sendRequestBtn.setOnClickListener {
//            viewModel.invalidForm()
//        }

        viewModel.dialogMessage.observe(viewLifecycleOwner){message ->
            if (!(message.isNullOrBlank())){
                AlertDialog.Builder(requireContext())
                    .setTitle(getString(R.string.error))
                    .setMessage(message)
                    .setPositiveButton(getString(R.string.okay)){ _, _ ->
                        // do nothing
                    }
                    .setIcon(R.drawable.ic_warning)
                    .show()
            }

        }


        return binding.root
    }
}