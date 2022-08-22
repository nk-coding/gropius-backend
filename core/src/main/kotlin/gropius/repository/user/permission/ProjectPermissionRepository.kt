package gropius.repository.user.permission

import gropius.model.user.permission.ProjectPermission
import gropius.repository.GropiusRepository
import org.springframework.stereotype.Repository

/**
 * Repository for [ProjectPermission]
 */
@Repository
interface ProjectPermissionRepository : GropiusRepository<ProjectPermission, String>