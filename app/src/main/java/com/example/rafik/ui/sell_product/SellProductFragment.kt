package com.example.rafik.ui.sell_product

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.rafik.R
import com.example.rafik.databinding.FragmentSellProductBinding
import com.github.dhaval2404.imagepicker.ImagePicker

class SellProductFragment : Fragment() {
    private lateinit var binding: FragmentSellProductBinding
    private lateinit var viewModel: SellProductViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSellProductBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(
            this,
            SellProductVMFactory(requireContext().applicationContext as Application)
        )[SellProductViewModel::class.java]

        initTargetSpinner()
        binding.viewModel = viewModel
        binding.pickImg.setOnClickListener {
            ImagePicker.with(this)
                .crop()                    //Crop image(Optional), Check Customization for more option
                .compress(1024)            //Final image size will be less than 1 MB(Optional)
                .maxResultSize(
                    1080,
                    1080
                )    //Final image resolution will be less than 1080 x 1080(Optional)
                .start()
        }
        return binding.root
    }


    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (resultCode) {
            Activity.RESULT_OK -> {
                val uri: Uri = data?.data!!
                // Use Uri object instead of File to avoid storage permissions
                binding.pickImg.setImageURI(uri)
            }

            ImagePicker.RESULT_ERROR -> {
                Toast.makeText(requireContext(), ImagePicker.getError(data), Toast.LENGTH_SHORT)
                    .show()
            }

            else -> {
                Toast.makeText(requireContext(), "Task Cancelled", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun initTargetSpinner() {
        val targetList = resources.getStringArray(R.array.target_type)
        val targetAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, targetList)
        binding.targetSpinner.adapter = targetAdapter

        binding.targetSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View,
                    position: Int,
                    id: Long
                ) {
                    Log.i("initAreaSpinner", "area: ${targetList[position]}")

                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    //todo
                }
            }

    }


}