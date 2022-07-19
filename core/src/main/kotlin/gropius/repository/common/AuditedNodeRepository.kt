package gropius.repository.common

import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository
import org.springframework.stereotype.Repository
import gropius.model.common.AuditedNode

/**
 * Repository for [AuditedNode]
 */
@Repository
interface AuditedNodeRepository : ReactiveNeo4jRepository<AuditedNode, String>