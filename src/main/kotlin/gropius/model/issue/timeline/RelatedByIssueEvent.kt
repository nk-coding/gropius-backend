package gropius.model.issue.timeline

import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import io.github.graphglue.model.Direction
import io.github.graphglue.model.DomainNode
import io.github.graphglue.model.FilterProperty
import io.github.graphglue.model.NodeRelationship
import org.springframework.data.annotation.Transient
import java.time.OffsetDateTime

@DomainNode
@GraphQLDescription(
    """Event representing that the Issue was used in an IssueRelation as related issue.
    The IssueRelation may not be active any more.
    """
)
class RelatedByIssueEvent(
    createdAt: OffsetDateTime,
    lastModifiedAt: OffsetDateTime,
) : TimelineItem(createdAt, lastModifiedAt) {

    companion object {
        const val RELATION = "RELATION"
    }

    @NodeRelationship(RELATION, Direction.OUTGOING)
    @GraphQLDescription("The IssueRelation the Issue is related at.")
    @FilterProperty
    @delegate:Transient
    var relation by NodeProperty<IssueRelation>()

}