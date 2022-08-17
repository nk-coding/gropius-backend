package gropius.dto.input.template

import gropius.dto.input.common.JSONFieldInput
import gropius.model.template.TemplatedNode

/**
 * Shared fields all create mutation inputs of subclasses of [TemplatedNode] have
 */
interface CreateTemplatedNodeInput {

    /**
     * Initial value for the templated fields
     */
    val templatedFields: List<JSONFieldInput>

}