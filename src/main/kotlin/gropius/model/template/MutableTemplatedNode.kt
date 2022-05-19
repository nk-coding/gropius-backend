package gropius.model.template

import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import com.expediagroup.graphql.generator.annotations.GraphQLIgnore

@GraphQLDescription("Interface for all types which support templates that can be written by the user.")
interface MutableTemplatedNode : TemplatedNode {
    @GraphQLIgnore
    private fun graphQLKotlinDummyFunction() {
        // Dummy function to avoid GraphQL kotlin optimization weirdness
    }
}