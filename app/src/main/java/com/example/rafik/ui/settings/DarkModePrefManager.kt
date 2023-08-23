package com.example.rafik.ui.settings

import android.content.Context
import android.content.SharedPreferences

class DarkModePrefManager(context: Context) {
    private var PRIVATE_MODE = 0
    private val PREF_NAME = "NightMode"
    private val IS_NIGHT_MODE = "IsNightMode"

    // shared pref mode
    private var pref: SharedPreferences = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE)
    // Shared preferences file name
    private var editor: SharedPreferences.Editor = pref.edit()

    fun setDarkMode(isFirstTime: Boolean) {
        editor.putBoolean(IS_NIGHT_MODE, isFirstTime)
        editor.commit()
    }

    fun isNightMode(): Boolean {
        return pref.getBoolean(IS_NIGHT_MODE, false)
    }
}