package gropius.repository.user.permission

import gropius.model.user.permission.NodePermission
import gropius.repository.GropiusRepository
import org.springframework.stereotype.Repository

/**
 * Repository for [NodePermission]
 */
@Repository
interface NodePermissionRepository : GropiusRepository<NodePermission<*>, String>