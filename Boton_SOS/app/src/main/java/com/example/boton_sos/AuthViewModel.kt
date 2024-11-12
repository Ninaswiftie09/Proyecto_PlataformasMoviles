package com.example.boton_sos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.AuthResult
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import android.util.Log

class AuthViewModel(private val repository: AuthRepository = AuthRepository()) : ViewModel() {

    private val _authResult = MutableStateFlow<Result<AuthResult>?>(null)
    val authResult: StateFlow<Result<AuthResult>?> = _authResult

    private val _userInfo = MutableStateFlow<Map<String, Any>?>(null)
    val userInfo: StateFlow<Map<String, Any>?> = _userInfo

    fun register(email: String, password: String, onSuccess: (String) -> Unit) {
        viewModelScope.launch {
            val result = repository.registerUser(email, password)
            _authResult.value = result
            result.onSuccess { authResult ->
                val uid = authResult.user?.uid
                if (uid != null) {
                    fetchUserData(uid)
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
            .addOnSuccessListener {
                Log.d("Firestore", "Datos de usuario guardados con éxito")
                fetchUserData(uid) // Actualiza los datos del usuario
            }
            .addOnFailureListener { e ->
                Log.e("FirestoreError", "Error al guardar datos de usuario: ${e.message}")
            }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            val result = repository.loginUser(email, password)
            _authResult.value = result
            result.onSuccess { authResult ->
                val uid = authResult.user?.uid
                if (uid != null) {
                    fetchUserData(uid)
                }
            }
        }
    }

    fun clearAuthResult() {
        _authResult.value = null
    }

    fun fetchUserData(uid: String) {
        val db = Firebase.firestore
        db.collection("usuarios").document(uid).get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    _userInfo.value = document.data
                    Log.d("Firestore", "Datos del usuario obtenidos: ${document.data}")
                } else {
                    Log.e("FirestoreError", "El documento no existe")
                }
            }
            .addOnFailureListener { exception ->
                Log.e("FirestoreError", "Error al obtener datos de usuario: ${exception.message}")
            }
    }
}
