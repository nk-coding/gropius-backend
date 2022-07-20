package gropius.sync.github.model

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document

/**
 * Mapping of a single user from neo4j to github
 */
@Document
data class UserInfo(
    @Indexed(unique = true)
    val login: String,
    @Indexed(unique = true)
    val neo4jId: String
) {
    @Id
    var id: ObjectId? = null
}