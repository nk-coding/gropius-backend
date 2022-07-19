package gropius.repository.template

import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository
import org.springframework.stereotype.Repository
import gropius.model.template.InterfaceSpecificationInheritanceCondition

/**
 * Repository for [InterfaceSpecificationInheritanceCondition]
 */
@Repository
interface InterfaceSpecificationInheritanceConditionRepository : ReactiveNeo4jRepository<InterfaceSpecificationInheritanceCondition, String>