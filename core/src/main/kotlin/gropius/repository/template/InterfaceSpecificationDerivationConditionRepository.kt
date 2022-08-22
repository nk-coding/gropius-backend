package gropius.repository.template

import gropius.model.template.InterfaceSpecificationDerivationCondition
import gropius.repository.GropiusRepository
import org.springframework.stereotype.Repository

/**
 * Repository for [InterfaceSpecificationDerivationCondition]
 */
@Repository
interface InterfaceSpecificationDerivationConditionRepository :
    GropiusRepository<InterfaceSpecificationDerivationCondition, String>