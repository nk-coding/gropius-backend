package gropius.repository.user.permission

import gropius.model.user.permission.NodePermission
import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository
import org.springframework.stereotype.Repository

/**
 * Repository for [NodePermission]
 */
@Repository
interface NodePermissionRepository : ReactiveNeo4jRepository<NodePermission<*>, String>