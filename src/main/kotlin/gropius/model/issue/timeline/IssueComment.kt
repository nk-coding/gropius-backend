package gropius.model.issue.timeline

import com.expediagroup.graphql.generator.annotations.GraphQLName
import io.github.graphglue.model.Direction
import io.github.graphglue.model.DomainNode
import io.github.graphglue.model.FilterProperty
import io.github.graphglue.model.NodeRelationship
import org.springframework.data.annotation.Transient
import java.time.OffsetDateTime

@DomainNode
class IssueComment(
    createdAt: OffsetDateTime,
    lastModifiedAt: OffsetDateTime,
    body: String,
    lastEditedAt: OffsetDateTime,
    @GraphQLName("isDeleted")
    @FilterProperty
    val isCommentDeleted: Boolean
) : Comment(createdAt, lastModifiedAt, body, lastEditedAt) {

    companion object {
        const val ANSWERS = "ANSWERS"
    }

    override var body: String
        get() {
            return if (isCommentDeleted) {
                ""
            } else {
                super.body
            }
        }
        set(value) {
            super.body = value
        }

    @NodeRelationship(ANSWERS, Direction.OUTGOING)
    @FilterProperty
    @delegate:Transient
    var answers by NodeProperty<Comment>()

}