package gropius.model.issue

import com.expediagroup.graphql.generator.annotations.GraphQLType
import gropius.model.architecture.Trackable
import gropius.model.common.NamedSyncNode
import io.github.graphglue.model.*
import org.springframework.data.annotation.Transient
import java.time.OffsetDateTime


@DomainNode
class Label(
    createdAt: OffsetDateTime,
    lastModifiedAt: OffsetDateTime,
    name: String,
    description: String,
    @FilterProperty @OrderProperty @GraphQLType("Color") var color: String
) : NamedSyncNode(createdAt, lastModifiedAt, name, description) {

    @NodeRelationship(Trackable.LABEL, Direction.INCOMING)
    @FilterProperty
    @delegate:Transient
    val trackables by NodeSetProperty<Trackable>()

    @NodeRelationship(Issue.LABEL, Direction.INCOMING)
    @FilterProperty
    @delegate:Transient
    val issues by NodeSetProperty<Issue>()

}