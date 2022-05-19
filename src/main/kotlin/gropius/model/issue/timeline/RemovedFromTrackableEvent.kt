package gropius.model.issue.timeline

import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import gropius.model.architecture.Trackable
import io.github.graphglue.model.Direction
import io.github.graphglue.model.DomainNode
import io.github.graphglue.model.FilterProperty
import io.github.graphglue.model.NodeRelationship
import org.springframework.data.annotation.Transient
import java.time.OffsetDateTime

@DomainNode
@GraphQLDescription("Event representing that an Issue was removed from a Trackable.")
class RemovedFromTrackableEvent(
    createdAt: OffsetDateTime, lastModifiedAt: OffsetDateTime
) : TimelineItem(createdAt, lastModifiedAt) {

    companion object {
        const val REMOVED_FROM = "REMOVED_FROM"
    }

    @NodeRelationship(REMOVED_FROM, Direction.OUTGOING)
    @GraphQLDescription("The Trackable the Issue was removed from.")
    @FilterProperty
    @delegate:Transient
    var removedFromTrackable by NodeProperty<Trackable>()

}