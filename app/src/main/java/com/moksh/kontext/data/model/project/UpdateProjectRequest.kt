package com.moksh.kontext.data.model.project

import com.google.gson.annotations.SerializedName

data class UpdateProjectRequest(
    @SerializedName("name")
    val name: String? = null,
    @SerializedName("description")
    val description: String? = null
)