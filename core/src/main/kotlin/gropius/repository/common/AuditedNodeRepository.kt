package gropius.repository.common

import gropius.model.common.AuditedNode
import gropius.repository.GropiusRepository
import org.springframework.stereotype.Repository

/**
 * Repository for [AuditedNode]
 */
@Repository
interface AuditedNodeRepository : GropiusRepository<AuditedNode, String>