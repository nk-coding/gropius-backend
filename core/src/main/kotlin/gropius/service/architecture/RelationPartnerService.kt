package gropius.service.architecture

import gropius.model.architecture.RelationPartner
import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository

/**
 * Base class for services for subclasses of [RelationPartner]
 *
 * @param repository the associated repository used for CRUD functionality
 */
abstract class RelationPartnerService<T : RelationPartner>(repository: ReactiveNeo4jRepository<T, String>) : AffectedByIssueService<T>(repository)