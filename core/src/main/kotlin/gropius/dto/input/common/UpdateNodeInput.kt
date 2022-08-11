package gropius.dto.input.common

import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import com.expediagroup.graphql.generator.scalars.ID
import io.github.graphglue.model.Node
import kotlin.properties.Delegates

/**
 * Fragment for update mutation inputs for classes extending [Node]
 */
abstract class UpdateNodeInput : Input() {

    @GraphQLDescription("The id of the node to update")
    var id: ID by Delegates.notNull()

}