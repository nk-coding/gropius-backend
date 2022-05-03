package gropius.model.issue.timeline

import gropius.model.template.AssignmentType
import gropius.model.user.User
import io.github.graphglue.model.Direction
import io.github.graphglue.model.DomainNode
import io.github.graphglue.model.FilterProperty
import io.github.graphglue.model.NodeRelationship
import org.springframework.data.annotation.Transient
import java.time.OffsetDateTime


@DomainNode
class Assignment(createdAt: OffsetDateTime, lastModifiedAt: OffsetDateTime) : TimelineItem(createdAt, lastModifiedAt) {

    companion object {
        const val TYPE = "TYPE"
        const val USER = "USER"
    }

    @NodeRelationship(TYPE, Direction.OUTGOING)
    @FilterProperty
    @delegate:Transient
    var type by NodeProperty<AssignmentType?>()

    @NodeRelationship(USER, Direction.OUTGOING)
    @FilterProperty
    @delegate:Transient
    var user by NodeProperty<User>()

}