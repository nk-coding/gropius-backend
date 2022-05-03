package gropius.model.user

import com.expediagroup.graphql.generator.annotations.GraphQLIgnore
import gropius.model.architecture.IMS
import io.github.graphglue.model.Direction
import io.github.graphglue.model.DomainNode
import io.github.graphglue.model.FilterProperty
import io.github.graphglue.model.NodeRelationship
import org.springframework.data.annotation.Transient

@DomainNode
class IMSUser(displayName: String, email: String?, @GraphQLIgnore val username: String?) : User(displayName, email) {

    companion object {
        const val GROPIUS_USER = "GROPIUS_USER"
    }

    override fun username(): String? = username

    @NodeRelationship(GROPIUS_USER, Direction.OUTGOING)
    @FilterProperty
    @delegate:Transient
    var gropiusUser by NodeProperty<GropiusUser?>()

    @NodeRelationship(IMS.USER, Direction.INCOMING)
    @FilterProperty
    @delegate:Transient
    var ims by NodeProperty<IMS>()
}