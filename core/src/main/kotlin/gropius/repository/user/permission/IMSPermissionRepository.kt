package gropius.repository.user.permission

import gropius.model.user.permission.IMSPermission
import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository
import org.springframework.stereotype.Repository

/**
 * Repository for [IMSPermission]
 */
@Repository
interface IMSPermissionRepository : ReactiveNeo4jRepository<IMSPermission, String>