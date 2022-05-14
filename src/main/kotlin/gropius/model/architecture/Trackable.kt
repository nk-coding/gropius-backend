package gropius.model.architecture

import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import gropius.model.issue.Artefact
import gropius.model.issue.Issue
import gropius.model.issue.Label
import io.github.graphglue.model.Direction
import io.github.graphglue.model.DomainNode
import io.github.graphglue.model.FilterProperty
import io.github.graphglue.model.NodeRelationship
import org.springframework.data.annotation.Transient
import java.net.URL

@DomainNode
@GraphQLDescription(
    """An entity which can have Issues, Labels and Artefacts.
    Has pinned issues.
    Can be synced to an IMS by creating an IMSProject.
    Can be affected by Issues.
    """
)
abstract class Trackable(
    name: String,
    description: String,
    @GraphQLDescription("If existing, the URL of the repository (e.g. a GitHub repository).")
    @FilterProperty
    var repositoryURL: URL
) : AffectedByIssue(name, description) {

    companion object {
        const val ISSUE = "ISSUE"
        const val LABEL = "LABEL"
        const val ARTEFACT = "ARTEFACT"
        const val SYNCS_TO = "SYNCS_TO"
    }

    @NodeRelationship(ISSUE, Direction.OUTGOING)
    @GraphQLDescription(
        """The set of Issues which are part of this Trackable.
        An Issue has to be part of a Trackable to use the Labels and Artefacts defined by the Trackable.
        """
    )
    @FilterProperty
    @delegate:Transient
    val issues by NodeSetProperty<Issue>()

    @NodeRelationship(LABEL, Direction.OUTGOING)
    @GraphQLDescription("The set of Labels which can be added to issues of this trackable.")
    @FilterProperty
    @delegate:Transient
    val labels by NodeSetProperty<Label>()

    @NodeRelationship(ARTEFACT, Direction.OUTGOING)
    @GraphQLDescription("Artefacts of this trackable, typically some kind of file.")
    @FilterProperty
    @delegate:Transient
    val artefacts by NodeSetProperty<Artefact>()

    @NodeRelationship(SYNCS_TO, Direction.OUTGOING)
    @GraphQLDescription("IMSProjects this Trackable is synced to and from.")
    @FilterProperty
    @delegate:Transient
    val syncsTo by NodeSetProperty<IMSProject>()

    @NodeRelationship(Issue.PINNED_ON, Direction.INCOMING)
    @GraphQLDescription("Issues which are pinned to this trackable, subset of `issues`.")
    @FilterProperty
    @delegate:Transient
    val pinnedIssues by NodeSetProperty<Issue>()

}