package gropius.model.template

import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import gropius.model.architecture.InterfaceSpecificationVersion
import io.github.graphglue.model.DomainNode

@DomainNode
@GraphQLDescription(
    """SubTemplate for InterfaceSpecificationVersion.
    Part of a InterfaceSpecificationTemplate.
    Defines templated fields with specific types (defined using JSON schema).
    """
)
class InterfaceSpecificationVersionTemplate(
    name: String, description: String, templateFieldSpecifications: MutableMap<String, String>
) : SubTemplate<InterfaceSpecificationVersion, InterfaceSpecificationTemplate, InterfaceSpecificationVersionTemplate>(
    name, description, templateFieldSpecifications
)