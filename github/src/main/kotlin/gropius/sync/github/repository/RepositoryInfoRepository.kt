package gropius.sync.github.repository

import gropius.sync.github.model.RepositoryInfo
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository

/**
 * Mapping of a single repository from neo4j to github
 */
@Repository
interface RepositoryInfoRepository : ReactiveMongoRepository<RepositoryInfo, ObjectId> {
    /**
     * Lookup to find the mapping given a github id
     */
    suspend fun findByUserAndRepo(user: String, repo: String): RepositoryInfo?
}
