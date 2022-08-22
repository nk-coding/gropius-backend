package gropius.repository.architecture

import gropius.model.architecture.InterfaceSpecificationVersion
import gropius.repository.GropiusRepository
import org.springframework.stereotype.Repository

/**
 * Repository for [InterfaceSpecificationVersion]
 */
@Repository
interface InterfaceSpecificationVersionRepository : GropiusRepository<InterfaceSpecificationVersion, String>