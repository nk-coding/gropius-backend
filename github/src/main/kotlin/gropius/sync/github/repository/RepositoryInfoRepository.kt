package gropius.sync.github.repository

import gropius.sync.github.model.RepositoryInfo
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository

@Repository
interface RepositoryInfoRepository : ReactiveMongoRepository<RepositoryInfo, ObjectId> {
    suspend fun findByUserAndRepo(user: String, repo: String): RepositoryInfo?
}
