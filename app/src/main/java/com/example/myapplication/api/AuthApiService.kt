package com.example.myapplication.api

import com.example.myapplication.data.LoginRequest
import com.example.myapplication.data.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {
    @POST("footscray/auth")
    suspend fun loginFootscray(@Body request: LoginRequest): Response<LoginResponse>
    
    @POST("sydney/auth")
    suspend fun loginSydney(@Body request: LoginRequest): Response<LoginResponse>
    
    @POST("br/auth")
    suspend fun loginBr(@Body request: LoginRequest): Response<LoginResponse>
}
