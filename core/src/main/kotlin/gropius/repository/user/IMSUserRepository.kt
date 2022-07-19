package gropius.repository.user

import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository
import org.springframework.stereotype.Repository
import gropius.model.user.IMSUser

/**
 * Repository for [IMSUser]
 */
@Repository
interface IMSUserRepository : ReactiveNeo4jRepository<IMSUser, String>