package gropius.sync.github.model

import gropius.sync.github.generated.fragment.IssueDataExtensive
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import java.net.URI

/**
 * Cache for unprocessed issues
 * @param url API URL of IMS of the repo
 * @param githubId ID on GitHub
 * @param data Data from github api
 * @param attempts Number of attempts tried to insert into db
 */
@Document
data class IssueDataCache(
    @Indexed(unique = true)
    var githubId: String, val url: URI, val data: IssueDataExtensive, var attempts: Int?
) {
    /**
     * MongoDB ID
     */
    @Id
    var id: ObjectId? = null
}