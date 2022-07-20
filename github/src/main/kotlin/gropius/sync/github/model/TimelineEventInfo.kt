package gropius.sync.github.model

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import java.time.OffsetDateTime

/**
 * Mapping of a single timeline event from neo4j to github
 */
@Document
data class TimelineEventInfo(
    @Indexed(unique = true)
    val githubId: String,
    @Indexed
    val neo4jId: String?, val lastModifiedAt: OffsetDateTime, val type: String?
) {
    @Id
    var id: ObjectId? = null
}