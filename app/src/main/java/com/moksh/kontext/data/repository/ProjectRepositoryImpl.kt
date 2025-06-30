package com.moksh.kontext.data.repository

import com.moksh.kontext.data.api.ProjectApiService
import com.moksh.kontext.data.mapper.toDto
import com.moksh.kontext.data.mapper.toRequest
import com.moksh.kontext.data.utils.safeCall
import com.moksh.kontext.domain.model.CreateProjectDto
import com.moksh.kontext.domain.model.ProjectDto
import com.moksh.kontext.domain.model.UpdateProjectDto
import com.moksh.kontext.domain.repository.ProjectRepository
import com.moksh.kontext.domain.utils.DataError
import com.moksh.kontext.domain.utils.Result
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProjectRepositoryImpl @Inject constructor(
    private val projectApiService: ProjectApiService
) : ProjectRepository {

    override suspend fun getAllProjects(): Result<List<ProjectDto>, DataError> {
        return when (val result = safeCall { projectApiService.getAllProjects() }) {
            is Result.Success -> {
                result.data.data?.let { projects ->
                    Result.Success(projects.map { it.toDto() })
                } ?: Result.Error(DataError.Network.EMPTY_RESPONSE)
            }

            is Result.Error -> result
        }
    }

    override suspend fun getProject(projectId: String): Result<ProjectDto, DataError> {
        return when (val result = safeCall { projectApiService.getProject(projectId) }) {
            is Result.Success -> {
                result.data.data?.let { project ->
                    Result.Success(project.toDto())
                } ?: Result.Error(DataError.Network.EMPTY_RESPONSE)
            }

            is Result.Error -> result
        }
    }

    override suspend fun createProject(createProjectDto: CreateProjectDto): Result<ProjectDto, DataError> {
        return when (val result = safeCall {
            projectApiService.createProject(createProjectDto.toRequest())
        }) {
            is Result.Success -> {
                result.data.data?.let { project ->
                    Result.Success(project.toDto())
                } ?: Result.Error(DataError.Network.EMPTY_RESPONSE)
            }

            is Result.Error -> result
        }
    }

    override suspend fun updateProject(
        projectId: String,
        updateProjectDto: UpdateProjectDto
    ): Result<ProjectDto, DataError> {
        return when (val result = safeCall {
            projectApiService.updateProject(projectId, updateProjectDto.toRequest())
        }) {
            is Result.Success -> {
                result.data.data?.let { project ->
                    Result.Success(project.toDto())
                } ?: Result.Error(DataError.Network.EMPTY_RESPONSE)
            }

            is Result.Error -> result
        }
    }

    override suspend fun deleteProject(projectId: String): Result<Unit, DataError> {
        return when (val result = safeCall { projectApiService.deleteProject(projectId) }) {
            is Result.Success -> Result.Success(Unit)
            is Result.Error -> result
        }
    }
}