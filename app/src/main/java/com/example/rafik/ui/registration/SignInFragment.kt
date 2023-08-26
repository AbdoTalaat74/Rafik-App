package com.example.rafik.ui.registration


import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.widget.AppCompatSpinner
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.rafik.R
import com.example.rafik.databinding.FragmentSignInBinding
import com.example.rafik.domian.entity.Area
import com.example.rafik.domian.entity.City
import com.example.rafik.domian.entity.User
import com.example.rafik.utils.Constants.UserFound
import com.example.rafik.viewModel.LoginViewModel
import com.google.android.material.snackbar.Snackbar

class SignInFragment : Fragment() {
    private val tag = "SignInFragment"
    private lateinit var binding: FragmentSignInBinding
    private val loginViewModel by activityViewModels<LoginViewModel>()
    lateinit var name: String
    private lateinit var phone: String
    private lateinit var phone2: String
    private lateinit var address: String
    private lateinit var user: User
    private lateinit var city: City
    private lateinit var area: Area
    private var isLogin: Boolean = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignInBinding.inflate(layoutInflater)
        nameFocusListener()
        addressFocusListener()
        phoneFocusListener()
        initCitySpinner(binding.citySpinner, loginViewModel.cities)
        loginViewModel.isLogin.observe(viewLifecycleOwner) {
            isLogin = it
            Log.i("isLogin", "isLogin $isLogin")
        }
        loginViewModel.isUserFound.observe(viewLifecycleOwner) {
            Log.i("SignInFragment", "isUserFound=$it")
            Log.i("SignInFragment", "isLogin=$isLogin")
            when (it) {
                UserFound.FOUND -> {
                    if (isLogin) {
                        findNavController().navigate(R.id.action_signInFragment_to_otpAuthFrag)
                    } else {
                        Snackbar.make(
                            binding.coordinatorlayout,
                            getString(R.string.this_number_is_already_registered_try_to_log_in),
                            Snackbar.LENGTH_SHORT
                        ).show()
                        binding.registerButton.revertAnimation()
                        binding.loginButton.revertAnimation()
                    }
                }

                UserFound.NOT_FOUND -> {
                    if (isLogin) {
                        Snackbar.make(
                            binding.coordinatorlayout,
                            getString(R.string.this_number_is_not_registered_try_to_register),
                            Snackbar.LENGTH_SHORT
                        ).show()
                        binding.registerButton.revertAnimation()
                        binding.loginButton.revertAnimation()
                    } else {
                        findNavController().navigate(R.id.action_signInFragment_to_otpAuthFrag)
                    }
                }

                null -> {
                    Log.i(tag, "INVALID_USER")
                }
            }
        }
        binding.registerButton.setOnClickListener {
            binding.registerButton.startAnimation()
            if (binding.editTextName.text.toString() == "") {
                binding.textInputName.error = getString(R.string.enter_your_name)
                binding.registerButton.revertAnimation()
            }
            if (binding.editTextAddress.text.toString() == "") {
                binding.textInputPhone.error = getString(R.string.enter_your_address)
                binding.registerButton.revertAnimation()
            }
            if (binding.editTextPhone.text.toString() == "") {
                binding.textInputPhone.error = getString(R.string.enter_phone)
                binding.registerButton.revertAnimation()
            }
            submitForm()
        }

        binding.loginButton.setOnClickListener {
            binding.loginButton.startAnimation()
            if (binding.editTextPhone2.text.toString() == "") {
                binding.textInputPhone2.error = getString(R.string.enter_phone)
                binding.registerButton.revertAnimation()
            }
            submitForm2()
        }

        return binding.root
    }

    private fun nameFocusListener() {
        binding.editTextName.setOnFocusChangeListener { _, focus ->
            if (!focus) {
                binding.textInputName.error = validateName()
            } else {
                binding.textInputName.error = ""
            }
        }
    }

    private fun validateName(): String? {
        name = binding.editTextName.text.toString()
        while (name == "") {
            return getString(R.string.enter_your_name)
        }
        return null
    }

    private fun addressFocusListener() {
        binding.editTextAddress.setOnFocusChangeListener { _, focus ->
            if (!focus) {
                binding.textInputAddress.error = validateAddress()
            } else {
                binding.textInputAddress.error = ""
            }
        }
    }

    private fun validateAddress(): String? {
        address = binding.editTextAddress.text.toString()
        while (address == "") {
            binding.registerButton.revertAnimation()
            return getString(R.string.enter_your_address)
        }
        return null
    }

    private fun initCitySpinner(spinner: AppCompatSpinner, cities: List<City>) {
        val citiesAdapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, cities)
        citiesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = citiesAdapter
        spinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    city = City(cities[position].arName, cities[position].enName)
                    initAreaSpinner(binding.areaSpinner, cities[position].areas)
                    Log.i("initAreaSpinner", "city: ${cities[position]}")
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    //todo
                }
            }
    }

    private fun initAreaSpinner(spinner: AppCompatSpinner, areas: List<Area>) {
        val areaAdapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, areas)
        areaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = areaAdapter
        spinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    area = areas[position]
                    Log.i("initAreaSpinner", "area: ${areas[position]}")
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    //todo
                }
            }
    }

    private fun phoneFocusListener() {
        binding.editTextPhone.setOnFocusChangeListener { _, focus ->
            if (!focus) {
                binding.textInputPhone.error = validatePhone()
            } else {
                binding.textInputPhone.error = ""
            }
        }
    }

    private fun validatePhone2(): String? {
        phone2 = binding.editTextPhone2.text.toString()
        while (phone2 == "") {
            binding.loginButton.revertAnimation()
            return getString(R.string.enter_phone)
        }
        if (phone2.length < 11) {
            binding.loginButton.revertAnimation()
            return getString(R.string.lenght_phone_check)
        }
        if (!phone2.matches("01\\d{9}".toRegex())) {
            binding.loginButton.revertAnimation()
            return getString(R.string.contian_11)
        }
        return null
    }

    private fun validatePhone(): String? {
        phone = binding.editTextPhone.text.toString()
        while (phone == "") {
            binding.registerButton.revertAnimation()
            return getString(R.string.enter_phone)
        }
        if (phone.length < 11) {
            binding.registerButton.revertAnimation()
            return getString(R.string.lenght_phone_check)
        }
        if (!phone.matches("01\\d{9}".toRegex())) {
            binding.registerButton.revertAnimation()
            return getString(R.string.contian_11)
        }
        return null
    }

    private fun submitForm2() {
        binding.textInputPhone2.error = validatePhone2()
        val validPhone = (binding.textInputPhone2.error == null)
        if (validPhone) {
            user = User(phone = phone2)
            Log.i("SignInFragment", "throw $user to vm")
            alertDialog2()
        }
    }

    private fun submitForm() {
        binding.textInputName.error = validateName()
        binding.textInputAddress.error = validateAddress()
        binding.textInputPhone.error = validatePhone()
        val validName = (binding.textInputName.error == null)
        val validAddress = (binding.textInputAddress.error == null)
        val validPhone = (binding.textInputPhone.error == null)
        if (validName && validAddress && validPhone) {
            //todo throw user to vm
            alertDialog()
        }
    }

    private fun alertDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(resources.getString(R.string.confirmation))
        builder.setMessage("${resources.getString(R.string.are_you_sure)} $phone ?")
        builder.setPositiveButton(R.string.yes) { _, _ ->
            user = User(name, phone, address, "", city, area)
            Log.i("SignInFragment", "throw $user to vm")
            loginViewModel.postUser(user, false)
            loginViewModel.checkUser(phone)
            isLogin = true
        }

        builder.setNegativeButton(R.string.no) { _, _ ->
            //todo no
            binding.registerButton.revertAnimation()
            binding.loginButton.revertAnimation()
        }
        builder.show()
    }

    private fun alertDialog2() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(resources.getString(R.string.confirmation))
        builder.setMessage("${resources.getString(R.string.are_you_sure)} $phone2 ?")
        builder.setPositiveButton(R.string.yes) { _, _ ->
            user = User(phone = phone2)
            loginViewModel.postUser(user, true)
            loginViewModel.checkUser(phone2)
            isLogin = true
        }

        builder.setNegativeButton(R.string.no) { _, _ ->
            //todo no
            binding.registerButton.revertAnimation()
            binding.loginButton.revertAnimation()
        }
        builder.show()
    }
}