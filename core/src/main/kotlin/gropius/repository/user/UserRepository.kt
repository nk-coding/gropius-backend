package gropius.repository.user

import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository
import org.springframework.stereotype.Repository
import gropius.model.user.User

/**
 * Repository for [User]
 */
@Repository
interface UserRepository : ReactiveNeo4jRepository<User, String>