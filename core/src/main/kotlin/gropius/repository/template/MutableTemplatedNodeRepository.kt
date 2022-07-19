package gropius.repository.template

import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository
import org.springframework.stereotype.Repository
import gropius.model.template.MutableTemplatedNode

/**
 * Repository for [MutableTemplatedNode]
 */
@Repository
interface MutableTemplatedNodeRepository : ReactiveNeo4jRepository<MutableTemplatedNode, String>