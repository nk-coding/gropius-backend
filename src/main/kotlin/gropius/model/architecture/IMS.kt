package gropius.model.architecture

import gropius.model.common.ExtensibleNode
import gropius.model.user.IMSUser
import io.github.graphglue.model.Direction
import io.github.graphglue.model.DomainNode
import io.github.graphglue.model.FilterProperty
import io.github.graphglue.model.NodeRelationship
import org.springframework.data.annotation.Transient

@DomainNode
class IMS : ExtensibleNode() {

    companion object {
        const val PROJECT = "PROJECT"
        const val USER = "USER"
    }

    @NodeRelationship(PROJECT, Direction.OUTGOING)
    @FilterProperty
    @delegate:Transient
    val projects by NodeSetProperty<IMSProject>()

    @NodeRelationship(USER, Direction.OUTGOING)
    @FilterProperty
    @delegate:Transient
    val users by NodeSetProperty<IMSUser>()
}