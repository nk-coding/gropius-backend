package gropius.repository.architecture

import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository
import org.springframework.stereotype.Repository
import gropius.model.architecture.Relation

/**
 * Repository for [Relation]
 */
@Repository
interface RelationRepository : ReactiveNeo4jRepository<Relation, String>