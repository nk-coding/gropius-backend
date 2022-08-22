package gropius.repository.architecture

import gropius.model.architecture.Interface
import gropius.repository.GropiusRepository
import org.springframework.stereotype.Repository

/**
 * Repository for [Interface]
 */
@Repository
interface InterfaceRepository : GropiusRepository<Interface, String>