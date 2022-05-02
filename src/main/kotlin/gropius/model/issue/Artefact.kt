package gropius.model.issue

import gropius.model.architecture.Trackable
import gropius.model.common.SyncNode
import gropius.model.issue.timeline.Comment
import io.github.graphglue.model.*
import org.springframework.data.annotation.Transient
import java.net.URL
import java.time.OffsetDateTime

@DomainNode
class Artefact(
    createdAt: OffsetDateTime, lastModifiedAt: OffsetDateTime,
    @FilterProperty @OrderProperty var file: URL,
    @FilterProperty @OrderProperty
    var from: Int?,
    @FilterProperty @OrderProperty
    var to: Int?,
    @FilterProperty @OrderProperty
    var version: String?
) : SyncNode(createdAt, lastModifiedAt) {

    @NodeRelationship(Trackable.ARTEFACT, Direction.INCOMING)
    @FilterProperty
    @delegate:Transient
    val trackable by NodeProperty<Trackable>()

    @NodeRelationship(Issue.ARTEFACT, Direction.INCOMING)
    @FilterProperty
    @delegate:Transient
    val issues by NodeSetProperty<Issue>()

    @NodeRelationship(Comment.REFERENCED_ARTEFACT, Direction.INCOMING)
    @FilterProperty
    @delegate:Transient
    val referencingComments by NodeSetProperty<Comment>()

}