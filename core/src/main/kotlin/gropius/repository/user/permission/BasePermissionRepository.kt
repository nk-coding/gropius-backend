package gropius.repository.user.permission

import gropius.model.user.permission.BasePermission
import gropius.repository.GropiusRepository
import org.springframework.stereotype.Repository

/**
 * Repository for [BasePermission]
 */
@Repository
interface BasePermissionRepository : GropiusRepository<BasePermission, String>