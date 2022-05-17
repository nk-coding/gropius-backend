package gropius.model.issue

import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import com.expediagroup.graphql.generator.annotations.GraphQLType
import gropius.model.architecture.Trackable
import gropius.model.common.NamedAuditedNode
import io.github.graphglue.model.*
import org.springframework.data.annotation.Transient
import java.time.OffsetDateTime

@DomainNode
@GraphQLDescription(
    """Label used to mark Issues with.
    A Label consists of a name, a description and a color.
    Issues may be synced to all IMSProjects of Trackables they are part of.
    """
)
class Label(
    createdAt: OffsetDateTime,
    lastModifiedAt: OffsetDateTime,
    name: String,
    description: String,
    @GraphQLDescription("The color of the Label, used to display the Label.")
    @FilterProperty
    @OrderProperty
    @GraphQLType("Color")
    var color: String
) : NamedAuditedNode(createdAt, lastModifiedAt, name, description) {

    @NodeRelationship(Trackable.LABEL, Direction.INCOMING)
    @GraphQLDescription("Trackables this Label is part of.")
    @FilterProperty
    @delegate:Transient
    val trackables by NodeSetProperty<Trackable>()

    @NodeRelationship(Issue.LABEL, Direction.INCOMING)
    @GraphQLDescription("Issues which currently have this Label.")
    @FilterProperty
    @delegate:Transient
    val issues by NodeSetProperty<Issue>()

}