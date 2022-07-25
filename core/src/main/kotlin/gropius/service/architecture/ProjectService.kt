package gropius.service.architecture

import gropius.authorization.GropiusAuthorizationContext
import gropius.dto.input.architecture.CreateProjectInput
import gropius.dto.input.architecture.UpdateProjectInput
import gropius.dto.input.common.DeleteNodeInput
import gropius.model.architecture.Project
import gropius.model.user.permission.GlobalPermission
import gropius.model.user.permission.NodePermission
import gropius.repository.architecture.ProjectRepository
import gropius.repository.findById
import gropius.service.user.permission.ProjectPermissionService
import io.github.graphglue.authorization.Permission
import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.stereotype.Service

/**
 * Service for [Project]s. Provides functions to create, update and delete
 *
 * @param repository the associated repository used for CRUD functionality
 * @param projectPermissionService used to create the initial permission for a created [Project]
 */
@Service
class ProjectService(repository: ProjectRepository, val projectPermissionService: ProjectPermissionService) :
    TrackableService<Project, ProjectRepository>(repository) {

    /**
     * Creates a new [Project] based on the provided [input]
     * Checks the authorization status
     *
     * @param authorizationContext used to check for the required permission
     * @param input defines the [Project]
     * @return the saved created [Project]
     */
    suspend fun createProject(
        authorizationContext: GropiusAuthorizationContext, input: CreateProjectInput
    ): Project {
        input.validate()
        val user = getUser(authorizationContext)
        checkPermission(
            user, Permission(GlobalPermission.CAN_CREATE_PROJECTS, authorizationContext), "create Projects"
        )
        val project = Project(input.name, input.description, input.repositoryURL)
        createdExtensibleNode(project, input)
        projectPermissionService.createDefaultPermission(user, project)
        return repository.save(project).awaitSingle()
    }

    /**
     * Updates a [Project] based on the provided [input]
     * Checks the authorization status
     *
     * @param authorizationContext used to check for the required permission
     * @param input defines which [Project] to update and how
     * @return the updated [Project]
     */
    suspend fun updateProject(
        authorizationContext: GropiusAuthorizationContext, input: UpdateProjectInput
    ): Project {
        input.validate()
        val project = repository.findById(input.id)
        checkPermission(
            project, Permission(NodePermission.ADMIN, authorizationContext), "update the Project"
        )
        updateTrackable(project, input)
        return repository.save(project).awaitSingle()
    }

    /**
     * Deletes a [Project] by id
     * Checks the authorization status
     *
     * @param authorizationContext used to check for the required permission
     * @param input defines which [Project] to delete
     */
    suspend fun deleteProject(
        authorizationContext: GropiusAuthorizationContext, input: DeleteNodeInput
    ) {
        input.validate()
        val project = repository.findById(input.id)
        checkPermission(
            project, Permission(NodePermission.ADMIN, authorizationContext), "delete the Project"
        )
        beforeDeleteTrackable(project)
        repository.delete(project).awaitSingle()
    }

}