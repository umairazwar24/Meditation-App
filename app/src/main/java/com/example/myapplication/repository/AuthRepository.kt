package com.example.myapplication.repository

import com.example.myapplication.api.ApiClient
import com.example.myapplication.data.AuthResult
import com.example.myapplication.data.LoginRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AuthRepository {
    private val authApiService = ApiClient.authApiService
    
    suspend fun login(
        username: String, 
        password: String, 
        classLocation: ClassLocation
    ): AuthResult = withContext(Dispatchers.IO) {
        try {
            val request = LoginRequest(username, password)
            val response = when (classLocation) {
                ClassLocation.FOOTSCRAY -> authApiService.loginFootscray(request)
                ClassLocation.SYDNEY -> authApiService.loginSydney(request)
                ClassLocation.BR -> authApiService.loginBr(request)
            }
            
            if (response.isSuccessful) {
                response.body()?.let { loginResponse ->
                    AuthResult.Success(loginResponse.keypass)
                } ?: AuthResult.Error("Invalid response from server")
            } else {
                when (response.code()) {
                    401 -> AuthResult.Error("Invalid username or password")
                    404 -> AuthResult.Error("Authentication endpoint not found")
                    500 -> AuthResult.Error("Server error. Please try again later")
                    else -> AuthResult.Error("Login failed. Please try again")
                }
            }
        } catch (e: Exception) {
            AuthResult.Error("Network error: ${e.message ?: "Please check your internet connection"}")
        }
    }
}

enum class ClassLocation {
    FOOTSCRAY,
    SYDNEY,
    BR
}
