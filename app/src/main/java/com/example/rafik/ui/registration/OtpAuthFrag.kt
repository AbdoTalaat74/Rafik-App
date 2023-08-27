package com.example.rafik.ui.registration

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.rafik.R
import com.example.rafik.databinding.FragmentOtpAuthBinding
import com.example.rafik.ui.MainActivity
import com.example.rafik.viewModel.LoginViewModel
import com.google.android.material.snackbar.Snackbar
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
    private lateinit var intent: Intent
    private var isLogin: Boolean = false

    private lateinit var countDownTimer: CountDownTimer
    private val initialTimeInMillis: Long = 60000 // 60 seconds in milliseconds


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOtpAuthBinding.inflate(layoutInflater)

        mAuth = FirebaseAuth.getInstance()
        loginViewModel.user.observe(viewLifecycleOwner) {
            Log.i("OtpAuthFrag", "observing $it")
            binding.user = it
            phone = "+20" + it.phone
            loginViewModel.checkUser(it.phone)
            binding.otpView.requestFocus()
            sendVerificationCode(phone)
        }
        binding.viewModel = loginViewModel
        loginViewModel.isLogin.observe(viewLifecycleOwner) {
            isLogin = it
            Log.i("isLogin", "isLogin $isLogin")
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
            startTimer()

        }

        loginViewModel.startTimer.observe(viewLifecycleOwner){
            if (it == true) {
                startTimer()
            }
        }

        loginViewModel.isButtonEnabled.observe(viewLifecycleOwner){
            binding.tryAgain.isEnabled = it
            if (it == true){
                Log.e("OtpAuthFrag","tryAgain Enabled")
                binding.tryAgain.setTextColor(resources.getColor(com.apachat.loadingbutton.core.R.color.green,null))
                binding.timerText.visibility = INVISIBLE
            }else{
                Log.e("OtpAuthFrag","tryAgain Disabled")
                binding.tryAgain.setTextColor(resources.getColor(R.color.Gray,null))
                binding.timerText.visibility = VISIBLE
            }
        }



        binding.idBtnVerify.setOnClickListener {
            if (it.isEnabled){
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
                    binding.otpView.otp?.let { code ->
                        verifyCode(code)
                        it.isEnabled = false
                    }
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
                    intent = Intent(activity, MainActivity::class.java)
                    binding.otpView.showSuccess()
                    when (isLogin) {
                        true -> {
                            Log.i("signInWithCredential", "login successfully")
                            Snackbar.make(
                                binding.constraintLayout,
                                "Logged in successfully",
                                Snackbar.LENGTH_LONG
                            ).show()
                            Handler(Looper.getMainLooper()).postDelayed({
                                loginViewModel.loginUser(binding.user!!)
                            }, 1000)


                        }

                        false -> {
                            Log.i("signInWithCredential", "sign up successfully")
                            Snackbar.make(
                                binding.constraintLayout,
                                "successfully registered",
                                Snackbar.LENGTH_LONG
                            ).show()
                            Handler(Looper.getMainLooper()).postDelayed({
                                loginViewModel.setUser(binding.user!!)
                            }, 1000)

                        }
                    }
                } else {
                    binding.otpView.showError()
                    val errorMessage = task.exception?.message
                    if (errorMessage == "The verification code from SMS/TOTP is invalid. Please check and enter the correct verification code again.") {
                        Snackbar.make(
                            binding.constraintLayout,
                            getString(R.string.the_verification_code_is_invalid),
                            Snackbar.LENGTH_LONG
                        ).show()
                        Log.e("signInWithCredential", task.exception!!.message.toString())
                        binding.idBtnVerify.revertAnimation()
                    } else {
                        Snackbar.make(
                            binding.constraintLayout,
                            getString(R.string.an_error_occurred_try_again_later),
                            Snackbar.LENGTH_LONG
                        ).show()
                    }
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
                    binding.idBtnVerify.isEnabled = true
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


    private fun startTimer() {
        loginViewModel.onStartTimer()
        countDownTimer = object : CountDownTimer(initialTimeInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val secondsRemaining = millisUntilFinished / 1000
                loginViewModel.timerText.set(secondsRemaining.toString())
                loginViewModel.setButtonState(false)
                binding.timerText.visibility = VISIBLE
            }

            override fun onFinish() {
                loginViewModel.timerText.set("0")
                Log.e("OtpAuthFrag","onFinish called")
                loginViewModel.setButtonState(true)

                // Handle timer completion
            }
        }
        countDownTimer.start()
    }
}