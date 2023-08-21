package com.example.rafik.ui.registration


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.example.rafik.R
import com.example.rafik.databinding.FragmentSignInBinding
import com.example.rafik.domian.entity.User
import com.example.rafik.viewModel.LoginViewModel
import com.google.firebase.auth.FirebaseAuth

class SignInFragment : Fragment() {
    private lateinit var binding: FragmentSignInBinding
    private val loginViewModel by activityViewModels<LoginViewModel>()
    private lateinit var auth: FirebaseAuth
    lateinit var name: String
    private lateinit var phone: String
    private lateinit var address: String
    private lateinit var user: User
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignInBinding.inflate(layoutInflater)
        nameFocusListener()
        addressFocusListener()
        phoneFocusListener()
        binding.registerButton.setOnClickListener {
            binding.registerButton.startAnimation()
            if (binding.editTextName.text.toString() == "") {
                binding.textInputName.helperText = getString(R.string.enter_your_name)
                binding.registerButton.revertAnimation()
            }
            if (binding.editTextAddress.text.toString() == "") {
                binding.textInputPhone.helperText = getString(R.string.enter_your_address)
                binding.registerButton.revertAnimation()
            }
            if (binding.editTextPhone.text.toString() == "") {
                binding.textInputPhone.helperText = getString(R.string.enter_phone)
                binding.registerButton.revertAnimation()
            }
            submitForm(it)
        }

        return binding.root
    }

    private fun nameFocusListener() {
        binding.editTextName.setOnFocusChangeListener { _, focus ->
            if (!focus) {
                binding.textInputName.helperText = validateName()
            } else {
                binding.textInputName.helperText = ""
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
                binding.textInputAddress.helperText = validateAddress()
            } else {
                binding.textInputAddress.helperText = ""
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

    private fun phoneFocusListener() {
        binding.editTextPhone.setOnFocusChangeListener { _, focus ->
            if (!focus) {
                binding.textInputPhone.helperText = validatePhone()
            } else {
                binding.textInputPhone.helperText = ""
            }
        }
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
            return  getString(R.string.contian_11)
        }
        return null
    }

    private fun submitForm(view:View) {
        binding.textInputName.helperText = validateName()
        binding.textInputAddress.helperText = validateAddress()
        binding.textInputPhone.helperText = validatePhone()
        val validName = (binding.textInputName.helperText == null)
        val validAddress = (binding.textInputAddress.helperText == null)
        val validPhone = (binding.textInputPhone.helperText == null)
        if (validName && validAddress && validPhone) {
            //todo throw user to vm
            user=User(name, phone, address, "")
            Log.i("SignInFragment","throw $user to vm")
            loginViewModel.postUser(user)
            //todo navigating to otp
            Navigation.findNavController(view).navigate(R.id.action_signInFragment_to_otpTestFrag)
        }
    }
}