package gropius.model.architecture

import gropius.model.issue.Issue
import io.github.graphglue.model.*
import java.net.URL

@DomainNode
abstract class Trackable(name: String, description: String, @FilterProperty var repositoryURL: URL) :
    AffectedByIssue(name, description) {

    companion object {
            const val ISSUE = "ISSUE"
    }

    @NodeRelationship(ISSUE, Direction.OUTGOING)
    @FilterProperty
    @delegate:Transient
    val issues by NodeSetProperty<Issue>()
}