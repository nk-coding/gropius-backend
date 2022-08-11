package gropius.dto.input.architecture

import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import com.expediagroup.graphql.generator.execution.OptionalInput
import gropius.dto.input.common.UpdateNamedNodeInput
import gropius.model.architecture.Trackable
import java.net.URI
import kotlin.properties.Delegates

/**
 * Fragment for update mutation inputs for classes extending [Trackable]
 */
abstract class UpdateTrackableInput : UpdateNamedNodeInput() {

    @GraphQLDescription("The repositoryURL of the named node")
    var repositoryURL: OptionalInput<URI?> by Delegates.notNull()

}