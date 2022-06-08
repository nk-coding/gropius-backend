package gropius.sync.github.repository

import gropius.sync.github.model.TimelineEventInfo
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository

@Repository
interface TimelineEventInfoRepository : ReactiveMongoRepository<TimelineEventInfo, ObjectId> {
    suspend fun findByNeo4jId(neo4jId: String): TimelineEventInfo?
    suspend fun findByGithubId(findByGithubId: String): TimelineEventInfo?
}