package gropius.repository.user.permission

import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository
import org.springframework.stereotype.Repository
import gropius.model.user.permission.IMSPermission

/**
 * Repository for [IMSPermission]
 */
@Repository
interface IMSPermissionRepository : ReactiveNeo4jRepository<IMSPermission, String>