package gropius.repository.user

import gropius.model.user.User
import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository
import org.springframework.stereotype.Repository

/**
 * Repository for [User]
 */
@Repository
interface UserRepository : ReactiveNeo4jRepository<User, String>