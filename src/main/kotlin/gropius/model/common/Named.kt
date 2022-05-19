package gropius.model.common

import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import io.github.graphglue.model.FilterProperty
import io.github.graphglue.model.OrderProperty

@GraphQLDescription("Entity with a name and a description.")
interface Named {
    @GraphQLDescription("The name of this entity.")
    @FilterProperty
    @OrderProperty
    var name: String

    @GraphQLDescription("The description of this entity.")
    @FilterProperty
    var description: String
}