package gropius.dto.input.template

import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import com.expediagroup.graphql.generator.execution.OptionalInput
import com.fasterxml.jackson.databind.JsonNode
import com.networknt.schema.JsonSchemaFactory
import com.networknt.schema.JsonValidator
import com.networknt.schema.SpecVersionDetector
import gropius.dto.input.common.CreateNamedNodeInput
import gropius.dto.input.common.JSONFieldInput
import gropius.dto.input.common.UpdateNamedNodeInput
import gropius.dto.input.common.ensureNoDuplicates
import gropius.dto.input.ifPresent
import gropius.model.template.BaseTemplate
import kotlin.properties.Delegates

/**
 * Fragment for create mutation inputs for classes extending [BaseTemplate]
 */
abstract class CreateBaseTemplateInput : CreateNamedNodeInput() {

    @GraphQLDescription("The initial value of the templateFieldSpecifications, should be a JSON schema JSON")
    var templateFieldSpecifications: OptionalInput<List<JSONFieldInput>> by Delegates.notNull()

    override fun validate() {
        super.validate()
        templateFieldSpecifications.ifPresent {
            for (field in it) {
                val schema = field.value as JsonNode
                JsonSchemaFactory.getInstance(SpecVersionDetector.detect(field.value)).getSchema(schema)
            }
            it.ensureNoDuplicates()
        }
    }
}