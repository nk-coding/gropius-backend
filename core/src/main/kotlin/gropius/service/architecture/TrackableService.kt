package gropius.service.architecture

import gropius.model.architecture.Trackable
import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository

/**
 * Base class for services for subclasses of [Trackable]
 *
 * @param repository the associated repository used for CRUD functionality
 */
abstract class TrackableService<T : Trackable, R : ReactiveNeo4jRepository<T, String>>(repository: R) :
    AffectedByIssueService<T, R>(repository)