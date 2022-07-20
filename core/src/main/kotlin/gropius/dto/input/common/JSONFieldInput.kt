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

/**
 * Ensures that a List of [JSONFieldInput] does not contain duplicate names
 *
 * @throws IllegalStateException if there are duplicate names
 */
fun List<JSONFieldInput>.ensureNoDuplicates() {
    val duplicates = this.groupingBy { it.name }.eachCount().filter { it.value > 1 }.keys
    if (duplicates.isNotEmpty()) {
        throw IllegalStateException("Duplicate names found: $duplicates")
    }
}