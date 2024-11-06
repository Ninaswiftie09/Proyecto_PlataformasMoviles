package com.example.boton_sos

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.google.firebase.auth.AuthResult


class AuthViewModel(private val repository: AuthRepository = AuthRepository()) : ViewModel() {

    private val _authResult = MutableStateFlow<Result<AuthResult>?>(null)
    val authResult: StateFlow<Result<AuthResult>?> = _authResult

    fun register(email: String, password: String, onSuccess: (String) -> Unit) {
        viewModelScope.launch {
            val result = repository.registerUser(email, password)
            _authResult.value = result
            result.onSuccess { authResult ->
                val uid = authResult.user?.uid
                if (uid != null) {
                    onSuccess(uid)
                }
            }
        }
    }

    fun saveUserData(
        uid: String,
        name: String,
        phoneNumber: String,
        bloodType: String,
        emergencyContactName1: String,
        emergencyContactPhone1: String,
        emergencyContactName2: String,
        emergencyContactPhone2: String
    ) {
        val db = Firebase.firestore
        val user = mapOf(
            "nombre" to name,
            "telefono" to phoneNumber,
            "tipoDeSangre" to bloodType,
            "emergencia1" to mapOf(
                "nombre" to emergencyContactName1,
                "numero" to emergencyContactPhone1
            ),
            "emergencia2" to mapOf(
                "nombre" to emergencyContactName2,
                "numero" to emergencyContactPhone2
            )
        )

        db.collection("usuarios").document(uid).set(user)
            .addOnSuccessListener {  }
            .addOnFailureListener { e ->
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