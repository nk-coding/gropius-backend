package gropius.model.issue.timeline

import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import gropius.model.issue.Artefact
import gropius.model.user.User
import io.github.graphglue.model.*
import org.springframework.data.annotation.Transient
import java.time.OffsetDateTime

@DomainNode
@GraphQLDescription(
    """Supertype for IssueComment and Body.
    Represents a text block in the Timeline.
    Keeps track when it was last edited and by who, but does not keep track of the change history.
    """
)
abstract class Comment(
    createdAt: OffsetDateTime,
    lastModifiedAt: OffsetDateTime,
    @GraphQLDescription(
        """The text of the Comment.
        Supports GFM (GitHub Flavored Markdown).
        Updates to the body cause lastEditedAt and lastEditedBy to change, while updates to referencedArtefacts
        do not.
        """
    )
    @FilterProperty
    open var body: String,
    @GraphQLDescription("Keep track when the Comment was last updated. If not updated yet, the DateTime of creation.")
    @FilterProperty
    @OrderProperty
    var lastEditedAt: OffsetDateTime
) : TimelineItem(createdAt, lastModifiedAt) {

    companion object {
        const val LAST_EDITED_BY = "LAST_EDITED_BY"
    }

    @NodeRelationship(IssueComment.ANSWERS, Direction.INCOMING)
    @GraphQLDescription("IssueComments which answer this Comment.")
    @FilterProperty
    @delegate:Transient
    val answeredBy by NodeSetProperty<IssueComment>()

    @NodeRelationship(LAST_EDITED_BY, Direction.OUTGOING)
    @GraphQLDescription("The User who last edited the Comment. If not edited yet, the creator of the Comment.")
    @FilterProperty
    @delegate:Transient
    var lastEditedBy by NodeProperty<User>()

}