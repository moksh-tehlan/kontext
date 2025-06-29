package com.moksh.kontext.data.model.user

import com.google.gson.annotations.SerializedName

data class UpdateUserRequest(
    @SerializedName("nickname")
    val nickname: String? = null,
    @SerializedName("email")
    val email: String? = null,
    @SerializedName("firstName")
    val firstName: String? = null,
    @SerializedName("lastName")
    val lastName: String? = null,
    @SerializedName("profilePictureUrl")
    val profilePictureUrl: String? = null,
    @SerializedName("role")
    val role: String? = null
)