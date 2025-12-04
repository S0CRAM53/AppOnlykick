package com.example.apponlykick.repository

import com.example.apponlykick.data.model.UserDto
import com.example.apponlykick.data.remote.ApiClient
import com.example.apponlykick.ui.viewmodel.User

class AuthRepository {
    private val api = ApiClient.service

    suspend fun registerUser(name: String, email: String, pass: String): User? {
        return try {
            val request = UserDto(name = name, email = email, pass = pass)

            val response = api.register(request)

            if (response.isSuccessful && response.body() != null) {
                val backendUser = response.body()!!

                User(
                    id = backendUser.id?.toString() ?: "0",
                    email = backendUser.email,
                    name = backendUser.name ?: name
                )
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun loginUser(email: String, pass: String): User? {
        return try {
            val request = UserDto(name = "", email = email, pass = pass)

            val response = api.login(request)

            if (response.isSuccessful && response.body() != null) {
                val backendUser = response.body()!!

                User(
                    id = backendUser.id?.toString() ?: "0",
                    email = backendUser.email,
                    name = backendUser.name ?: "Usuario"
                )
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}