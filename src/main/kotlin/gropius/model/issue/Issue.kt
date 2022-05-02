package gropius.model.issue

import gropius.model.architecture.AffectedByIssue
import gropius.model.architecture.Trackable
import gropius.model.common.SyncNode
import gropius.model.issue.timeline.*
import gropius.model.template.IssuePriority
import gropius.model.template.IssueType
import gropius.model.user.User
import io.github.graphglue.model.*
import org.springframework.data.annotation.Transient
import java.time.Duration
import java.time.OffsetDateTime

// TODO keep isDuplicate?
// TODO add (Un)MarkedAsDuplicateEvents?
// TODO reintroduce Referenced Events?
@DomainNode
class Issue(
    createdAt: OffsetDateTime,
    lastModifiedAt: OffsetDateTime,
    @FilterProperty @OrderProperty var title: String,
    @FilterProperty @OrderProperty var lastUpdatedAt: OffsetDateTime,
    @FilterProperty var isOpen: Boolean,
    @FilterProperty @OrderProperty var startDate: OffsetDateTime?,
    @FilterProperty @OrderProperty var dueDate: OffsetDateTime?,
    @FilterProperty @OrderProperty var estimatedTime: Duration?,
    @FilterProperty @OrderProperty var spentTime: Duration?
) : SyncNode(createdAt, lastModifiedAt) {

    companion object {
        const val TIMELINE = "TIMELINE"
        const val ISSUE_COMMENT = "ISSUE_COMMENT"
        const val BODY = "BODY"
        const val TYPE = "TYPE"
        const val PRIORITY = "PRIORITY"
        const val LABEL = "LABEL"
        const val ARTEFACT = "ARTEFACT"
        const val PARTICIPANT = "PARTICIPANT"
        const val INCOMING_RELATION = "INCOMING_RELATION"
        const val OUTGOING_RELATION = "OUTGOING_RELATION"
        const val ASSIGNMENT = "ASSIGNMENT"
        const val PINNED_ON = "PINNED_ON"
    }

    @NodeRelationship(AffectedByIssue.AFFECTS, Direction.INCOMING)
    @FilterProperty
    @delegate:Transient
    val affects by NodeSetProperty<AffectedByIssue>()

    @NodeRelationship(Trackable.ISSUE, Direction.INCOMING)
    @FilterProperty
    @delegate:Transient
    val trackables by NodeSetProperty<Trackable>()

    @NodeRelationship(TIMELINE, Direction.OUTGOING)
    @FilterProperty
    @delegate:Transient
    val timelineItems by NodeSetProperty<TimelineItem>()

    @NodeRelationship(ISSUE_COMMENT, Direction.OUTGOING)
    @FilterProperty
    @delegate:Transient
    val issueComments by NodeSetProperty<IssueComment>()

    @NodeRelationship(BODY, Direction.OUTGOING)
    @FilterProperty
    @delegate:Transient
    val body by NodeProperty<Body>()

    @NodeRelationship(TYPE, Direction.OUTGOING)
    @FilterProperty
    @delegate:Transient
    var type by NodeProperty<IssueType>()

    @NodeRelationship(PRIORITY, Direction.OUTGOING)
    @FilterProperty
    @delegate:Transient
    var priority by NodeProperty<IssuePriority?>()

    @NodeRelationship(LABEL, Direction.OUTGOING)
    @FilterProperty
    @delegate:Transient
    val labels by NodeSetProperty<Label>()

    @NodeRelationship(ARTEFACT, Direction.OUTGOING)
    @FilterProperty
    @delegate:Transient
    val artefacts by NodeSetProperty<Artefact>()

    @NodeRelationship(PARTICIPANT, Direction.OUTGOING)
    @FilterProperty
    @delegate:Transient
    val participants by NodeSetProperty<User>()

    @NodeRelationship(INCOMING_RELATION, Direction.OUTGOING)
    @FilterProperty
    @delegate:Transient
    val incomingRelations by NodeSetProperty<IssueRelation>()

    @NodeRelationship(OUTGOING_RELATION, Direction.OUTGOING)
    @FilterProperty
    @delegate:Transient
    val outgoingRelations by NodeSetProperty<IssueRelation>()

    @NodeRelationship(ASSIGNMENT, Direction.OUTGOING)
    @FilterProperty
    @delegate:Transient
    val assignments by NodeSetProperty<Assignment>()

    @NodeRelationship(PINNED_ON, Direction.OUTGOING)
    @FilterProperty
    @delegate:Transient
    val pinnedOn by NodeSetProperty<Trackable>()
}