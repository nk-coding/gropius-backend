package gropius.repository.user.permission

import gropius.model.user.permission.ProjectPermission
import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository
import org.springframework.stereotype.Repository

/**
 * Repository for [ProjectPermission]
 */
@Repository
interface ProjectPermissionRepository : ReactiveNeo4jRepository<ProjectPermission, String>