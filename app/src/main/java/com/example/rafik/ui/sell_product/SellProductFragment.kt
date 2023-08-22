package com.example.rafik.ui.sell_product

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
import com.example.rafik.R
import com.example.rafik.databinding.FragmentSellProductBinding

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

        return binding.root
    }



    private fun initTargetSpinner(){
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