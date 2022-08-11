package gropius.model.template

import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import com.expediagroup.graphql.generator.annotations.GraphQLIgnore
import com.expediagroup.graphql.generator.annotations.GraphQLType
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import io.github.graphglue.model.property.LazyLoadingDelegate
import io.github.graphglue.model.property.NodePropertyDelegate
import org.springframework.beans.factory.annotation.Autowired

@GraphQLDescription("Interface for all types which support templates.")
interface TemplatedNode {

    /**
     * The values for all templated fields. The schema is defined by the [Template] of this node
     */
    @GraphQLIgnore
    val templatedFields: MutableMap<String, String>

    /**
     * Template associated with the TemplatedNode
     */
    @GraphQLIgnore
    val template: LazyLoadingDelegate<out BaseTemplate<*, *>, out NodePropertyDelegate<out BaseTemplate<*, *>>.NodeProperty>

    @GraphQLDescription("Value of a field defined by the template. Error if such a field is not defined.")
    @GraphQLType("JSON")
    fun templatedField(
        @GraphQLDescription("Name of the extension field")
        name: String,
        @Autowired
        @GraphQLIgnore
        objectMapper: ObjectMapper
    ): JsonNode? {
        return templatedFields[name]?.let { objectMapper.readTree(it) }
            ?: throw IllegalArgumentException("No field found for name $name")
    }

    @GraphQLDescription("All templated fields, if a `namePrefix` is provided, only those matching it")
    fun templatedFields(
        @GraphQLDescription("Name of the templated field.")
        namePrefix: String? = null,
        @Autowired
        @GraphQLIgnore
        objectMapper: ObjectMapper
    ): List<JSONField> {
        val fields = if (namePrefix != null) {
            templatedFields.filter { it.key.startsWith(namePrefix) }
        } else {
            templatedFields
        }
        return fields.entries.map { JSONField(it.key, objectMapper.readTree(it.value)) }
    }
}