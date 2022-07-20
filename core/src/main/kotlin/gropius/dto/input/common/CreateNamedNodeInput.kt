package gropius.dto.input.common

import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import gropius.model.common.NamedNode
import kotlin.properties.Delegates

/**
 * Fragment for create mutation inputs for classes extending [NamedNode]
 */
abstract class CreateNamedNodeInput : UpdateExtensibleNodeInput() {

    @GraphQLDescription("The name of the NamedNode, must not be empty")
    var name: String by Delegates.notNull()

    @GraphQLDescription("The description of the NamedNode")
    var description: String by Delegates.notNull()

}