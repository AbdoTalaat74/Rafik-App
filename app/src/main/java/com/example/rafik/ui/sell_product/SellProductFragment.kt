package com.example.rafik.ui.sell_product

import android.app.Activity
import android.app.AlertDialog
import android.app.Application
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.rafik.R
import com.example.rafik.databinding.FragmentSellProductBinding
import com.example.rafik.ui.initToolbar
import com.github.dhaval2404.imagepicker.ImagePicker

class SellProductFragment : Fragment() {
    private lateinit var binding: FragmentSellProductBinding
    private lateinit var viewModel: SellProductViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentSellProductBinding.inflate(layoutInflater)
        initToolbar(binding.topAppBar, getString(R.string.product_for_sale), this)
        viewModel = ViewModelProvider(
            this, SellProductVMFactory(requireContext().applicationContext as Application)
        )[SellProductViewModel::class.java]
        initTargetSpinner()
        binding.viewModel = viewModel
        binding.pickImg.setOnClickListener {
            ImagePicker.with(this)
                .compress(1024)//Final image size will be less than 1 MB(Optional)
                .maxResultSize(
                    1080, 1080
                )//Final image resolution will be less than 1080 x 1080(Optional)
                .createIntent { intent ->
                    viewModel.postImageProgressBarState(true)
                    startForProfileImageResult.launch(intent)
                }
        }

        viewModel.imageProgressBar.observe(viewLifecycleOwner) {
            if (it == true) {
                binding.pickImg.visibility = GONE
                binding.imageProgressBar.visibility = VISIBLE
                binding.imageProgressBar.isActivated = it
            } else {
                binding.imageProgressBar.visibility = GONE
                binding.imageProgressBar.isActivated = it
            }
        }

        viewModel.imageUri.observe(viewLifecycleOwner) {
            if (it != null) {
                binding.productImage.visibility = VISIBLE
                binding.deleteImage.visibility = VISIBLE
                binding.imageProgressBar.visibility = GONE
                binding.pickImg.visibility = GONE
            } else {
                binding.productImage.visibility = GONE
                binding.deleteImage.visibility = GONE
                binding.pickImg.visibility = VISIBLE
            }
        }


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
                viewModel.onNavigateUp()
            }
        }

        binding.deleteImage.setOnClickListener {
            val animation = AnimationUtils.loadAnimation(requireContext(), R.anim.click_animation)


            animation.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation?) {
                    // Animation started
                }

                override fun onAnimationEnd(animation: Animation?) {
                    // Animation ended, navigate to the RevenuesAndExpensesFragment
                    viewModel.postImageUri(null)
                }

                override fun onAnimationRepeat(animation: Animation?) {
                    // Animation repeated
                }
            })
            it.startAnimation(animation)
        }

        return binding.root
    }

    private fun initTargetSpinner() {
        val targetList = resources.getStringArray(R.array.target_type)
        val targetAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, targetList)
        binding.targetSpinner.adapter = targetAdapter


        binding.targetSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>, view: View?, position: Int, id: Long
                ) {
                    viewModel.postTarget(targetList[position])
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    //todo
                }

            }

    }

    private val startForProfileImageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data
            when (resultCode) {
                Activity.RESULT_OK -> {
                    val fileUri = data?.data!!
                    viewModel.postImageUri(fileUri)
                    Glide.with(this).load(fileUri).listener(object : RequestListener<Drawable> {
                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Drawable>?,
                            isFirstResource: Boolean
                        ): Boolean {
                            viewModel.postImageProgressBarState(false)
                            return false
                        }

                        override fun onResourceReady(
                            resource: Drawable?,
                            model: Any?,
                            target: Target<Drawable>?,
                            dataSource: com.bumptech.glide.load.DataSource?,
                            isFirstResource: Boolean
                        ): Boolean {
                            viewModel.postImageProgressBarState(false)
                            return false
                        }

                    })
                        .into(binding.productImage)
                }

                ImagePicker.RESULT_ERROR -> {
                    Toast.makeText(requireContext(), ImagePicker.getError(data), Toast.LENGTH_SHORT)
                        .show()
                    viewModel.postImageProgressBarState(false)
                }

                else -> {
                    viewModel.postImageProgressBarState(false)

                }
            }
        }
}