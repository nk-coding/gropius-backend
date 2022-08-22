package gropius.repository.common

import gropius.model.common.NamedNode
import gropius.repository.GropiusRepository
import org.springframework.stereotype.Repository

/**
 * Repository for [NamedNode]
 */
@Repository
interface NamedNodeRepository : GropiusRepository<NamedNode, String>