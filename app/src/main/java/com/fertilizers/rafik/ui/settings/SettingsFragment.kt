package com.fertilizers.rafik.ui.settings

import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat.recreate
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.fertilizers.rafik.R
import com.fertilizers.rafik.databinding.FragmentSettingsBinding
import com.fertilizers.rafik.ui.AuthenticationActivity
import com.google.firebase.auth.FirebaseAuth
import java.util.Locale

class SettingsFragment : Fragment() {
    private val settingsViewModel by activityViewModels<SettingsViewModel>()
    private lateinit var binding: FragmentSettingsBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(layoutInflater)
        settingsViewModel.user.observe(viewLifecycleOwner) {
            binding.user = it
        }
        binding.logout.setOnClickListener {
            Log.i("SettingsFragment", "logout pressed")
            FirebaseAuth.getInstance().signOut()
            activity?.let {
                val intent = Intent(
                    it, AuthenticationActivity::class.java
                ).addFlags(FLAG_ACTIVITY_CLEAR_TASK)
                it.startActivity(intent)
                it.finish()
            }
        }
            binding.profileCardView.setOnClickListener {
                when (binding.textInputName.visibility) {
                    View.GONE -> {
                        binding.textInputName.visibility = View.VISIBLE
                        binding.user?.let {
                            if (binding.editTextName.text.toString() != it.name) {
                                binding.save.visibility = View.VISIBLE
                            }
                        }
                    }

                    View.VISIBLE -> {
                        binding.textInputName.visibility = View.GONE
                        binding.save.visibility = View.GONE
                    }

                    View.INVISIBLE -> {
                        Log.e("textInputName", "view is INVISIBLE")
                    }
                }
            }

            binding.langCardView.setOnClickListener {
                when (binding.radioGroup.visibility) {
                    View.GONE -> {
                        binding.radioGroup.visibility = View.VISIBLE
                    }

                    View.VISIBLE -> {
                        binding.radioGroup.visibility = View.GONE
                    }

                    View.INVISIBLE -> {
                        Log.e("radioGroup", "view is INVISIBLE")
                    }
                }
            }

            binding.editTextName.addTextChangedListener(object : TextWatcher {

                override fun afterTextChanged(s: Editable) {}

                override fun beforeTextChanged(
                    s: CharSequence, start: Int,
                    count: Int, after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    binding.user?.let {
                        if (s.toString() != it.name) {
                            binding.save.visibility = View.VISIBLE
                        }
                    }
                }
            })

            //function for enabling dark mode & language
            setDarkModeSwitch()
            setLanguageRadio()
            // Get radio group selected item using on checked change listener
            binding.radioGroup.setOnCheckedChangeListener { _, checkedId ->
                val langPref = LanguagePrefManger(requireContext())
                when (binding.root.findViewById(checkedId) as RadioButton) {
                    binding.ar -> {
                        langPref.setLanguage(Locale.getDefault().language)
                        langPref.setLanguage("ar")
                        setLocale("ar")
                    }

                    binding.en -> {
                        langPref.setLanguage("en")
                        setLocale("en")
                    }
                }
            }

            binding.save.setOnClickListener {
                binding.user?.let {
                    it.name = binding.editTextName.text.toString()
                    settingsViewModel.updateUser(it)
                    binding.save.visibility = View.GONE
                }
            }


        binding.backArrow.setOnClickListener {
            val animation = AnimationUtils.loadAnimation(requireContext(), R.anim.click_animation)

            animation.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation?) {
                    // Animation started
                }

                override fun onAnimationEnd(animation: Animation?) {
                    // Animation ended, navigate to the RevenuesAndExpensesFragment
                    findNavController().navigateUp()
                }

                override fun onAnimationRepeat(animation: Animation?) {
                    // Animation repeated
                }
            })
            it.startAnimation(animation)
        }

        binding.textView7.setOnClickListener {

            val animation = AnimationUtils.loadAnimation(requireContext(), R.anim.click_animation)

            animation.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation?) {
                    // Animation started
                }

                override fun onAnimationEnd(animation: Animation?) {
                    // Animation ended, navigate to the RevenuesAndExpensesFragment
                    findNavController().navigateUp()
                }

                override fun onAnimationRepeat(animation: Animation?) {
                    // Animation repeated
                }
            })
            binding.backArrow.startAnimation(animation)

        }

            return binding.root
        }

        @Suppress("DEPRECATION")
        private fun setLocale(languageCode: String) {
            val locale = Locale(languageCode)
            Locale.setDefault(locale)
            val resources: Resources = resources
            val config: Configuration = resources.configuration
            config.setLocale(locale)
            resources.updateConfiguration(config, resources.displayMetrics)
            recreate(requireActivity())
        }

        //في مشكله لما بيتحول لنايت مود وهو علي المود العادي todo
        private fun setDarkModeSwitch() {
            binding.darkModeSwitch.isChecked = DarkModePrefManager(requireContext()).isNightMode()
            binding.darkModeSwitch.setOnCheckedChangeListener { _, isChecked ->
                val darkModePrefManager = DarkModePrefManager(requireContext())
                darkModePrefManager.setDarkMode(isChecked)
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                recreate(requireActivity())
            }
        }

        private fun setLanguageRadio() {
            val myLocale = Locale(LanguagePrefManger(requireContext()).getLanguage())
            binding.radioGroup.clearCheck()
            when (myLocale.language) {
                "ar" -> {
                    binding.ar.isChecked = true
                }

                "en" -> {
                    binding.en.isChecked = true

                }
            }
            binding.ar.isChecked
        }

    }