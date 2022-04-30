package gropius.model.common

import com.expediagroup.graphql.generator.annotations.GraphQLType
import io.github.graphglue.model.DomainNode
import io.github.graphglue.model.Node

@DomainNode
abstract class ExtensibleNode : Node() {
    @GraphQLType("JSON")
    fun extensionField(name: String): Any {
        TODO()
    }

}