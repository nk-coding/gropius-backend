package gropius.repository.user.permission

import gropius.model.user.permission.TrackablePermission
import gropius.repository.GropiusRepository
import org.springframework.stereotype.Repository

/**
 * Repository for [TrackablePermission]
 */
@Repository
interface TrackablePermissionRepository : GropiusRepository<TrackablePermission<*>, String>