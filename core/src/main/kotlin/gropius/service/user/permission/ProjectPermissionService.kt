package gropius.service.user.permission

import gropius.authorization.GropiusAuthorizationContext
import gropius.dto.input.user.permission.CreateProjectPermissionInput
import gropius.model.architecture.Project
import gropius.model.user.GropiusUser
import gropius.model.user.permission.ProjectPermission
import gropius.repository.architecture.ProjectRepository
import gropius.repository.findAllById
import gropius.repository.user.permission.ProjectPermissionRepository
import org.springframework.stereotype.Service

/**
 * Service for [ProjectPermission]s. Provides functions to create, update and delete
 *
 * @param repository the associated repository used for CRUD functionality
 * @param projectRepository used to get [Project]s by id
 */
@Service
class ProjectPermissionService(
    repository: ProjectPermissionRepository,
    private val projectRepository: ProjectRepository
) : TrackablePermissionService<ProjectPermission, Project, ProjectPermissionRepository>(repository) {

    /**
     * Creates the default [ProjectPermission]
     * The created permission grants ADMIN and READ only to the provided [user]
     * Does not check permissions or save the created permission / [project]
     *
     * @param user the user who is granted the permission
     * @param project used to add the permission to
     */
    suspend fun createDefaultPermission(
        user: GropiusUser, project: Project
    ) {
        project.permissions() += createDefaultPermission(user, ::ProjectPermission)
    }

    /**
     * Creates a new [ProjectPermission] based on the provided [input]
     * Checks the authorization status
     *
     * @param authorizationContext used to check for the required permission
     * @param input defines the [ProjectPermission]
     * @return the saved created [ProjectPermission]
     */
    suspend fun createProjectPermission(
        authorizationContext: GropiusAuthorizationContext, input: CreateProjectPermissionInput
    ): ProjectPermission {
        return createNodePermission(
            authorizationContext,
            input,
            projectRepository.findAllById(input.nodesWithPermission),
            ::ProjectPermission
        )
    }

}