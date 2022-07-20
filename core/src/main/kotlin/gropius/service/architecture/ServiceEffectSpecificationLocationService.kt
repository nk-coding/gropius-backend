package gropius.service.architecture

import gropius.model.architecture.ServiceEffectSpecificationLocation
import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository

/**
 * Base class for services for subclasses of [ServiceEffectSpecificationLocation]
 *
 * @param repository the associated repository used for CRUD functionality
 */
abstract class ServiceEffectSpecificationLocationService<T : ServiceEffectSpecificationLocation, R : ReactiveNeo4jRepository<T, String>>(
    repository: R
) : AffectedByIssueService<T, R>(repository)