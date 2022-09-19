package gropius.graphql

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import io.github.graphglue.connection.filter.model.FilterEntry
import org.neo4j.cypherdsl.core.Condition
import org.neo4j.cypherdsl.core.Conditions
import org.neo4j.cypherdsl.core.Cypher
import org.neo4j.cypherdsl.core.Node

/**
 * Parsed filter entry of a [TemplatedFieldsFilterEntryDefinition]
 *
 * @param value the value provided by the user
 * @param objectMapper used to serialize [JsonNode]s
 * @param definition [TemplatedFieldsFilterEntryDefinition] used to create this entry
 */
class TemplatedFieldsFilterEntry(
    private val value: List<*>, private val objectMapper: ObjectMapper, definition: TemplatedFieldsFilterEntryDefinition
) : FilterEntry(definition) {

    override fun generateCondition(node: Node): Condition {
        return if (value.isEmpty()) {
            Conditions.isTrue()
        } else {
            value.fold(Conditions.noCondition()) { condition, entry ->
                entry as Map<*, *>
                val property = node.property("templatedFields.${entry["name"] as String}")
                val propertyValue = Cypher.anonParameter(objectMapper.writeValueAsString(entry["value"] as JsonNode))
                condition.and(property.isEqualTo(propertyValue))
            }
        }
    }

}