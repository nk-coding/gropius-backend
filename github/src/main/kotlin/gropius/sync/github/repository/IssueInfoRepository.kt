package gropius.sync.github.repository

import gropius.sync.github.model.IssueInfo
import kotlinx.coroutines.flow.Flow
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository

/**
 * Repository for mapping of a single issue from neo4j to github
 */
@Repository
interface IssueInfoRepository : ReactiveMongoRepository<IssueInfo, ObjectId> {
    /**
     * Find all issues that have outstanding changes
     * @param imsProject IMSProject syncing currently
     * @return result of database operation
     */
    suspend fun findByProjectAndDirtyIsTrue(project: String): Flow<IssueInfo>

    /**
     * Lookup to find the mapping given a neo4j id
     * @param imsProject IMSProject syncing currently
     * @param neo4jId Database query param
     * @return result of database operation
     */
    suspend fun findByIMSProjectAndNeo4jId(imsProject: String, neo4jId: String): IssueInfo?

    /**
     * Lookup to find the mapping given a github id
     * @param imsProject IMSProject syncing currently
     * @param githubId Database query param
     * @return result of database operation
     */
    suspend fun findByIMSProjectAndGithubId(imsProject: String, githubId: String): IssueInfo?
}


