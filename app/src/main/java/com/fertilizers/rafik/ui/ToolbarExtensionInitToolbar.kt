package com.fertilizers.rafik.ui

import android.app.AlertDialog
import com.google.android.material.appbar.MaterialToolbar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import com.fertilizers.rafik.R

fun initToolbar(
    toolbar: MaterialToolbar, title: String? = null, fragment: Fragment
) {
    toolbar.setNavigationIcon(R.drawable.ic_back_arrow)
    toolbar.title = title
    toolbar.setTitleTextColor(fragment.resources.getColor(R.color.base_green_color))
    toolbar.isTitleCentered = true
    toolbar.setNavigationOnClickListener {

        AlertDialog.Builder(fragment.context)
            .setTitle(fragment.resources.getString(R.string.confirmation))
            .setMessage(fragment.resources.getString(R.string.navigate_up_confirmation))
            .setPositiveButton(fragment.resources.getString(R.string.okay)) { _, _ ->
                onBackPressed(fragment)
            }
            .setNegativeButton(fragment.resources.getString(R.string.cancel)) { _, _ ->
                //do nothing
            }
            .setIcon(R.drawable.ic_warning)
            .show()

    }

}

fun onBackPressed(fragment: Fragment) {
    NavHostFragment.findNavController(fragment).popBackStack()
}
