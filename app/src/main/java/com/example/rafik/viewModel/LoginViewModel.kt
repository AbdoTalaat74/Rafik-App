package com.example.rafik.viewModel

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.example.rafik.data.repo.FireBaseRepoImpl
import com.example.rafik.domian.entity.FirebaseUserLiveData
import com.example.rafik.domian.entity.User
import kotlinx.coroutines.launch

@SuppressLint("CheckResult")
class LoginViewModel : ViewModel() {
    private val firebaseRepoImpl = FireBaseRepoImpl()

    private val _username = MutableLiveData<String?>()
    val username: LiveData<String?>
        get() = _username

    private val _user = MutableLiveData<User?>()
    val user: LiveData<User?>
        get() = _user

    private val _authenticationState = MutableLiveData<AuthenticationState?>()
    val authenticationState: LiveData<AuthenticationState?>
        get() = _authenticationState

    fun postUser(user: User) {
        _user.value=user
        viewModelScope.launch {
            _authenticationState.value = AuthenticationState.AUTHENTICATED
        }
    }

    fun setUser(user: User) {
        viewModelScope.launch {
            firebaseRepoImpl.setUser(user)
            _authenticationState.value = AuthenticationState.AUTHENTICATED
        }
    }

    enum class AuthenticationState {
        AUTHENTICATED, UNAUTHENTICATED, INVALID_AUTHENTICATION
    }

    init {
        FirebaseUserLiveData().map { user ->
            if (user != null) {
                _authenticationState.value = AuthenticationState.AUTHENTICATED
                _username.value = user.displayName
            } else {
                _authenticationState.value = AuthenticationState.UNAUTHENTICATED
            }
        }
    }

}