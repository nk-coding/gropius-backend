package gropius.repository.architecture

import gropius.model.architecture.InterfaceDefinition
import gropius.repository.GropiusRepository
import org.springframework.stereotype.Repository

/**
 * Repository for [InterfaceDefinition]
 */
@Repository
interface InterfaceDefinitionRepository : GropiusRepository<InterfaceDefinition, String>