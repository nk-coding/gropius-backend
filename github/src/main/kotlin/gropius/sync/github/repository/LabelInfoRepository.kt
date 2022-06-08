package gropius.sync.github.repository

import gropius.sync.github.model.LabelInfo
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository

@Repository
interface LabelInfoRepository : ReactiveMongoRepository<LabelInfo, ObjectId> {
    suspend fun findByNeo4jId(neo4jId: String): LabelInfo?
    suspend fun findByGithubId(findByGithubId: String): LabelInfo?
}