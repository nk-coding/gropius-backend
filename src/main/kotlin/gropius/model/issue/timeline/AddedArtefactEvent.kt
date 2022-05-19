package gropius.model.issue.timeline

import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import gropius.model.issue.Artefact
import io.github.graphglue.model.Direction
import io.github.graphglue.model.DomainNode
import io.github.graphglue.model.FilterProperty
import io.github.graphglue.model.NodeRelationship
import org.springframework.data.annotation.Transient
import java.time.OffsetDateTime

@DomainNode
@GraphQLDescription("Event representing that an Artefact was added to an Issue.")
class AddedArtefactEvent(
    createdAt: OffsetDateTime, lastModifiedAt: OffsetDateTime
) : TimelineItem(createdAt, lastModifiedAt) {

    companion object {
        const val ADDED_ARTEFACT = "ADDED_ARTEFACT"
    }

    @NodeRelationship(ADDED_ARTEFACT, Direction.OUTGOING)
    @GraphQLDescription("The Artefact added to the Issue.")
    @FilterProperty
    @delegate:Transient
    var addedArtefact by NodeProperty<Artefact>()

}