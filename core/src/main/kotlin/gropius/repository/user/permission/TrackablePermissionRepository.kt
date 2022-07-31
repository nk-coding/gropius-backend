package gropius.repository.user.permission

import gropius.model.user.permission.TrackablePermission
import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository
import org.springframework.stereotype.Repository

/**
 * Repository for [TrackablePermission]
 */
@Repository
interface TrackablePermissionRepository : ReactiveNeo4jRepository<TrackablePermission<*>, String>