package gropius.model.template

import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import com.expediagroup.graphql.generator.annotations.GraphQLIgnore

@GraphQLDescription("Interface for all types which support templates describing user writeable fields.")
interface MutableTemplatedNode : TemplatedNode {
    /**
     * Dummy function to avoid GraphQL kotlin optimization weirdness
     */
    @GraphQLIgnore
    private fun graphQLKotlinDummyFunction() {
    }
}