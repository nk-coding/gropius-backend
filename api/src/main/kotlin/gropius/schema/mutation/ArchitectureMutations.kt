package gropius.schema.mutation

import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import com.expediagroup.graphql.generator.scalars.ID
import com.expediagroup.graphql.server.operations.Mutation
import graphql.schema.DataFetchingEnvironment
import gropius.authorization.gropiusAuthorizationContext
import gropius.dto.input.architecture.CreateComponentInput
import gropius.dto.input.architecture.UpdateComponentInput
import gropius.dto.input.common.DeleteNodeInput
import gropius.graphql.AutoPayloadType
import gropius.model.architecture.Component
import gropius.service.architecture.ComponentService

/**
 * Contains all architecture-related mutations
 *
 * @param componentService used for component-related mutations
 */
@org.springframework.stereotype.Component
class ArchitectureMutations(private val componentService: ComponentService) : Mutation {

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

}