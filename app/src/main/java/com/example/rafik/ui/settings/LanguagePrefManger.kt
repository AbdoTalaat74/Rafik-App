package com.example.rafik.ui.settings

import android.content.Context
import android.content.SharedPreferences
import java.util.Locale

class LanguagePrefManger(context: Context) {
    private var PRIVATE_MODE = 0
    private val PREF_NAME = "Language"
    private val IS_EN = "Language"

    // shared pref mode
    private var pref: SharedPreferences = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE)

    // Shared preferences file name
    private var editor: SharedPreferences.Editor = pref.edit()

    fun setLanguage(isFirstTime: String) {
        editor.putString(IS_EN, isFirstTime)
        editor.commit()
    }

    fun getLanguage(): String {
        return pref.getString(IS_EN, Locale.getDefault().language)!!
    }
}