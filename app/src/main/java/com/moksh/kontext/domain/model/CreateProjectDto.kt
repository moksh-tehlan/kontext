package com.moksh.kontext.domain.model

data class CreateProjectDto(
    val name: String,
    val description: String? = null
)