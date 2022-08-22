package gropius.sync.github.repository

import gropius.sync.github.model.IssueInfo
import kotlinx.coroutines.flow.Flow
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository
import java.net.URI

/**
 * Repository for mapping of a single issue from neo4j to GitHub
 */
@Repository
interface IssueInfoRepository : ReactiveMongoRepository<IssueInfo, ObjectId> {
    /**
     * Find all issues that have outstanding changes
     * @param url API URL syncing currently
     * @return result of database operation
     */
    suspend fun findByUrlAndDirtyIsTrue(url: URI): Flow<IssueInfo>

    /**
     * Lookup to find the mapping given a neo4j id
     * @param url API URL syncing currently
     * @param neo4jId Database query param
     * @return result of database operation
     */
    suspend fun findByUrlAndNeo4jId(url: URI, neo4jId: String): IssueInfo?

    /**
     * Lookup to find the mapping given a GitHub id
     * @param url API URL syncing currently
     * @param githubId Database query param
     * @return result of database operation
     */
    suspend fun findByUrlAndGithubId(url: URI, githubId: String): IssueInfo?
}


