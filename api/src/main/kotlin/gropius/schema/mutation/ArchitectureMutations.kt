package gropius.schema.mutation

import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import com.expediagroup.graphql.generator.scalars.ID
import com.expediagroup.graphql.server.operations.Mutation
import graphql.schema.DataFetchingEnvironment
import gropius.authorization.gropiusAuthorizationContext
import gropius.dto.input.architecture.*
import gropius.dto.input.common.DeleteNodeInput
import gropius.graphql.AutoPayloadType
import gropius.model.architecture.*
import gropius.service.architecture.*

/**
 * Contains all architecture-related mutations
 *
 * @param componentService used for Component-related mutations
 * @param projectService used for Project-related mutations
 * @param interfaceSpecificationService used for InterfaceSpecification-related mutations
 * @param interfaceSpecificationVersionService used for InterfaceSpecificationVersion-related mutations
 * @param interfacePartService used for InterfacePart-related mutations
 */
@org.springframework.stereotype.Component
class ArchitectureMutations(
    private val componentService: ComponentService,
    private val projectService: ProjectService,
    private val interfaceSpecificationService: InterfaceSpecificationService,
    private val interfaceSpecificationVersionService: InterfaceSpecificationVersionService,
    private val interfacePartService: InterfacePartService
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

    @GraphQLDescription("Creates a new InterfaceSpecification, requires ADMIN on the Component.")
    @AutoPayloadType("The created InterfaceSpecification")
    suspend fun createInterfaceSpecification(
        @GraphQLDescription("Defines the created InterfaceSpecification")
        input: CreateInterfaceSpecificationInput,
        dfe: DataFetchingEnvironment
    ): InterfaceSpecification {
        return interfaceSpecificationService.createInterfaceSpecification(dfe.gropiusAuthorizationContext, input)
    }

    @GraphQLDescription("Updates the specified InterfaceSpecification, requires ADMIN on the Component of the InterfaceSpecification to update")
    @AutoPayloadType("The updated InterfaceSpecification")
    suspend fun updateInterfaceSpecification(
        @GraphQLDescription("Defines which InterfaceSpecification to update and how to update it")
        input: UpdateInterfaceSpecificationInput,
        dfe: DataFetchingEnvironment
    ): InterfaceSpecification {
        return interfaceSpecificationService.updateInterfaceSpecification(dfe.gropiusAuthorizationContext, input)
    }

    @GraphQLDescription("Deletes the specified InterfaceSpecification, requires ADMIN on the Component of the InterfaceSpecification to delete")
    @AutoPayloadType("The id of the deleted InterfaceSpecification")
    suspend fun deleteInterfaceSpecification(
        @GraphQLDescription("Defines which InterfaceSpecification to delete")
        input: DeleteNodeInput,
        dfe: DataFetchingEnvironment
    ): ID {
        interfaceSpecificationService.deleteInterfaceSpecification(dfe.gropiusAuthorizationContext, input)
        return input.id
    }

    @GraphQLDescription("Creates a new InterfaceSpecificationVersion, requires ADMIN on the Component.")
    @AutoPayloadType("The created InterfaceSpecificationVersion")
    suspend fun createInterfaceSpecificationVersion(
        @GraphQLDescription("Defines the created InterfaceSpecificationVersion")
        input: CreateInterfaceSpecificationVersionInput,
        dfe: DataFetchingEnvironment
    ): InterfaceSpecificationVersion {
        return interfaceSpecificationVersionService.createInterfaceSpecificationVersion(dfe.gropiusAuthorizationContext, input)
    }

    @GraphQLDescription("Updates the specified InterfaceSpecificationVersion, requires ADMIN on the Component of the InterfaceSpecificationVersion to update")
    @AutoPayloadType("The updated InterfaceSpecificationVersion")
    suspend fun updateInterfaceSpecificationVersion(
        @GraphQLDescription("Defines which InterfaceSpecificationVersion to update and how to update it")
        input: UpdateInterfaceSpecificationVersionInput,
        dfe: DataFetchingEnvironment
    ): InterfaceSpecificationVersion {
        return interfaceSpecificationVersionService.updateInterfaceSpecificationVersion(dfe.gropiusAuthorizationContext, input)
    }

    @GraphQLDescription("Deletes the specified InterfaceSpecificationVersion, requires ADMIN on the Component of the InterfaceSpecificationVersion to delete")
    @AutoPayloadType("The id of the deleted InterfaceSpecificationVersion")
    suspend fun deleteInterfaceSpecificationVersion(
        @GraphQLDescription("Defines which InterfaceSpecificationVersion to delete")
        input: DeleteNodeInput,
        dfe: DataFetchingEnvironment
    ): ID {
        interfaceSpecificationVersionService.deleteInterfaceSpecificationVersion(dfe.gropiusAuthorizationContext, input)
        return input.id
    }

    @GraphQLDescription("Creates a new InterfacePart, requires ADMIN on the Component.")
    @AutoPayloadType("The created InterfacePart")
    suspend fun createInterfacePart(
        @GraphQLDescription("Defines the created InterfacePart")
        input: CreateInterfacePartInput,
        dfe: DataFetchingEnvironment
    ): InterfacePart {
        return interfacePartService.createInterfacePart(dfe.gropiusAuthorizationContext, input)
    }

    @GraphQLDescription("Updates the specified InterfacePart, requires ADMIN on the Component of the InterfacePart to update")
    @AutoPayloadType("The updated InterfacePart")
    suspend fun updateInterfacePart(
        @GraphQLDescription("Defines which InterfacePart to update and how to update it")
        input: UpdateInterfacePartInput,
        dfe: DataFetchingEnvironment
    ): InterfacePart {
        return interfacePartService.updateInterfacePart(dfe.gropiusAuthorizationContext, input)
    }

    @GraphQLDescription("Deletes the specified InterfacePart, requires ADMIN on the Component of the InterfacePart to delete")
    @AutoPayloadType("The id of the deleted InterfacePart")
    suspend fun deleteInterfacePart(
        @GraphQLDescription("Defines which InterfacePart to delete")
        input: DeleteNodeInput,
        dfe: DataFetchingEnvironment
    ): ID {
        interfacePartService.deleteInterfacePart(dfe.gropiusAuthorizationContext, input)
        return input.id
    }

}