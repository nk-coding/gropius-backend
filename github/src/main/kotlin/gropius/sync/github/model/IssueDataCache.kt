package gropius.sync.github.model

import gropius.sync.github.generated.fragment.IssueDataExtensive
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document

/**
 * Cache for unprocessed issues
 */
@Document
data class IssueDataCache(
    @Indexed(unique = true)
    var githubId: String, val user: String, val repo: String, val data: IssueDataExtensive, var attempts: Int?
) {
    @Id
    var id: ObjectId? = null
}