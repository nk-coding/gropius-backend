package gropius.repository.template

import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository
import org.springframework.stereotype.Repository
import gropius.model.template.RelationCondition

/**
 * Repository for [RelationCondition]
 */
@Repository
interface RelationConditionRepository : ReactiveNeo4jRepository<RelationCondition, String>