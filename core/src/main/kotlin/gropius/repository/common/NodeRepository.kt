package gropius.repository.common

import io.github.graphglue.model.Node
import gropius.repository.GropiusRepository
import org.springframework.stereotype.Repository

/**
 * Repository for [Node]
 */
@Repository
interface NodeRepository : GropiusRepository<Node, String>