package com.example.rafik.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rafik.data.repo.FireBaseRepoImpl
import kotlinx.coroutines.launch

class SettingsViewModel : ViewModel() {
    private val firebaseRepoImpl = FireBaseRepoImpl()

    init {
        viewModelScope.launch {
            firebaseRepoImpl.getUser()
        }
    }

    val user = firebaseRepoImpl.user
}