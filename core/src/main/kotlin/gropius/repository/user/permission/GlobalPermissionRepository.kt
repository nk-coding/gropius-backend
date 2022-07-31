package gropius.repository.user.permission

import gropius.model.user.permission.GlobalPermission
import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository
import org.springframework.stereotype.Repository

/**
 * Repository for [GlobalPermission]
 */
@Repository
interface GlobalPermissionRepository : ReactiveNeo4jRepository<GlobalPermission, String>