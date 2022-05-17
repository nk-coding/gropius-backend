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
@GraphQLDescription("Event representing that an Issue was pinned on a Trackable.")
class AddedToPinnedIssuesEvent(
    createdAt: OffsetDateTime, lastModifiedAt: OffsetDateTime
) : TimelineItem(createdAt, lastModifiedAt) {

    companion object {
        const val PINNED_ON = "PINNED_ON"
    }

    @NodeRelationship(PINNED_ON, Direction.OUTGOING)
    @GraphQLDescription("The Trackable the Issue is now pinned on.")
    @FilterProperty
    @delegate:Transient
    var pinnedOn by NodeProperty<Trackable>()

}