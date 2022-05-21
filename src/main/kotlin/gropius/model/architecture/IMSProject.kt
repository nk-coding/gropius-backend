package gropius.model.architecture

import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import gropius.model.common.ExtensibleNode
import gropius.model.issue.Issue
import io.github.graphglue.model.Direction
import io.github.graphglue.model.DomainNode
import io.github.graphglue.model.FilterProperty
import io.github.graphglue.model.NodeRelationship
import org.springframework.data.annotation.Transient

@DomainNode
@GraphQLDescription(
    """Project on an IMS, represents a Trackable synced to an IMS.
    The representation on the IMS depends on the type of IMS, e.g. for GitHub, a project is a repository.
    """
)
class IMSProject : ExtensibleNode() {

    companion object {
        const val PARTIALLY_SYNCED_ISSUES = "PARTIALLY_SYNCED_ISSUES"
    }

    @NodeRelationship(Trackable.SYNCS_TO, Direction.INCOMING)
    @GraphQLDescription("The trackable which is synced.")
    @FilterProperty
    @delegate:Transient
    val trackable by NodeProperty<Trackable>()

    @NodeRelationship(IMS.PROJECT, Direction.INCOMING)
    @GraphQLDescription("The IMS this project is a part of.")
    @FilterProperty
    @delegate:Transient
    val ims by NodeProperty<IMS>()

    @NodeRelationship(PARTIALLY_SYNCED_ISSUES, Direction.OUTGOING)
    @GraphQLDescription("Issues which are currently partially synced with this IMSProject")
    @FilterProperty
    @delegate:Transient
    val partiallySyncedIssues by NodeSetProperty<Issue>()
}