package gropius.repository.common

import gropius.model.common.NamedAuditedNode
import gropius.repository.GropiusRepository
import org.springframework.stereotype.Repository

/**
 * Repository for [NamedAuditedNode]
 */
@Repository
interface NamedAuditedNodeRepository : GropiusRepository<NamedAuditedNode, String>