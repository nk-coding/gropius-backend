package gropius.repository.architecture

import gropius.model.architecture.Trackable
import gropius.repository.GropiusRepository
import org.springframework.stereotype.Repository

/**
 * Repository for [Trackable]
 */
@Repository
interface TrackableRepository : GropiusRepository<Trackable, String>