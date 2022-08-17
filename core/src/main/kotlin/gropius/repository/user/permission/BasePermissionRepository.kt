package gropius.repository.user.permission

import gropius.model.user.permission.BasePermission
import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository
import org.springframework.stereotype.Repository

/**
 * Repository for [BasePermission]
 */
@Repository
interface BasePermissionRepository : ReactiveNeo4jRepository<BasePermission, String>