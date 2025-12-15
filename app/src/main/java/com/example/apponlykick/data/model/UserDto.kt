package com.example.apponlykick.data.model

import com.google.gson.annotations.SerializedName

data class UserDto(
    // Si en el backend se llama "nombreUsuario", usa esto:
    @SerializedName("nombreUsuario")
    val name: String,

    @SerializedName("email")
    val email: String,

    // Aquí está la corrección clave: "pass" en Kotlin -> "passwordHash" en el JSON para Java
    @SerializedName("passwordHash")
    val pass: String,

    // Campo opcional para recibir el ID en la respuesta
    val id: Int? = null
)