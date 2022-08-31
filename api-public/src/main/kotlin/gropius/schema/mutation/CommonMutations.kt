package gropius.schema.mutation

import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import com.expediagroup.graphql.generator.annotations.GraphQLIgnore
import com.expediagroup.graphql.server.operations.Mutation
import graphql.schema.DataFetchingEnvironment
import gropius.authorization.gropiusAuthorizationContext
import gropius.dto.input.common.UpdateExtensionFieldsInput
import gropius.graphql.AutoPayloadType
import gropius.model.common.ExtensibleNode
import gropius.service.common.ExtensibleNodeService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

/**
 * Contains all mutations not related to a specific subgroup of mutations
 */
@Component
@Transactional(propagation = Propagation.REQUIRES_NEW)
class CommonMutations : Mutation {

    @GraphQLDescription("Updates extensionFields of the specified ExtensibleNode. Requires READ")
    @AutoPayloadType("The updated ExtensibleNode")
    suspend fun updateExtensionFields(
        @GraphQLDescription("Defines the ExtensibleNode to update and changed extensionFields")
        input: UpdateExtensionFieldsInput,
        dfe: DataFetchingEnvironment,
        @GraphQLIgnore
        @Autowired
        extensibleNodeService: ExtensibleNodeService
    ): ExtensibleNode {
        return extensibleNodeService.updateExtensionFields(dfe.gropiusAuthorizationContext, input)
    }

}