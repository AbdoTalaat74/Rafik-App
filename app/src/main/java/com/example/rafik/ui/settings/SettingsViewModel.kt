package com.example.rafik.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rafik.data.repo.FireBaseRepoImpl
import com.example.rafik.domian.entity.User
import kotlinx.coroutines.launch

class SettingsViewModel : ViewModel() {
    private val firebaseRepoImpl = FireBaseRepoImpl()

    fun updateUser(user: User) {
        viewModelScope.launch {
            firebaseRepoImpl.updateUser(user)
        }
    }

    init {
        viewModelScope.launch {
            firebaseRepoImpl.getUser()
        }
    }

    val user = firebaseRepoImpl.user
}