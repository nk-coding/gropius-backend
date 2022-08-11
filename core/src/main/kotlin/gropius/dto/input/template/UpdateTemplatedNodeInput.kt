package gropius.dto.input.template

import com.expediagroup.graphql.generator.execution.OptionalInput
import gropius.dto.input.common.JSONFieldInput
import gropius.model.template.TemplatedNode

/**
 * Shared fields all update mutation inputs of subclasses of [TemplatedNode] have
 */
interface UpdateTemplatedNodeInput {

    /**
     * Initial value for the templated fields
     */
    val templatedFields: OptionalInput<List<JSONFieldInput>>

}