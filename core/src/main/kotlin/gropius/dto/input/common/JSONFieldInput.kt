package gropius.dto.input.common

import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import com.expediagroup.graphql.generator.annotations.GraphQLType

@GraphQLDescription("Input set update the value of a JSON field, like an extension field or a templated field.")
class JSONFieldInput(
    @GraphQLDescription("The name of the field")
    val name: String,
    @GraphQLDescription("The new value of the field")
    @GraphQLType("JSON")
    val value: Any?
)