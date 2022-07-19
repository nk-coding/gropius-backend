package gropius.repository.architecture

import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository
import org.springframework.stereotype.Repository
import gropius.model.architecture.Trackable

/**
 * Repository for [Trackable]
 */
@Repository
interface TrackableRepository : ReactiveNeo4jRepository<Trackable, String>