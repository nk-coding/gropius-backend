package gropius.repository.user

import gropius.model.user.GropiusUser
import gropius.repository.GropiusRepository
import org.springframework.stereotype.Repository

/**
 * Repository for [GropiusUser]
 */
@Repository
interface GropiusUserRepository : GropiusRepository<GropiusUser, String>