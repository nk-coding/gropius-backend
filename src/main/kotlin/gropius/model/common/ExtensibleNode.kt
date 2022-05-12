package gropius.model.common

import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import com.expediagroup.graphql.generator.annotations.GraphQLType
import io.github.graphglue.model.DomainNode
import io.github.graphglue.model.Node

@DomainNode
@GraphQLDescription("Entity which provides dynamic extension fields.")
abstract class ExtensibleNode : Node() {

    @GraphQLDescription("Value of an extension field by name of the extension field.")
    @GraphQLType("JSON")
    fun extensionField(
        @GraphQLDescription("Name of the extension field")
        name: String
    ): Any? {
        TODO()
    }

}