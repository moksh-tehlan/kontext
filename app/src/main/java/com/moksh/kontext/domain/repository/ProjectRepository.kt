package com.moksh.kontext.domain.repository

import com.moksh.kontext.domain.model.CreateProjectDto
import com.moksh.kontext.domain.model.ProjectDto
import com.moksh.kontext.domain.model.UpdateProjectDto
import com.moksh.kontext.domain.utils.DataError
import com.moksh.kontext.domain.utils.Result

interface ProjectRepository {

    suspend fun getAllProjects(): Result<List<ProjectDto>, DataError>

    suspend fun getProject(projectId: String): Result<ProjectDto, DataError>

    suspend fun createProject(createProjectDto: CreateProjectDto): Result<ProjectDto, DataError>

    suspend fun updateProject(
        projectId: String,
        updateProjectDto: UpdateProjectDto
    ): Result<ProjectDto, DataError>

    suspend fun deleteProject(projectId: String): Result<Unit, DataError>
}