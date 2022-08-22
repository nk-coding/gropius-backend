package gropius.repository.architecture

import gropius.model.architecture.Component
import gropius.repository.GropiusRepository
import org.springframework.stereotype.Repository

/**
 * Repository for [Component]
 */
@Repository
interface ComponentRepository : GropiusRepository<Component, String>