package com.example.rafik.ui

import android.annotation.SuppressLint
import com.google.android.material.appbar.MaterialToolbar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import com.example.rafik.R

fun initToolbar(
    toolbar: MaterialToolbar, title: String? = null, fragment: Fragment
) {
    toolbar.setNavigationIcon(R.drawable.ic_back_arrow)
    toolbar.title = title
    toolbar.setTitleTextColor(fragment.resources.getColor(R.color.base_green_color))
    toolbar.isTitleCentered = true
    toolbar.setNavigationOnClickListener {
        onBackPressed(fragment)
    }

}

fun onBackPressed(fragment: Fragment) {
    NavHostFragment.findNavController(fragment).popBackStack()
}
