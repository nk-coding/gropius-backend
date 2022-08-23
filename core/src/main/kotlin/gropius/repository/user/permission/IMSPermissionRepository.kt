package gropius.repository.user.permission

import gropius.model.user.permission.IMSPermission
import gropius.repository.GropiusRepository
import org.springframework.stereotype.Repository

/**
 * Repository for [IMSPermission]
 */
@Repository
interface IMSPermissionRepository : GropiusRepository<IMSPermission, String>