package gropius.repository.template

import gropius.model.template.InterfaceSpecificationInheritanceCondition
import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository
import org.springframework.stereotype.Repository

/**
 * Repository for [InterfaceSpecificationInheritanceCondition]
 */
@Repository
interface InterfaceSpecificationInheritanceConditionRepository :
    ReactiveNeo4jRepository<InterfaceSpecificationInheritanceCondition, String>