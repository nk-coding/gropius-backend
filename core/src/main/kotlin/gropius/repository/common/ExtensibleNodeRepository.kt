package gropius.repository.common

import gropius.model.common.ExtensibleNode
import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository
import org.springframework.stereotype.Repository

/**
 * Repository for [ExtensibleNode]
 */
@Repository
interface ExtensibleNodeRepository : ReactiveNeo4jRepository<ExtensibleNode, String>