package gropius.repository.user.permission

import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository
import org.springframework.stereotype.Repository
import gropius.model.user.permission.ComponentPermission

/**
 * Repository for [ComponentPermission]
 */
@Repository
interface ComponentPermissionRepository : ReactiveNeo4jRepository<ComponentPermission, String>