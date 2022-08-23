package gropius.repository.user

import gropius.model.user.User
import gropius.repository.GropiusRepository
import org.springframework.stereotype.Repository

/**
 * Repository for [User]
 */
@Repository
interface UserRepository : GropiusRepository<User, String>