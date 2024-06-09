package com.example.flo

import android.telecom.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthRetrofitInterface {
    @POST("/users")
    fun signUp(@Body user : User): Call<AuthResponse>

    @POST("/users/login")
    fun login(@Body user: User): Call<AuthResponse>

    @GET("/users/auto-login") // 토큰 유효성 검사 엔드포인트
    fun autoLogin(@Header("Authorization") token: String): Call<AuthResponse>
}
