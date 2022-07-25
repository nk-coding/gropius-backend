package gropius.schema.mutation

import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import com.expediagroup.graphql.generator.scalars.ID
import com.expediagroup.graphql.server.operations.Mutation
import graphql.schema.DataFetchingEnvironment
import gropius.authorization.gropiusAuthorizationContext
import gropius.dto.input.architecture.CreateComponentInput
import gropius.dto.input.architecture.CreateProjectInput
import gropius.dto.input.architecture.UpdateComponentInput
import gropius.dto.input.architecture.UpdateProjectInput
import gropius.dto.input.common.DeleteNodeInput
import gropius.graphql.AutoPayloadType
import gropius.model.architecture.Component
import gropius.model.architecture.Project
import gropius.service.architecture.ComponentService
import gropius.service.architecture.ProjectService

/**
 * Contains all architecture-related mutations
 *
 * @param componentService used for Component-related mutations
 * @param projectService used for Project-related mutations
 */
@org.springframework.stereotype.Component
class ArchitectureMutations(
    private val componentService: ComponentService,
    private val projectService: ProjectService
) : Mutation {

    @GraphQLDescription(
        """Creates a new Component, requires CAN_CREATE_COMPONENTS.
        Automatically generates a default ComponentPermission which grants the user READ and ADMIN
        """
    )
    @AutoPayloadType("The created Component")
    suspend fun createComponent(
        @GraphQLDescription("Defines the created Component")
        input: CreateComponentInput,
        dfe: DataFetchingEnvironment
    ): Component {
        return componentService.createComponent(dfe.gropiusAuthorizationContext, input)
    }

    @GraphQLDescription("Updates the specified Component, requires ADMIN on the component to update")
    @AutoPayloadType("The updated Component")
    suspend fun updateComponent(
        @GraphQLDescription("Defines which Component to update and how to update it")
        input: UpdateComponentInput,
        dfe: DataFetchingEnvironment
    ): Component {
        return componentService.updateComponent(dfe.gropiusAuthorizationContext, input)
    }

    @GraphQLDescription("Deletes the specified Component, requires ADMIN on the component to delete")
    @AutoPayloadType("The id of the deleted Component")
    suspend fun deleteComponent(
        @GraphQLDescription("Defines which Component to delete")
        input: DeleteNodeInput,
        dfe: DataFetchingEnvironment
    ): ID {
        componentService.deleteComponent(dfe.gropiusAuthorizationContext, input)
        return input.id
    }

    @GraphQLDescription(
        """Creates a new Project, requires CAN_CREATE_PROJECTS.
        Automatically generates a default ProjectPermission which grants the user READ and ADMIN
        """
    )
    @AutoPayloadType("The created Project")
    suspend fun createProject(
        @GraphQLDescription("Defines the created Project")
        input: CreateProjectInput,
        dfe: DataFetchingEnvironment
    ): Project {
        return projectService.createProject(dfe.gropiusAuthorizationContext, input)
    }

    @GraphQLDescription("Updates the specified Project, requires ADMIN on the project to update")
    @AutoPayloadType("The updated Project")
    suspend fun updateProject(
        @GraphQLDescription("Defines which Project to update and how to update it")
        input: UpdateProjectInput,
        dfe: DataFetchingEnvironment
    ): Project {
        return projectService.updateProject(dfe.gropiusAuthorizationContext, input)
    }

    @GraphQLDescription("Deletes the specified Project, requires ADMIN on the project to delete")
    @AutoPayloadType("The id of the deleted Project")
    suspend fun deleteProject(
        @GraphQLDescription("Defines which Project to delete")
        input: DeleteNodeInput,
        dfe: DataFetchingEnvironment
    ): ID {
        projectService.deleteProject(dfe.gropiusAuthorizationContext, input)
        return input.id
    }

}