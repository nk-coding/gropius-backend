package gropius.model.template

import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import com.expediagroup.graphql.generator.annotations.GraphQLType

@GraphQLDescription("A JSON extension field, consisting of a name and a value.")
class JSONField(
    @GraphQLDescription("The name of the field, used as unique identifier.")
    val name: String,
    @GraphQLDescription("The value of the JSON field, might be null.")
    @GraphQLType("JSON")
    val value: Any?
)