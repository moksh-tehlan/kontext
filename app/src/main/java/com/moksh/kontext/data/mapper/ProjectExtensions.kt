package com.moksh.kontext.data.mapper

import com.moksh.kontext.data.model.project.CreateProjectRequest
import com.moksh.kontext.data.model.project.Project
import com.moksh.kontext.data.model.project.UpdateProjectRequest
import com.moksh.kontext.domain.model.CreateProjectDto
import com.moksh.kontext.domain.model.ProjectDto
import com.moksh.kontext.domain.model.UpdateProjectDto

fun Project.toDto(): ProjectDto {
    return ProjectDto(
        id = id,
        name = name,
        description = description,
        agentInstruction = agentInstruction,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

fun CreateProjectDto.toRequest(): CreateProjectRequest {
    return CreateProjectRequest(
        name = name,
        description = description
    )
}

fun UpdateProjectDto.toRequest(): UpdateProjectRequest {
    return UpdateProjectRequest(
        name = name,
        description = description,
        agentInstruction = agentInstruction
    )
}