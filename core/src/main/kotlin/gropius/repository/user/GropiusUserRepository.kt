package gropius.repository.user

import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository
import org.springframework.stereotype.Repository
import gropius.model.user.GropiusUser

/**
 * Repository for [GropiusUser]
 */
@Repository
interface GropiusUserRepository : ReactiveNeo4jRepository<GropiusUser, String>