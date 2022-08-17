package gropius.sync.github.model

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import java.net.URI

/**
 * Mapping of a single label from neo4j to github
 * @param url API URL of IMS of the repo
 */
@Document
data class LabelInfo(
    /**
     * ID on github
     */
    @Indexed(unique = true)
    val githubId: String,
    /**
     * ID in gropius database
     */
    @Indexed(unique = true)
    val neo4jId: String, val url: URI
) {
    /**
     * MongoDB ID
     */
    @Id
    var id: ObjectId? = null
}