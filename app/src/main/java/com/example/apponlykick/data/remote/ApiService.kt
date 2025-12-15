package com.example.apponlykick.data.remote

import com.example.apponlykick.data.model.LoginResponse
import com.example.apponlykick.data.model.Product
import com.example.apponlykick.data.model.SaleRequest
import com.example.apponlykick.data.model.SaleResponse
import com.example.apponlykick.data.model.UserDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @GET("productos")
    suspend fun getProducts(): List<Product>

    @POST("usuarios/login")
    suspend fun login(@Body user: UserDto): Response<LoginResponse>

    @POST("usuarios/registro")
    suspend fun register(@Body user: UserDto): Response<UserDto>

    @POST("ventas")
    suspend fun createSale(@Body saleRequest: SaleRequest): Response<SaleResponse>

    @GET("ventas/usuario/{id}")
    suspend fun getSalesHistory(@Path("id") userId: Int): Response<List<SaleResponse>>
}
