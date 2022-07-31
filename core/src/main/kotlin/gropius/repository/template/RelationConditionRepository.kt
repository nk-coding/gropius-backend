package gropius.repository.template

import gropius.model.template.RelationCondition
import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository
import org.springframework.stereotype.Repository

/**
 * Repository for [RelationCondition]
 */
@Repository
interface RelationConditionRepository : ReactiveNeo4jRepository<RelationCondition, String>