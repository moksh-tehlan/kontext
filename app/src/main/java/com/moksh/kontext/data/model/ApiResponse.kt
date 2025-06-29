package com.moksh.kontext.data.model

import com.google.gson.annotations.SerializedName

data class ApiResponse<T>(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("message")
    val message: String,
    @SerializedName("data")
    val data: T? = null,
    @SerializedName("status")
    val status: Int,
    @SerializedName("timestamp")
    val timestamp: String
)

data class ErrorResponse(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("message")
    val message: String,
    @SerializedName("error")
    val error: String? = null,
    @SerializedName("path")
    val path: String? = null,
    @SerializedName("status")
    val status: Int,
    @SerializedName("timestamp")
    val timestamp: String
)
