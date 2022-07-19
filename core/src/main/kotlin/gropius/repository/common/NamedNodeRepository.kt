package gropius.repository.common

import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository
import org.springframework.stereotype.Repository
import gropius.model.common.NamedNode

/**
 * Repository for [NamedNode]
 */
@Repository
interface NamedNodeRepository : ReactiveNeo4jRepository<NamedNode, String>