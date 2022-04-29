package gropius.model.template

import gropius.model.issue.Artefact
import io.github.graphglue.model.DomainNode

@DomainNode
class ArtefactTemplate(name: String, description: String) : Template<Artefact, ArtefactTemplate>(name, description)