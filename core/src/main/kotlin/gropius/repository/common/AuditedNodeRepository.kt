package gropius.repository.common

import gropius.model.common.AuditedNode
import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository
import org.springframework.stereotype.Repository

/**
 * Repository for [AuditedNode]
 */
@Repository
interface AuditedNodeRepository : ReactiveNeo4jRepository<AuditedNode, String>