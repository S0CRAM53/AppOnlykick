package com.example.apponlykick.data.model

import com.google.gson.annotations.SerializedName

data class UserDto(
    @SerializedName("idUsuario") val id: Int? = null,
    @SerializedName("nombreUsuario") val name: String? = null,
    @SerializedName("email") val email: String,
    @SerializedName("passwordHash") val pass: String? = null,
    @SerializedName("rol") val role: String = "CLIENTE"
)