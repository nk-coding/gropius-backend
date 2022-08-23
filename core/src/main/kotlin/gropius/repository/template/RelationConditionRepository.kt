package gropius.repository.template

import gropius.model.template.RelationCondition
import gropius.repository.GropiusRepository
import org.springframework.stereotype.Repository

/**
 * Repository for [RelationCondition]
 */
@Repository
interface RelationConditionRepository : GropiusRepository<RelationCondition, String>