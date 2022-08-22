package gropius.repository.architecture

import gropius.model.architecture.ComponentVersion
import gropius.repository.GropiusRepository
import org.springframework.stereotype.Repository

/**
 * Repository for [ComponentVersion]
 */
@Repository
interface ComponentVersionRepository : GropiusRepository<ComponentVersion, String>