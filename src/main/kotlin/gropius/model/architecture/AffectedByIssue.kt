package gropius.model.architecture

import gropius.model.common.NamedNode
import gropius.model.issue.Issue
import io.github.graphglue.model.Direction
import io.github.graphglue.model.DomainNode
import io.github.graphglue.model.FilterProperty
import io.github.graphglue.model.NodeRelationship
import org.springframework.data.annotation.Transient

@DomainNode
abstract class AffectedByIssue(name: String, description: String) : NamedNode(name, description) {

    companion object {
        const val AFFECTS = "AFFECTS"
    }

    @NodeRelationship(AFFECTS, Direction.OUTGOING)
    @FilterProperty
    @delegate:Transient
    val affectingIssues by NodeSetProperty<Issue>()

}