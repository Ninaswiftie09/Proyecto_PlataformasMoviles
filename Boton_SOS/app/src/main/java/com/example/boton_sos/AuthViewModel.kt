package com.example.boton_sos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.google.firebase.auth.AuthResult

class AuthViewModel(private val repository: AuthRepository = AuthRepository()) : ViewModel() {

    private val _authResult = MutableStateFlow<Result<AuthResult>?>(null)
    val authResult: StateFlow<Result<AuthResult>?> = _authResult

    fun register(email: String, password: String) {
        viewModelScope.launch {
            _authResult.value = repository.registerUser(email, password)
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _authResult.value = repository.loginUser(email, password)
        }
    }

    fun clearAuthResult() {
        _authResult.value = null
    }
}

