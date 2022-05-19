package gropius.model.architecture

import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import com.expediagroup.graphql.generator.annotations.GraphQLIgnore
import gropius.model.common.NamedNode
import gropius.model.template.BaseTemplate
import gropius.model.template.IMSTemplate
import gropius.model.template.MutableTemplatedNode
import gropius.model.user.IMSUser
import io.github.graphglue.model.Direction
import io.github.graphglue.model.DomainNode
import io.github.graphglue.model.FilterProperty
import io.github.graphglue.model.NodeRelationship
import org.springframework.data.annotation.Transient
import org.springframework.data.neo4j.core.schema.CompositeProperty

@DomainNode("imss")
@GraphQLDescription(
    """Entity which represents an issue management system (like GitHub, Jira, Redmine, ...).
    Trackables can be added to this via an IMSProject, so that their issues are synced to this IMS.
    """
)
class IMS(
    name: String,
    description: String,
    @property:GraphQLIgnore
    @CompositeProperty
    override val templatedFields: MutableMap<String, String>
) : NamedNode(name, description), MutableTemplatedNode {

    companion object {
        const val PROJECT = "PROJECT"
        const val USER = "USER"
    }

    @NodeRelationship(BaseTemplate.USED_IN, Direction.INCOMING)
    @GraphQLDescription("The Template of this Component.")
    @FilterProperty
    @delegate:Transient
    val template by NodeProperty<IMSTemplate>()

    @NodeRelationship(PROJECT, Direction.OUTGOING)
    @GraphQLDescription("Projects which are synced to this IMS.")
    @FilterProperty
    @delegate:Transient
    val projects by NodeSetProperty<IMSProject>()

    @NodeRelationship(USER, Direction.OUTGOING)
    @GraphQLDescription("Users of this IMS.")
    @FilterProperty
    @delegate:Transient
    val users by NodeSetProperty<IMSUser>()
}