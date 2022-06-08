package gropius.sync.github.model

import gropius.sync.github.generated.fragment.TimelineItemData
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class TimelineItemDataCache(
    @Indexed(unique = true)
    var githubId: String, val issue: String, val data: TimelineItemData, var attempts: Int?
) {
    @Id
    var id: ObjectId? = null
}