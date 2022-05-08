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
@GraphQLDescription("Event representing that the Issue was added to a Trackable.")
class AddedToTrackableEvent(createdAt: OffsetDateTime, lastModifiedAt: OffsetDateTime) :
    TimelineItem(createdAt, lastModifiedAt) {

    companion object {
        const val ADDED_TO = "ADDED_TO"
    }

    @NodeRelationship(ADDED_TO, Direction.OUTGOING)
    @GraphQLDescription("The Trackable the Issue was added to.")
    @FilterProperty
    @delegate:Transient
    var addedToTrackable by NodeProperty<Trackable>()

}