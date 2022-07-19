package gropius.repository.user.permission

import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository
import org.springframework.stereotype.Repository
import gropius.model.user.permission.TrackablePermission

/**
 * Repository for [TrackablePermission]
 */
@Repository
interface TrackablePermissionRepository : ReactiveNeo4jRepository<TrackablePermission<*>, String>