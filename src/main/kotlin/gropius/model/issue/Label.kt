package gropius.model.issue

import gropius.model.architecture.Trackable
import gropius.model.common.NamedSyncNode
import io.github.graphglue.model.*
import java.time.OffsetDateTime
import org.springframework.data.annotation.Transient


@DomainNode
class Label(
    createdAt: OffsetDateTime,
    lastModifiedAt: OffsetDateTime,
    name: String,
    description: String,
    @FilterProperty @OrderProperty val color: String
) : NamedSyncNode(createdAt, lastModifiedAt, name, description) {

    @NodeRelationship(Trackable.LABEL, Direction.INCOMING)
    @FilterProperty
    @delegate:Transient
    val trackables by NodeSetProperty<Trackable>()

    @NodeRelationship(Issue.LABEL, Direction.INCOMING)
    @FilterProperty
    @delegate:Transient
    val issues by NodeSetProperty<Issue>()

}