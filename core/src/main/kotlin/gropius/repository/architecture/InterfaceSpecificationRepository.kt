package gropius.repository.architecture

import gropius.model.architecture.InterfaceSpecification
import gropius.repository.GropiusRepository
import org.springframework.stereotype.Repository

/**
 * Repository for [InterfaceSpecification]
 */
@Repository
interface InterfaceSpecificationRepository : GropiusRepository<InterfaceSpecification, String>