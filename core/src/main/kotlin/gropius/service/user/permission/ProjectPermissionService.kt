package gropius.service.user.permission

import gropius.model.architecture.Project
import gropius.model.user.GropiusUser
import gropius.model.user.permission.ProjectPermission
import gropius.repository.user.permission.ProjectPermissionRepository
import org.springframework.stereotype.Service

/**
 * Service for [ProjectPermission]s. Provides functions to create, update and delete
 *
 * @param repository the associated repository used for CRUD functionality
 */
@Service
class ProjectPermissionService(repository: ProjectPermissionRepository) :
    TrackablePermissionService<ProjectPermission, ProjectPermissionRepository>(repository) {

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

}