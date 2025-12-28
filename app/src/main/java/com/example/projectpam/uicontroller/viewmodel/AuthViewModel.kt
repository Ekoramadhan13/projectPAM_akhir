package com.example.projectpam.uicontroller.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectpam.repositori.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    object Success : AuthState()
    data class Error(val message: String) : AuthState()
}

class AuthViewModel : ViewModel() {

    private val repository = AuthRepository()

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState

    // Menyimpan info apakah login berhasil sebagai admin
    private val _isAdmin = MutableStateFlow(false)
    val isAdmin: StateFlow<Boolean> = _isAdmin

    // ================= LOGIN USER =================
    fun loginUser(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                repository.login(email, password)
                _isAdmin.value = false
                _authState.value = AuthState.Success
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "Login user gagal")
            }
        }
    }

    // ================= LOGIN ADMIN =================
    fun loginAdmin(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                repository.login(email, password)
                _isAdmin.value = true
                _authState.value = AuthState.Success
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "Login admin gagal")
            }
        }
    }

    // ================= REGISTER USER =================
    fun register(name: String, email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                repository.register(name, email, password)
                _authState.value = AuthState.Success
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "Register gagal")
            }
        }
    }

    fun resetState() {
        _authState.value = AuthState.Idle
    }
}
