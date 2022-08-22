package gropius.repository.architecture

import gropius.model.architecture.InterfacePart
import gropius.repository.GropiusRepository
import org.springframework.stereotype.Repository

/**
 * Repository for [InterfacePart]
 */
@Repository
interface InterfacePartRepository : GropiusRepository<InterfacePart, String>