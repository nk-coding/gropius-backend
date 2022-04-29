package gropius.model.issue

import gropius.model.architecture.Trackable
import gropius.model.common.SyncNode
import gropius.model.issue.timeline.Comment
import io.github.graphglue.model.Direction
import io.github.graphglue.model.DomainNode
import io.github.graphglue.model.FilterProperty
import io.github.graphglue.model.NodeRelationship
import java.time.OffsetDateTime
import org.springframework.data.annotation.Transient

@DomainNode
class Artefact(createdAt: OffsetDateTime, lastModifiedAt: OffsetDateTime) : SyncNode(createdAt, lastModifiedAt) {

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