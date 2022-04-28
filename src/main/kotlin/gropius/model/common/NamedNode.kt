package gropius.model.common

import io.github.graphglue.model.DomainNode

@DomainNode
abstract class NamedNode(
    override var name: String, override var description: String
) : ExtensibleNode(), Named