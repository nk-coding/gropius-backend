package gropius.repository.common

import gropius.model.common.ExtensibleNode
import gropius.repository.GropiusRepository
import org.springframework.stereotype.Repository

/**
 * Repository for [ExtensibleNode]
 */
@Repository
interface ExtensibleNodeRepository : GropiusRepository<ExtensibleNode, String>