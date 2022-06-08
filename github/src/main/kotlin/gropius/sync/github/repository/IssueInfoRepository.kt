package gropius.sync.github.repository

import gropius.sync.github.model.IssueInfo
import kotlinx.coroutines.flow.Flow
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository

@Repository
interface IssueInfoRepository : ReactiveMongoRepository<IssueInfo, ObjectId> {
    suspend fun findByDirtyIsTrue(): Flow<IssueInfo>
    suspend fun findByNeo4jId(neo4jId: String): IssueInfo?
    suspend fun findByGithubId(findByGithubId: String): IssueInfo?
}


