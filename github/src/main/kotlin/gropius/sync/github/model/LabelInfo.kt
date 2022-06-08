package gropius.sync.github.model
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class LabelInfo(
    @Indexed(unique = true)
    val githubId: String,
    @Indexed(unique = true)
    val neo4jId: String
) {
    @Id
    var id: ObjectId? = null
}