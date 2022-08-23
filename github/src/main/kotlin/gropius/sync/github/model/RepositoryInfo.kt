package gropius.sync.github.model

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import java.time.OffsetDateTime

/**
 * Mapping of a single repository from neo4j to github
 * @param imsProject IMSProject of the repo
 * @param user user/orga on github
 * @param repo repo on github
 * @param Time of the last item of the last issue query
 */
@Document
data class RepositoryInfo(
    @Indexed
    val user: String,
    @Indexed
    val repo: String,
    @Indexed
    val imsProject: String, var lastAccess: OffsetDateTime
) {
    /**
     * MongoDB ID
     */
    @Id
    var id: ObjectId? = null
}