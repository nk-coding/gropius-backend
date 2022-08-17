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
 */
@Document
data class IssueDataCache(
    /**
     * ID on github
     */
    @Indexed(unique = true)
    var githubId: String, val url: URI,
    /**
     * Data from github api
     */
    val data: IssueDataExtensive,
    /**
     * Number of attempts tried to insert into db
     */
    var attempts: Int?
) {
    /**
     * MongoDB ID
     */
    @Id
    var id: ObjectId? = null
}