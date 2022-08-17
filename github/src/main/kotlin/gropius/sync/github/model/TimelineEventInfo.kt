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
 */
@Document
data class TimelineEventInfo(
    /**
     * ID on github
     */
    @Indexed(unique = true)
    val githubId: String,
    /**
     * ID in gropius database
     */
    @Indexed
    val neo4jId: String?,
    /**
     * Time of the last interaction with this timeline item
     */
    val lastModifiedAt: OffsetDateTime,
    /**
     * Github __typename of this event
     */
    val type: String?, val url: URI
) {
    /**
     * MongoDB ID
     */
    @Id
    var id: ObjectId? = null
}