package gropius.model.architecture

import gropius.model.common.ExtensibleNode
import io.github.graphglue.model.Direction
import io.github.graphglue.model.DomainNode
import io.github.graphglue.model.FilterProperty
import io.github.graphglue.model.NodeRelationship
import org.springframework.data.annotation.Transient


@DomainNode
class IMSProject : ExtensibleNode() {

    @NodeRelationship(Trackable.SYNCS_TO, Direction.INCOMING)
    @FilterProperty
    @delegate:Transient
    val trackable by NodeProperty<Trackable>()

    @NodeRelationship(IMS.PROJECT, Direction.INCOMING)
    @FilterProperty
    @delegate:Transient
    val ims by NodeProperty<IMS>()

}