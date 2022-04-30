package gropius.model.template

import gropius.model.common.NamedNode
import io.github.graphglue.model.*
import org.springframework.data.annotation.Transient

// TODO templateFieldSpecifications
@DomainNode
abstract class Template<T : Node, S : Template<T, S>>(
    name: String,
    description: String,
    @FilterProperty var isDeprecated: Boolean
) : NamedNode(name, description) {

    companion object {
        const val USED_IN = "USED_IN"
        const val EXTENDS = "EXTENDS"
    }

    @NodeRelationship(USED_IN, Direction.OUTGOING)
    @delegate:Transient
    val usedIn by NodeSetProperty<T>()

    @NodeRelationship(EXTENDS, Direction.OUTGOING)
    @delegate:Transient
    val extends by NodeSetProperty<S>()

    @NodeRelationship(EXTENDS, Direction.INCOMING)
    @delegate:Transient
    val extendedBy by NodeSetProperty<S>()
}