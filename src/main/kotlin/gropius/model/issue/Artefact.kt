package gropius.model.issue

import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import gropius.model.architecture.Trackable
import gropius.model.common.SyncNode
import gropius.model.issue.timeline.Comment
import io.github.graphglue.model.*
import org.springframework.data.annotation.Transient
import java.net.URL
import java.time.OffsetDateTime

@DomainNode
@GraphQLDescription(
    """An Artefact referencing a file defined via an URL.
    Can optionally specify a line range (fromt - to), and a version.
    Is part of exactly one Trackable.
    Can be referenced by Comments and Issues.
    Artefacts are synced to all IMSProjects of the Trackable they are part of.
    """
)
class Artefact(
    createdAt: OffsetDateTime,
    lastModifiedAt: OffsetDateTime,
    @GraphQLDescription("An URL to the file this Artefact references")
    @FilterProperty
    @OrderProperty
    var file: URL,
    @GraphQLDescription("If present, the first line of the file this Artefact references, inclusive")
    @FilterProperty
    @OrderProperty
    var from: Int?,
    @GraphQLDescription("If present, the last line of the file this Artefact references, inclusive")
    @FilterProperty
    @OrderProperty
    var to: Int?,
    @GraphQLDescription("if present, the current version of this Artefact")
    @FilterProperty
    @OrderProperty
    var version: String?
) : SyncNode(createdAt, lastModifiedAt) {

    @NodeRelationship(Trackable.ARTEFACT, Direction.INCOMING)
    @GraphQLDescription("The Trackable this Artefact is part of.")
    @FilterProperty
    @delegate:Transient
    var trackable by NodeProperty<Trackable>()

    @NodeRelationship(Issue.ARTEFACT, Direction.INCOMING)
    @GraphQLDescription("Issues which currently have this Artefact.")
    @FilterProperty
    @delegate:Transient
    val issues by NodeSetProperty<Issue>()

    @NodeRelationship(Comment.REFERENCED_ARTEFACT, Direction.INCOMING)
    @GraphQLDescription("Comments which currently reference this Artefact.")
    @FilterProperty
    @delegate:Transient
    val referencingComments by NodeSetProperty<Comment>()

}