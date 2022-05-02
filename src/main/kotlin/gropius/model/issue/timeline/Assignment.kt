package gropius.model.issue.timeline

import gropius.model.template.AssignmentType
import gropius.model.user.User
import io.github.graphglue.model.*
import java.time.OffsetDateTime
import org.springframework.data.annotation.Transient


@DomainNode
class Assignment(createdAt: OffsetDateTime, lastModifiedAt: OffsetDateTime) : TimelineItem(createdAt, lastModifiedAt) {

    companion object {
        const val TYPE = "TYPE"
        const val USER = "USER"
    }

    @NodeRelationship(TYPE, Direction.OUTGOING)
    @FilterProperty
    @delegate:Transient
    val type by NodeProperty<AssignmentType?>()

    @NodeRelationship(USER, Direction.OUTGOING)
    @FilterProperty
    @delegate:Transient
    val user by NodeProperty<User>()

}