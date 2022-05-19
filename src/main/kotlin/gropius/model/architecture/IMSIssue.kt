package gropius.model.architecture

import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import com.expediagroup.graphql.generator.annotations.GraphQLIgnore
import gropius.model.common.ExtensibleNode
import gropius.model.issue.Issue
import gropius.model.template.BaseTemplate
import gropius.model.template.IMSIssueTemplate
import gropius.model.template.MutableTemplatedNode
import gropius.model.template.TemplatedNode
import io.github.graphglue.model.Direction
import io.github.graphglue.model.DomainNode
import io.github.graphglue.model.FilterProperty
import io.github.graphglue.model.NodeRelationship
import org.springframework.data.annotation.Transient
import org.springframework.data.neo4j.core.schema.CompositeProperty

@DomainNode
@GraphQLDescription(
    """Project on an IMS, represents a Trackable synced to an IMS.
    The representation on the IMS depends on the type of IMS, e.g. for GitHub, a project is a repository.
    """
)
class IMSIssue(
    @property:GraphQLIgnore
    @CompositeProperty
    override val templatedFields: MutableMap<String, String>
) : ExtensibleNode(), TemplatedNode {

    companion object {
        const val PROJECT = "PROJECT"
        const val ISSUE = "ISSUE"
    }

    @NodeRelationship(BaseTemplate.USED_IN, Direction.INCOMING)
    @GraphQLDescription("The Template of this Component.")
    @FilterProperty
    @delegate:Transient
    val template by NodeProperty<IMSIssueTemplate>()

    @NodeRelationship(PROJECT, Direction.OUTGOING)
    @GraphQLDescription("The IMSProject the issue is synced with.")
    @FilterProperty
    @delegate:Transient
    var imsProject by NodeProperty<IMSProject>()

    @NodeRelationship(ISSUE, Direction.OUTGOING)
    @GraphQLDescription("The Issue that is synced by the IMSProject")
    @FilterProperty
    @delegate:Transient
    val issue by NodeProperty<Issue>()
}