package com.moksh.kontext.data.model.user

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("id")
    val id: String,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("updatedAt")
    val updatedAt: String,
    @SerializedName("createdBy")
    val createdBy: String,
    @SerializedName("updatedBy")
    val updatedBy: String,
    @SerializedName("version")
    val version: Int,
    @SerializedName("isActive")
    val isActive: Boolean,
    @SerializedName("email")
    val email: String,
    @SerializedName("firstName")
    val firstName: String,
    @SerializedName("lastName")
    val lastName: String,
    @SerializedName("authProvider")
    val authProvider: String,
    @SerializedName("isEmailVerified")
    val isEmailVerified: Boolean,
    @SerializedName("role")
    val role: String
)