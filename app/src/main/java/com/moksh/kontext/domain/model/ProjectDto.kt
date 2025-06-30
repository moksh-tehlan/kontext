package com.moksh.kontext.domain.model

data class ProjectDto(
    val id: String,
    val name: String,
    val description: String?,
    val createdAt: String,
    val updatedAt: String
)