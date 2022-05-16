package gropius.model.issue

import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import gropius.model.architecture.AffectedByIssue
import gropius.model.architecture.IMSProject
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
@GraphQLDescription(
    """An Issue in the Gropius system.
    Issues can be used to report bugs, request features, ask questions, ...
    Issues are synced to all IMSProjects of Trackables they are part of.
    All changes to the Issue are reflected by the timeline.
    """
)
class Issue(
    createdAt: OffsetDateTime,
    lastModifiedAt: OffsetDateTime,
    @GraphQLDescription("Title of the Issue, usually a short description of the Issue.")
    @FilterProperty
    @OrderProperty
    var title: String,
    @GraphQLDescription("The DateTime when the Issue was last updated, this includes a changed timeline.")
    @FilterProperty
    @OrderProperty
    var lastUpdatedAt: OffsetDateTime,
    @GraphQLDescription("If true, this Issue is currently open, otherwise it is closed")
    @FilterProperty
    var isOpen: Boolean,
    @GraphQLDescription("DateTime when working on this Issue started / will start.")
    @FilterProperty
    @OrderProperty
    var startDate: OffsetDateTime?,
    @GraphQLDescription("DateTime when working on this Issue should be finished.")
    @FilterProperty
    @OrderProperty
    var dueDate: OffsetDateTime?,
    @GraphQLDescription("Estimated amount of time necessary for this Issue.")
    @FilterProperty
    @OrderProperty
    var estimatedTime: Duration?,
    @GraphQLDescription("Time spent working on this Issue.")
    @FilterProperty
    @OrderProperty
    var spentTime: Duration?
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
        const val AFFECTS = "AFFECTS"
    }

    @NodeRelationship(AFFECTS, Direction.OUTGOING)
    @GraphQLDescription("Entities which are in some regard affected by this Issue.")
    @FilterProperty
    @delegate:Transient
    val affects by NodeSetProperty<AffectedByIssue>()

    @NodeRelationship(Trackable.ISSUE, Direction.INCOMING)
    @GraphQLDescription("Trackables this Issue is part of.")
    @FilterProperty
    @delegate:Transient
    val trackables by NodeSetProperty<Trackable>()

    @NodeRelationship(TIMELINE, Direction.OUTGOING)
    @GraphQLDescription("Timeline of the Issue, shows how the Issue changed over time.")
    @FilterProperty
    @delegate:Transient
    val timelineItems by NodeSetProperty<TimelineItem>()

    @NodeRelationship(ISSUE_COMMENT, Direction.OUTGOING)
    @GraphQLDescription("Comments on the Issue, subset of the timeline.")
    @FilterProperty
    @delegate:Transient
    val issueComments by NodeSetProperty<IssueComment>()

    @NodeRelationship(BODY, Direction.OUTGOING)
    @GraphQLDescription("The Body of the Issue, a Comment directly associated with the Issue.")
    @FilterProperty
    @delegate:Transient
    var body by NodeProperty<Body>()

    @NodeRelationship(TYPE, Direction.OUTGOING)
    @GraphQLDescription("The type of the Issue, e.g. BUG. Allowed IssueTypes are defined by the template.")
    @FilterProperty
    @delegate:Transient
    var type by NodeProperty<IssueType>()

    @NodeRelationship(PRIORITY, Direction.OUTGOING)
    @GraphQLDescription("The priority of the Issue, e.g. HIGH. Allowed IssuePriorities are defined by the template.")
    @FilterProperty
    @delegate:Transient
    var priority by NodeProperty<IssuePriority?>()

    @NodeRelationship(LABEL, Direction.OUTGOING)
    @GraphQLDescription("Labels currently assigned to the Issue. For the history, see timelineItems.")
    @FilterProperty
    @delegate:Transient
    val labels by NodeSetProperty<Label>()

    @NodeRelationship(ARTEFACT, Direction.OUTGOING)
    @GraphQLDescription("Artefacts currently assigned to the Issue. For the history, see timelineItems.")
    @FilterProperty
    @delegate:Transient
    val artefacts by NodeSetProperty<Artefact>()

    @NodeRelationship(PARTICIPANT, Direction.OUTGOING)
    @GraphQLDescription("Users who participated on the Issue, e.g. commented, added Labels, ...")
    @FilterProperty
    @delegate:Transient
    val participants by NodeSetProperty<User>()

    @NodeRelationship(INCOMING_RELATION, Direction.OUTGOING)
    @GraphQLDescription("Current IssueRelations which have this Issue as end point.")
    @FilterProperty
    @delegate:Transient
    val incomingRelations by NodeSetProperty<IssueRelation>()

    @NodeRelationship(OUTGOING_RELATION, Direction.OUTGOING)
    @GraphQLDescription("Current IssueRelations which have this Issue as start point.")
    @FilterProperty
    @delegate:Transient
    val outgoingRelations by NodeSetProperty<IssueRelation>()

    @NodeRelationship(ASSIGNMENT, Direction.OUTGOING)
    @GraphQLDescription("Current Assignments to this Issue. For the history, see timelineItems.")
    @FilterProperty
    @delegate:Transient
    val assignments by NodeSetProperty<Assignment>()

    @NodeRelationship(PINNED_ON, Direction.OUTGOING)
    @GraphQLDescription("Trackables this Issue is currently pinned on. For the history, see timelineItems.")
    @FilterProperty
    @delegate:Transient
    val pinnedOn by NodeSetProperty<Trackable>()

    @NodeRelationship(IMSProject.PARTIALLY_SYNCED_ISSUES, Direction.INCOMING)
    @GraphQLDescription(
        """IMSProjects with which this Issue is currently partially synced,
        meaning that a sync is in progress, but not completed yet.
        """
    )
    @FilterProperty
    @delegate:Transient
    val partiallySyncedWith by NodeSetProperty<IMSProject>()
}