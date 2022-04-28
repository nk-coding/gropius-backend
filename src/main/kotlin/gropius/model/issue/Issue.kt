package gropius.model.issue

import gropius.model.architecture.AffectedByIssue
import gropius.model.architecture.Trackable
import gropius.model.common.SyncNode
import io.github.graphglue.model.*
import java.time.Duration
import java.time.LocalDateTime
import org.springframework.data.annotation.Transient

// TODO priority
// TODO type
// TODO keep isDuplicate?
@DomainNode
class Issue(
    createdAt: LocalDateTime,
    lastModifiedAt: LocalDateTime,
    @FilterProperty @OrderProperty var title: String,
    @FilterProperty @OrderProperty val lastUpdatedAt: LocalDateTime,
    @FilterProperty val isOpen: Boolean,
    @FilterProperty @OrderProperty val startDate: LocalDateTime?,
    @FilterProperty @OrderProperty val dueDate: LocalDateTime?,
    @FilterProperty @OrderProperty val estimatedTime: Duration?
) : SyncNode(createdAt, lastModifiedAt) {

    @NodeRelationship(AffectedByIssue.AFFECTS, Direction.INCOMING)
    @FilterProperty
    @delegate:Transient
    val affects by NodeSetProperty<AffectedByIssue>()

    @NodeRelationship(Trackable.ISSUE, Direction.INCOMING)
    @FilterProperty
    @delegate:Transient
    val trackables by NodeSetProperty<Trackable>()
}