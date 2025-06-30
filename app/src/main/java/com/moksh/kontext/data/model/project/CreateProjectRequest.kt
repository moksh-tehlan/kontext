package com.moksh.kontext.data.model.project

import com.google.gson.annotations.SerializedName

data class CreateProjectRequest(
    @SerializedName("name")
    val name: String,
    @SerializedName("description")
    val description: String? = null
)