package com.moksh.kontext.domain.model

data class UpdateProjectDto(
    val name: String? = null,
    val description: String? = null,
    val agentInstruction: String? = null
)