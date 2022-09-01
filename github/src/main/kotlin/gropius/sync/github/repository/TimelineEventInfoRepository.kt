package gropius.sync.github.repository

import gropius.sync.github.model.TimelineEventInfo
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository
import java.net.URI

/**
 * Repository for mapping of a single timeline event from neo4j to GitHub
 */
@Repository
interface TimelineEventInfoRepository : ReactiveMongoRepository<TimelineEventInfo, ObjectId> {
    /**
     * Lookup to find the mapping given a neo4j id
     * @param neo4jId Database query param
     * @return result of database operation
     */
    suspend fun findByNeo4jId(neo4jId: String): TimelineEventInfo?

    /**
     * Lookup to find the mapping given a GitHub id
     * @param githubId Database query param
     * @param url Database query param
     * @return result of database operation
     */
    suspend fun findByUrlAndGithubId(url: URI, githubId: String): TimelineEventInfo?
}