package com.fertilizers.rafik.ui.sell_product

import android.app.Activity
import android.app.AlertDialog
import android.app.Application
import android.app.DatePickerDialog
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.fertilizers.rafik.R
import com.fertilizers.rafik.databinding.FragmentSellProductBinding
import com.fertilizers.rafik.domian.entity.ProductType
import com.fertilizers.rafik.ui.initToolbar
import com.fertilizers.rafik.utils.Constants
import com.fertilizers.rafik.utils.Constants.MY_FORMAT
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class SellProductFragment : Fragment() {
    private lateinit var binding: FragmentSellProductBinding
    private lateinit var viewModel: SellProductViewModel
    val sdf = SimpleDateFormat(MY_FORMAT, Locale.ENGLISH)
    private var cal = Calendar.getInstance()
    private var picUri: Uri? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentSellProductBinding.inflate(layoutInflater)
        initToolbar(binding.topAppBar, getString(R.string.product_for_sale), this)
        viewModel = ViewModelProvider(
            this, SellProductVMFactory(requireContext().applicationContext as Application)
        )[SellProductViewModel::class.java]
        initTargetSpinner()
        amountFocusListener()
        addressFocusListener()
        priceFocusListener()
        binding.viewModel = viewModel

        val dateSetListener =
            DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                binding.textDate.text = sdf.format(cal.time)
            }

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
                binding.pickImg.visibility = INVISIBLE
                binding.imageProgressBar.visibility = VISIBLE
                binding.imageProgressBar.isActivated = it
            } else {
                if (picUri == null) {
                    binding.pickImg.visibility = VISIBLE
                    binding.productImage.visibility = GONE
                    binding.deleteImage.visibility = GONE
                } else {
                    binding.pickImg.visibility = INVISIBLE
                    binding.productImage.visibility = VISIBLE
                    binding.deleteImage.visibility = VISIBLE
                }
                binding.imageProgressBar.visibility = GONE
                binding.imageProgressBar.isActivated = it
            }
        }

        viewModel.imageUri.observe(viewLifecycleOwner) {
            if (it != null) {
                picUri = it
                binding.productImage.visibility = VISIBLE
                binding.deleteImage.visibility = VISIBLE
                binding.imageProgressBar.visibility = GONE
                binding.pickImg.visibility = INVISIBLE
            } else {
                binding.productImage.visibility = GONE
                binding.deleteImage.visibility = GONE
                binding.pickImg.visibility = VISIBLE
            }
        }

        viewModel.productTypes.observe(viewLifecycleOwner) {
            if (it == null) {
                Snackbar.make(
                    binding.coordinatorlayout,
                    getString(R.string.checkYourInternet),
                    Snackbar.LENGTH_LONG
                ).show()
            } else {
                initProductTypeSpinner(it)
            }
        }

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
                    picUri = null
                }

                override fun onAnimationRepeat(animation: Animation?) {
                    // Animation repeated
                }
            })
            it.startAnimation(animation)
        }

        binding.date.setOnClickListener {
            val animation = AnimationUtils.loadAnimation(requireContext(), R.anim.click_animation)

            animation.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation?) {
                    // Animation started
                }

                override fun onAnimationEnd(animation: Animation?) {
                    // Animation ended, navigate to the RevenuesAndExpensesFragment
                    val timePicker = DatePickerDialog(
                        requireContext(),
                        dateSetListener,
                        // set DatePickerDialog to point to today's date when it loads up
                        cal.get(Calendar.YEAR),
                        cal.get(Calendar.MONTH),
                        cal.get(Calendar.DAY_OF_MONTH)
                    )
                    timePicker.setOnCancelListener {

                    }
                    timePicker.show()
                }

                override fun onAnimationRepeat(animation: Animation?) {
                    // Animation repeated
                }
            })
            it.startAnimation(animation)
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
                    Snackbar.make(
                        binding.coordinatorlayout,
                        getString(R.string.your_request_was_not_sent_please_try_again_later),
                        Snackbar.LENGTH_LONG
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
                    Snackbar.make(
                        binding.coordinatorlayout,
                        getString(R.string.an_error_occurred_try_again_later),
                        Snackbar.LENGTH_SHORT
                    ).show()
                    viewModel.postImageProgressBarState(false)
                }

                else -> {
                    viewModel.postImageProgressBarState(false)

                }
            }
        }


    private fun amountFocusListener() {
        binding.amountTextField.setOnFocusChangeListener { _, focus ->
            if (!focus) {
                binding.amountTextLayout.error = validateAmount()
            } else {
                binding.amountTextLayout.error = ""
            }
        }
    }

    private fun validateAmount(): String? {
        val amount = binding.amountTextField.text.toString()
        while (amount.isNullOrBlank()) {
            return getString(R.string.please_fill_amount_field)
        }
        return null
    }


    private fun addressFocusListener() {
        binding.addressTextField.setOnFocusChangeListener { _, focus ->
            if (!focus) {
                binding.addressTextLayout.error = validateAddress()
            } else {
                binding.addressTextLayout.error = ""
            }
        }
    }

    private fun validateAddress(): String? {
        val address = binding.addressTextField.text.toString()
        while (address.isNullOrBlank()) {
            return getString(R.string.please_fill_address_field)
        }
        return null
    }


    private fun priceFocusListener() {
        binding.priceTextField.setOnFocusChangeListener { _, focus ->
            if (!focus) {
                binding.priceTextLayout.error = validatePrice()
            } else {
                binding.priceTextLayout.error = ""
            }
        }
    }


    private fun validatePrice(): String? {
        val price = binding.priceTextField.text.toString()
        while (price.isNullOrBlank()) {
            return getString(R.string.please_fill_price_field)
        }
        return null
    }


    private fun submitForm() {
        Log.e("SellProductFragment", "submitForm called")
        binding.amountTextLayout.error = validateAmount()
        binding.addressTextLayout.error = validateAddress()
        binding.priceTextLayout.error = validatePrice()
    }


}