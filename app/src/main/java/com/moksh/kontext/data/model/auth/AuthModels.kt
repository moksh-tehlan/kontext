package com.moksh.kontext.data.model.auth

import com.google.gson.annotations.SerializedName
import com.moksh.kontext.data.model.user.User

data class SendOtpRequest(
    @SerializedName("email")
    val email: String
)

data class LoginRequest(
    @SerializedName("email")
    val email: String,
    @SerializedName("otp")
    val otp: String
)

data class RefreshTokenRequest(
    @SerializedName("refreshToken")
    val refreshToken: String
)

data class GoogleLoginRequest(
    @SerializedName("idToken")
    val idToken: String
)

data class AuthResponse(
    @SerializedName("accessToken")
    val accessToken: String,
    @SerializedName("refreshToken")
    val refreshToken: String,
    @SerializedName("tokenType")
    val tokenType: String,
    @SerializedName("expiresIn")
    val expiresIn: Int,
    @SerializedName("user")
    val user: User
)