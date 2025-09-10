package com.example.myapplication.data

sealed class AuthResult {
    data class Success(val keypass: String) : AuthResult()
    data class Error(val message: String) : AuthResult()
    object Loading : AuthResult()
}
