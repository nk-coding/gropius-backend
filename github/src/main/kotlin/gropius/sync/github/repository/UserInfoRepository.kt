package gropius.sync.github.repository

import gropius.sync.github.model.UserInfo
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository

@Repository
interface UserInfoRepository : ReactiveMongoRepository<UserInfo, ObjectId> {
    suspend fun findByLogin(login: String): UserInfo?
    suspend fun findByNeo4jId(neo4jId: String): UserInfo?
}
