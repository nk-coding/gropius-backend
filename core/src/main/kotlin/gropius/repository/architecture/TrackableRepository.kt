package gropius.repository.architecture

import gropius.model.architecture.Trackable
import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository
import org.springframework.stereotype.Repository

/**
 * Repository for [Trackable]
 */
@Repository
interface TrackableRepository : ReactiveNeo4jRepository<Trackable, String>