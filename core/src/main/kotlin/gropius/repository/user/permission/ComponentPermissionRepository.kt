package gropius.repository.user.permission

import gropius.model.user.permission.ComponentPermission
import gropius.repository.GropiusRepository
import org.springframework.stereotype.Repository

/**
 * Repository for [ComponentPermission]
 */
@Repository
interface ComponentPermissionRepository : GropiusRepository<ComponentPermission, String>