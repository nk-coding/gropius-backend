package gropius.sync.github.repository

import gropius.sync.github.model.RepositoryInfo
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository

/**
 * Repository for mapping of a single repository from neo4j to github
 */
@Repository
interface RepositoryInfoRepository : ReactiveMongoRepository<RepositoryInfo, ObjectId> {
    /**
     * Lookup to find the mapping given a github id
     * @param user Database query param
     * @param repo Database query param
     * @return result of database operation
     */
    suspend fun findByUserAndRepo(user: String, repo: String): RepositoryInfo?

    /**
     * Lookup to find the mapping given a IMSProject
     * @param imsProject Database query param
     * @return result of database operation
     */
    suspend fun findByImsProject(imsProject: String): RepositoryInfo?
}
