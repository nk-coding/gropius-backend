package gropius.sync.github.model

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import java.net.URI
import java.time.OffsetDateTime

/**
 * Mapping of a single timeline event from neo4j to github
 * @param url API URL of IMS of the repo
 * @param githubId ID on github
 * @param neo4jId ID in gropius database
 * @param lastModifiedAt Time of the last interaction with this timeline item
 * @param type Github __typename of this event
 */
@Document
data class TimelineEventInfo(
    @Indexed
    val githubId: String,
    @Indexed
    val neo4jId: String?, val lastModifiedAt: OffsetDateTime, val type: String?, val url: URI
) {
    /**
     * MongoDB ID
     */
    @Id
    var id: ObjectId? = null
}