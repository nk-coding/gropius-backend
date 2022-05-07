package gropius.model.template

import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import gropius.model.issue.Artefact
import io.github.graphglue.model.DomainNode

@DomainNode("artefactTemplates")
@GraphQLDescription(
    """Template for Artefacts
    Defines templated fields with specific types (defined using JSON schema).
    """
)
class ArtefactTemplate(
    name: String, description: String, isDeprecated: Boolean
) : Template<Artefact, ArtefactTemplate>(name, description, isDeprecated)