package com.example.apponlykick.repository

import com.example.apponlykick.data.model.Product
import com.example.apponlykick.data.remote.ApiService

class ProductRepository(private val apiService: ApiService) {


    suspend fun getProducts(): List<Product> {
        return apiService.getProducts()
    }
}