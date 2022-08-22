package gropius.repository.user.permission

import gropius.model.user.permission.GlobalPermission
import gropius.repository.GropiusRepository
import org.springframework.stereotype.Repository

/**
 * Repository for [GlobalPermission]
 */
@Repository
interface GlobalPermissionRepository : GropiusRepository<GlobalPermission, String>