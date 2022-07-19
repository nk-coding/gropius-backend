package gropius.repository.user.permission

import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository
import org.springframework.stereotype.Repository
import gropius.model.user.permission.PermissionConfiguration

/**
 * Repository for [PermissionConfiguration]
 */
@Repository
interface PermissionConfigurationRepository : ReactiveNeo4jRepository<PermissionConfiguration, String>