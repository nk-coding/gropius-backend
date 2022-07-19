package gropius.repository.user.permission

import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository
import org.springframework.stereotype.Repository
import gropius.model.user.permission.PermissionEntry

/**
 * Repository for [PermissionEntry]
 */
@Repository
interface PermissionEntryRepository : ReactiveNeo4jRepository<PermissionEntry, String>