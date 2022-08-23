package gropius.sync.github.model

import gropius.sync.github.generated.fragment.TimelineItemData
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document

/**
 * Cache for unprocessed timeline items
 * @param imsProject IMSProject of the repo
 * @param githubId ID on github
 * @param issue Github ID of the associated issue
 * @param data Raw Github API data
 * @param attempts Number of attempts to sync into gropius database
 */
@Document
data class TimelineItemDataCache(
    @Indexed(unique = true)
    var githubId: String, val issue: String, val data: TimelineItemData, var attempts: Int?, val imsProject: String
) {
    /**
     * MongoDB ID
     */
    @Id
    var id: ObjectId? = null
}