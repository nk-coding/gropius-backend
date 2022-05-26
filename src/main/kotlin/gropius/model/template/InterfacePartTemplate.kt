package gropius.model.template

import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import gropius.model.architecture.InterfacePart
import io.github.graphglue.model.DomainNode

@DomainNode
@GraphQLDescription(
    """SubTemplate for InterfacePart.
    Part of a InterfaceSpecificationTemplate.
    Defines templated fields with specific types (defined using JSON schema).
    """
)
class InterfacePartTemplate(
    name: String, description: String, templateFieldSpecifications: MutableMap<String, String>
) : SubTemplate<InterfacePart, InterfaceSpecificationTemplate, InterfacePartTemplate>(
    name, description, templateFieldSpecifications
)