package com.moksh.kontext.data.model.project

import com.google.gson.annotations.SerializedName

data class Project(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("description")
    val description: String?,
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
    val isActive: Boolean
)