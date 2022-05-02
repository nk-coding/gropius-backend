package gropius.model.architecture

import gropius.model.issue.Artefact
import gropius.model.issue.Issue
import gropius.model.issue.Label
import io.github.graphglue.model.*
import java.net.URL
import org.springframework.data.annotation.Transient

@DomainNode
abstract class Trackable(name: String, description: String, @FilterProperty var repositoryURL: URL) :
    AffectedByIssue(name, description) {

    companion object {
        const val ISSUE = "ISSUE"
        const val LABEL = "LABEL"
        const val ARTEFACT = "ARTEFACT"
        const val SYNCS_TO = "SYNCS_TO"
    }

    @NodeRelationship(ISSUE, Direction.OUTGOING)
    @FilterProperty
    @delegate:Transient
    val issues by NodeSetProperty<Issue>()

    @NodeRelationship(LABEL, Direction.OUTGOING)
    @FilterProperty
    @delegate:Transient
    val labels by NodeSetProperty<Label>()

    @NodeRelationship(ARTEFACT, Direction.OUTGOING)
    @FilterProperty
    @delegate:Transient
    val artefacts by NodeSetProperty<Artefact>()

    @NodeRelationship(SYNCS_TO, Direction.OUTGOING)
    @FilterProperty
    @delegate:Transient
    val syncsTo by NodeSetProperty<IMSProject>()

    @NodeRelationship(Issue.PINNED_ON, Direction.INCOMING)
    @FilterProperty
    @delegate:Transient
    val pinnedIssues by NodeSetProperty<Issue>()

}