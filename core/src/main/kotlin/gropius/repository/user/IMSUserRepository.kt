package gropius.repository.user

import gropius.model.user.IMSUser
import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository
import org.springframework.stereotype.Repository

/**
 * Repository for [IMSUser]
 */
@Repository
interface IMSUserRepository : ReactiveNeo4jRepository<IMSUser, String>