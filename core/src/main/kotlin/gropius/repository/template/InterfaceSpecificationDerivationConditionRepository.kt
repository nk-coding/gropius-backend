package gropius.repository.template

import gropius.model.template.InterfaceSpecificationDerivationCondition
import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository
import org.springframework.stereotype.Repository

/**
 * Repository for [InterfaceSpecificationDerivationCondition]
 */
@Repository
interface InterfaceSpecificationDerivationConditionRepository :
    ReactiveNeo4jRepository<InterfaceSpecificationDerivationCondition, String>