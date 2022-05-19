package gropius.model.issue.timeline

import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import gropius.model.architecture.AffectedByIssue
import io.github.graphglue.model.Direction
import io.github.graphglue.model.DomainNode
import io.github.graphglue.model.FilterProperty
import io.github.graphglue.model.NodeRelationship
import org.springframework.data.annotation.Transient
import java.time.OffsetDateTime

@DomainNode
@GraphQLDescription("Event representing that an entity is no longer affected by an Issue.")
class RemovedAffectedEntityEvent(
    createdAt: OffsetDateTime, lastModifiedAt: OffsetDateTime
) : TimelineItem(createdAt, lastModifiedAt) {

    companion object {
        const val REMOVED_AFFECTED = "REMOVED_AFFECTED"
    }

    @NodeRelationship(REMOVED_AFFECTED, Direction.OUTGOING)
    @GraphQLDescription("The entity which is no longer affected by the Issue.")
    @FilterProperty
    @delegate:Transient
    var removedAffectedEntity by NodeProperty<AffectedByIssue>()

}