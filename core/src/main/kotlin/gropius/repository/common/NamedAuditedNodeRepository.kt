package gropius.repository.common

import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository
import org.springframework.stereotype.Repository
import gropius.model.common.NamedAuditedNode

/**
 * Repository for [NamedAuditedNode]
 */
@Repository
interface NamedAuditedNodeRepository : ReactiveNeo4jRepository<NamedAuditedNode, String>