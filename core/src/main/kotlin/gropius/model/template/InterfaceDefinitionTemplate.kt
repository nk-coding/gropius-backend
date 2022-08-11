package gropius.model.template

import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import gropius.model.architecture.InterfaceDefinition
import io.github.graphglue.model.DomainNode

@DomainNode
@GraphQLDescription(
    """SubTemplate for InterfaceDefinition.
    Part of a InterfaceSpecificationTemplate.
    Defines templated fields with specific types (defined using JSON schema).
    All templatedFieldSpecifications must allow `null` as value.
    """
)
class InterfaceDefinitionTemplate(
    name: String, description: String, templateFieldSpecifications: MutableMap<String, String>
) : SubTemplate<InterfaceDefinition, InterfaceSpecificationTemplate, InterfaceDefinitionTemplate>(
    name, description, templateFieldSpecifications
)