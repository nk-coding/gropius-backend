package gropius.sync.github.repository

import gropius.sync.github.model.RepositoryInfo
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository
import java.net.URI

/**
 * Repository for mapping of a single repository from neo4j to GitHub
 */
@Repository
interface RepositoryInfoRepository : ReactiveMongoRepository<RepositoryInfo, ObjectId> {
    /**
     * Lookup to find the mapping given a GitHub id
     * @param user Database query param
     * @param repo Database query param
     * @param url Database query param
     * @return result of database operation
     */
    suspend fun findByUrlAndUserAndRepo(url: URI, user: String, repo: String): RepositoryInfo?

    /**
     * Lookup to find the mapping given a IMSProject
     * @param url Database query param
     * @return result of database operation
     */
    suspend fun findByUrl(url: URI): RepositoryInfo?
}
