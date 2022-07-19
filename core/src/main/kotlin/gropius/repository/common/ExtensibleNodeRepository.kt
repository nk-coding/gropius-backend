package gropius.repository.common

import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository
import org.springframework.stereotype.Repository
import gropius.model.common.ExtensibleNode

/**
 * Repository for [ExtensibleNode]
 */
@Repository
interface ExtensibleNodeRepository : ReactiveNeo4jRepository<ExtensibleNode, String>