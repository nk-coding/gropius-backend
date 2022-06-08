package gropius.sync.github.model

import gropius.model.issue.Issue
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.neo4j.core.ReactiveNeo4jOperations
import org.springframework.data.neo4j.core.findById
import java.time.OffsetDateTime

@Document
data class IssueInfo(
    @Indexed(unique = true)
    var githubId: String,
    @Indexed(unique = true)
    var neo4jId: String, val dirty: Boolean, var lastAccess: OffsetDateTime?
) {
    @Id
    var id: ObjectId? = null
    suspend fun load(neoOperations: ReactiveNeo4jOperations): Issue {
        return neoOperations.findById<Issue>(neo4jId)!!
    }
}