package gropius.service.architecture

import gropius.model.architecture.AffectedByIssue
import gropius.service.common.NamedNodeService
import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository

/**
 * Base class for services for subclasses of [AffectedByIssue]
 *
 * @param repository the associated repository used for CRUD functionality
 */
abstract class AffectedByIssueService<T : AffectedByIssue>(repository: ReactiveNeo4jRepository<T, String>) : NamedNodeService<T>(repository)