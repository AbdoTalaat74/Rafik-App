package com.example.rafik.ui.registration

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.example.rafik.R
import com.example.rafik.databinding.FragmentOtpAuthBinding
import com.example.rafik.viewModel.LoginViewModel
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.PhoneAuthProvider.ForceResendingToken
import com.google.firebase.auth.PhoneAuthProvider.OnVerificationStateChangedCallbacks
import com.otpview.OTPListener
import java.util.concurrent.TimeUnit


class OtpAuthFrag : Fragment() {
    private lateinit var binding: FragmentOtpAuthBinding
    private lateinit var mAuth: FirebaseAuth
    private val loginViewModel by activityViewModels<LoginViewModel>()
    private lateinit var phone: String
    private var verificationId: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOtpAuthBinding.inflate(layoutInflater)
        mAuth = FirebaseAuth.getInstance()
        loginViewModel.user.observe(viewLifecycleOwner) {
            Log.i("OtpAuthFrag", "observing $it")
            binding.user = it
            phone = "+20" + it!!.phone
            binding.otpView.requestFocus()
            sendVerificationCode(phone)
        }

        binding.otpView.otpListener = object : OTPListener {
            override fun onInteractionListener() {

            }

            override fun onOTPComplete(otp: String) {
                Toast.makeText(requireContext(), "The OTP is $otp", Toast.LENGTH_SHORT).show()
            }
        }

        binding.tryAgain.setOnClickListener {
            binding.otpView.requestFocus()
            sendVerificationCode(phone)
        }

        binding.idBtnVerify.setOnClickListener {
            binding.idBtnVerify.startAnimation()
            if (TextUtils.isEmpty(binding.otpView.otp)) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.please_enter_code),
                    Toast.LENGTH_SHORT
                ).show()
                binding.idBtnVerify.revertAnimation()
                binding.otpView.showError()
            } else {
                binding.otpView.otp?.let {
                    verifyCode(it)
                }
            }
        }
        // Inflate the layout for this fragment
        return binding.root
    }

    private fun signInWithCredential(credential: PhoneAuthCredential) {
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    binding.otpView.showSuccess()
                        when (loginViewModel.isLogin.value!!) {
                            true -> {
                                Log.i("signInWithCredential", "login successfully")
                            }

                            false -> {
                                Log.i("signInWithCredential", "sign up successfully")
                                loginViewModel.setUser(binding.user!!)
                            }
                        }

                    Navigation.findNavController(requireView())
                        .navigate(R.id.action_otpTestFrag_to_homeFragment)
                } else {
                    Toast.makeText(requireContext(), task.exception!!.message, Toast.LENGTH_LONG)
                        .show()
                    Log.e("signInWithCredential", task.exception!!.message.toString())
                    binding.idBtnVerify.revertAnimation()
                    binding.otpView.showError()
                }
            }
    }

    private fun sendVerificationCode(number: String) {
        val options = PhoneAuthOptions.newBuilder(mAuth)
            .setPhoneNumber(number) // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(requireActivity()) // Activity (for callback binding)
            .setCallbacks(mCallBack) // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private val mCallBack: OnVerificationStateChangedCallbacks =
        object : OnVerificationStateChangedCallbacks() {
            override fun onCodeSent(s: String, forceResendingToken: ForceResendingToken) {
                super.onCodeSent(s, forceResendingToken)
                verificationId = s
                binding.idBtnVerify.isEnabled = true
            }

            override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
                val code = phoneAuthCredential.smsCode
                if (code != null) {
                    //  binding.idEdtOtp.setText(code)
                    verifyCode(code)
                }
            }

            override fun onVerificationFailed(e: FirebaseException) {
                Toast.makeText(requireActivity(), e.message, Toast.LENGTH_LONG).show()
                Log.e("onVerificationFailed", e.message.toString())
                binding.otpView.showError()
            }
        }

    private fun verifyCode(code: String) {
        val credential = PhoneAuthProvider.getCredential(verificationId, code)
        signInWithCredential(credential)
    }
}