package gropius.repository.user.permission

import gropius.model.user.permission.ComponentPermission
import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository
import org.springframework.stereotype.Repository

/**
 * Repository for [ComponentPermission]
 */
@Repository
interface ComponentPermissionRepository : ReactiveNeo4jRepository<ComponentPermission, String>