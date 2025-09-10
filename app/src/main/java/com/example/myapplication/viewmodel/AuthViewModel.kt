package com.example.myapplication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.AuthResult
import com.example.myapplication.repository.AuthRepository
import com.example.myapplication.repository.ClassLocation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {
    private val authRepository = AuthRepository()
    
    private val _authResult = MutableStateFlow<AuthResult>(AuthResult.Loading)
    val authResult: StateFlow<AuthResult> = _authResult.asStateFlow()
    
    fun login(username: String, password: String, classLocation: ClassLocation) {
        viewModelScope.launch {
            _authResult.value = AuthResult.Loading
            val result = authRepository.login(username, password, classLocation)
            _authResult.value = result
        }
    }
    
    fun clearAuthResult() {
        _authResult.value = AuthResult.Loading
    }
}
