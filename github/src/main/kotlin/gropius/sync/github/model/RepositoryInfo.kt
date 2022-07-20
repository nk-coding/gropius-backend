package gropius.sync.github.model

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import java.time.OffsetDateTime

/**
 * Mapping of a single repository from neo4j to github
 */
@Document
data class RepositoryInfo(
    @Indexed
    val user: String,
    @Indexed
    val repo: String, var lastAccess: OffsetDateTime
) {
    @Id
    var id: ObjectId? = null
}