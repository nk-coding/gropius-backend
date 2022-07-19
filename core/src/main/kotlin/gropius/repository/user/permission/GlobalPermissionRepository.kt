package gropius.repository.user.permission

import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository
import org.springframework.stereotype.Repository
import gropius.model.user.permission.GlobalPermission

/**
 * Repository for [GlobalPermission]
 */
@Repository
interface GlobalPermissionRepository : ReactiveNeo4jRepository<GlobalPermission, String>