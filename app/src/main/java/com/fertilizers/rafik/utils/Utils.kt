package com.fertilizers.rafik.utils

import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController

class Utils {
    companion object {
        fun Fragment.findNavControllerSafely(id: Int): NavController? {
            return if (findNavController().currentDestination?.id == id) {
                findNavController()
            } else {
                null
            }
        }
    }
}