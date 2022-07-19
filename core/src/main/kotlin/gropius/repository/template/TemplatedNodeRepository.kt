package gropius.repository.template

import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository
import org.springframework.stereotype.Repository
import gropius.model.template.TemplatedNode

/**
 * Repository for [TemplatedNode]
 */
@Repository
interface TemplatedNodeRepository : ReactiveNeo4jRepository<TemplatedNode, String>