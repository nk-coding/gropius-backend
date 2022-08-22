package gropius.repository.architecture

import gropius.model.architecture.IntraComponentDependencySpecification
import gropius.repository.GropiusRepository
import org.springframework.stereotype.Repository

/**
 * Repository for [IntraComponentDependencySpecification]
 */
@Repository
interface IntraComponentDependencySpecificationRepository :
    GropiusRepository<IntraComponentDependencySpecification, String>