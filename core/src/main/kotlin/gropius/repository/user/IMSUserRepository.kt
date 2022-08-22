package gropius.repository.user

import gropius.model.user.IMSUser
import gropius.repository.GropiusRepository
import org.springframework.stereotype.Repository

/**
 * Repository for [IMSUser]
 */
@Repository
interface IMSUserRepository : GropiusRepository<IMSUser, String>