package gropius.repository.common

import gropius.model.common.NamedNode
import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository
import org.springframework.stereotype.Repository

/**
 * Repository for [NamedNode]
 */
@Repository
interface NamedNodeRepository : ReactiveNeo4jRepository<NamedNode, String>