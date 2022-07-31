package gropius.repository.architecture

import gropius.model.architecture.Relation
import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository
import org.springframework.stereotype.Repository

/**
 * Repository for [Relation]
 */
@Repository
interface RelationRepository : ReactiveNeo4jRepository<Relation, String>