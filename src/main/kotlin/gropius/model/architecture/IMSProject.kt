package gropius.model.architecture

import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import com.expediagroup.graphql.generator.annotations.GraphQLIgnore
import gropius.model.common.ExtensibleNode
import gropius.model.issue.Issue
import gropius.model.user.permission.NodePermission
import io.github.graphglue.model.*
import gropius.model.template.BaseTemplate
import gropius.model.template.IMSProjectTemplate
import gropius.model.template.MutableTemplatedNode
import gropius.model.user.permission.TrackablePermission
import org.springframework.data.annotation.Transient
import org.springframework.data.neo4j.core.schema.CompositeProperty

@DomainNode
@GraphQLDescription(
    """Project on an IMS, represents a Trackable synced to an IMS.
    The representation on the IMS depends on the type of IMS, e.g. for GitHub, a project is a repository.
    READ is granted if READ is granted on `trackable` or `ims`.
    """
)
@Authorization(NodePermission.READ, allowFromRelated = ["trackable", "ims"])
@Authorization(TrackablePermission.MANAGE_IMS, allowFromRelated = ["trackable"])
class IMSProject(
    @property:GraphQLIgnore
    @CompositeProperty
    override val templatedFields: MutableMap<String, String>
) : ExtensibleNode(), MutableTemplatedNode {

    companion object {
        const val PARTIALLY_SYNCED_ISSUES = "PARTIALLY_SYNCED_ISSUES"
    }

    @NodeRelationship(BaseTemplate.USED_IN, Direction.INCOMING)
    @GraphQLDescription("The Template of this Component.")
    @FilterProperty
    @delegate:Transient
    val template by NodeProperty<IMSProjectTemplate>()

    @NodeRelationship(Trackable.SYNCS_TO, Direction.INCOMING)
    @GraphQLDescription("The trackable which is synced.")
    @FilterProperty
    @delegate:Transient
    val trackable by NodeProperty<Trackable>()

    @NodeRelationship(IMS.PROJECT, Direction.INCOMING)
    @GraphQLDescription("The IMS this project is a part of.")
    @GraphQLNullable
    @FilterProperty
    @delegate:Transient
    val ims by NodeProperty<IMS>()

    @NodeRelationship(PARTIALLY_SYNCED_ISSUES, Direction.OUTGOING)
    @GraphQLDescription("Issues which are currently partially synced with this IMSProject")
    @FilterProperty
    @delegate:Transient
    val partiallySyncedIssues by NodeSetProperty<Issue>()

    @NodeRelationship(IMSIssue.PROJECT, Direction.INCOMING)
    @GraphQLDescription("The IMSIssues synced to by this project.")
    @FilterProperty
    @delegate:Transient
    val imsIssues by NodeSetProperty<IMSIssue>()
}