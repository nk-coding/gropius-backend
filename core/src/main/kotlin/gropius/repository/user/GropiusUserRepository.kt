package gropius.repository.user

import gropius.model.user.GropiusUser
import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository
import org.springframework.stereotype.Repository

/**
 * Repository for [GropiusUser]
 */
@Repository
interface GropiusUserRepository : ReactiveNeo4jRepository<GropiusUser, String>