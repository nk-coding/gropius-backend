package gropius.repository.user.permission

import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository
import org.springframework.stereotype.Repository
import gropius.model.user.permission.NodePermission

/**
 * Repository for [NodePermission]
 */
@Repository
interface NodePermissionRepository : ReactiveNeo4jRepository<NodePermission<*>, String>