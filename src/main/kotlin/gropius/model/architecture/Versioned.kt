package gropius.model.architecture

import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import io.github.graphglue.model.FilterProperty
import io.github.graphglue.model.OrderProperty

@GraphQLDescription("Entity with a version")
interface Versioned {
    @GraphQLDescription("The version of this entity")
    @FilterProperty
    @OrderProperty
    var version: String
}