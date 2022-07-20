package gropius.sync.github.repository

import gropius.sync.github.model.IssueInfo
import kotlinx.coroutines.flow.Flow
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository

/**
 * Mapping of a single issue from neo4j to github
 */
@Repository
interface IssueInfoRepository : ReactiveMongoRepository<IssueInfo, ObjectId> {
    /**
     * Find all issues that have outstanding changes
     */
    suspend fun findByDirtyIsTrue(): Flow<IssueInfo>

    /**
     * Lookup to find the mapping given a neo4j id
     */
    suspend fun findByNeo4jId(neo4jId: String): IssueInfo?

    /**
     * Lookup to find the mapping given a github id
     */
    suspend fun findByGithubId(findByGithubId: String): IssueInfo?
}


