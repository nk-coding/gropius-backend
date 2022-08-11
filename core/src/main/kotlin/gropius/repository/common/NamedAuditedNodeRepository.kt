package gropius.repository.common

import gropius.model.common.NamedAuditedNode
import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository
import org.springframework.stereotype.Repository

/**
 * Repository for [NamedAuditedNode]
 */
@Repository
interface NamedAuditedNodeRepository : ReactiveNeo4jRepository<NamedAuditedNode, String>