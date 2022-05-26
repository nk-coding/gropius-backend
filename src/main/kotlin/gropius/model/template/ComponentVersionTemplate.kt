package gropius.model.template

import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import gropius.model.architecture.ComponentVersion
import io.github.graphglue.model.DomainNode

@DomainNode
@GraphQLDescription(
    """SubTemplate for ComponentVersion.
    Part of a ComponentTemplate.
    Defines templated fields with specific types (defined using JSON schema).
    """
)
class ComponentVersionTemplate(
    name: String, description: String, templateFieldSpecifications: MutableMap<String, String>
) : SubTemplate<ComponentVersion, ComponentTemplate, ComponentVersionTemplate>(
    name, description, templateFieldSpecifications
)