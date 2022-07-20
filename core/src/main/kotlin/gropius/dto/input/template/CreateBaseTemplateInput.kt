package gropius.dto.input.template

import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import com.expediagroup.graphql.generator.execution.OptionalInput
import gropius.dto.input.common.JSONFieldInput
import gropius.dto.input.common.UpdateNamedNodeInput
import gropius.model.template.BaseTemplate
import kotlin.properties.Delegates

/**
 * Fragment for create mutation inputs for classes extending [BaseTemplate]
 */
abstract class CreateBaseTemplateInput : UpdateNamedNodeInput() {

    @GraphQLDescription("The initial value of the templateFieldSpecifications, should be a JSON schema JSON")
    var templateFieldSpecifications: OptionalInput<List<JSONFieldInput>> by Delegates.notNull()

}