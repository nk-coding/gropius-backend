package gropius.repository.user.permission

import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository
import org.springframework.stereotype.Repository
import gropius.model.user.permission.BasePermission

/**
 * Repository for [BasePermission]
 */
@Repository
interface BasePermissionRepository : ReactiveNeo4jRepository<BasePermission, String>