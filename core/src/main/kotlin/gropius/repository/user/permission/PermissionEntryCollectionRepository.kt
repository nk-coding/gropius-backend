package gropius.repository.user.permission

import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository
import org.springframework.stereotype.Repository
import gropius.model.user.permission.PermissionEntryCollection

/**
 * Repository for [PermissionEntryCollection]
 */
@Repository
interface PermissionEntryCollectionRepository : ReactiveNeo4jRepository<PermissionEntryCollection<*>, String>