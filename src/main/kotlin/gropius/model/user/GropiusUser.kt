package gropius.model.user

import com.expediagroup.graphql.generator.annotations.GraphQLIgnore
import io.github.graphglue.model.Direction
import io.github.graphglue.model.DomainNode
import io.github.graphglue.model.FilterProperty
import io.github.graphglue.model.NodeRelationship
import org.springframework.data.annotation.Transient

@DomainNode
class GropiusUser(displayName: String, email: String?, @GraphQLIgnore val username: String) : User(displayName, email) {
    override fun username(): String = username

    @NodeRelationship(IMSUser.GROPIUS_USER, Direction.INCOMING)
    @FilterProperty
    @delegate:Transient
    val imsUsers by NodeSetProperty<IMSUser>()
}