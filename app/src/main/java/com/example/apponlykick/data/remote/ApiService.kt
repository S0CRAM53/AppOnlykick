package com.example.apponlykick.data.remote

import com.example.apponlykick.data.model.Product
import com.example.apponlykick.data.model.UserDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @GET("productos")
    suspend fun getProducts(): List<Product>

    @POST("usuarios/login")
    suspend fun login(@Body user: UserDto): Response<UserDto>

    @POST("usuarios/registro")
    suspend fun register(@Body user: UserDto): Response<UserDto>
}