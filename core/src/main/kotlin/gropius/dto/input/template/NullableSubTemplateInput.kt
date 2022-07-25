package gropius.dto.input.template

import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import com.fasterxml.jackson.databind.node.JsonNodeFactory
import com.networknt.schema.JsonSchema

@GraphQLDescription("Input to create a SubTemplate, where all templatedFieldSpecifications must allow null as value")
class NullableSubTemplateInput : SubTemplateInput() {

    override fun validateJsonSchema(schema: JsonSchema, name: String) {
        super.validateJsonSchema(schema, name)
        if (schema.validate(JsonNodeFactory.instance.nullNode()).isNotEmpty()) {
            throw IllegalArgumentException("TemplatedField $name must allow null")
        }
    }

}