package com.example.apponlykick.data.model

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("user") val user: UserDto,
    @SerializedName("token") val token: String
)