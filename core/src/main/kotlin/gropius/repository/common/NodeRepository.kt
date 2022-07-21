package gropius.repository.common

import io.github.graphglue.model.Node
import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository
import org.springframework.stereotype.Repository

/**
 * Repository for [Node]
 */
@Repository
interface NodeRepository : ReactiveNeo4jRepository<Node, String>