package gropius.dto.input.architecture

import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import gropius.dto.input.common.CreateNamedNodeInput
import gropius.model.architecture.Trackable
import java.net.URI

/**
 * Fragment for create mutation inputs for classes extending [Trackable]
 */
abstract class CreateTrackableInput : CreateNamedNodeInput() {

    @GraphQLDescription("The repositoryURL of the named node")
    var repositoryURL: URI? = null

}