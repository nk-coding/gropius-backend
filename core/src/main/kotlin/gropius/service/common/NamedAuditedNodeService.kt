package gropius.service.common

import gropius.model.common.NamedAuditedNode
import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository

/**
 * Base class for services for subclasses of [NamedAuditedNode]
 *
 * @param repository the associated repository used for CRUD functionality
 */
abstract class NamedAuditedNodeService<T : NamedAuditedNode>(repository: ReactiveNeo4jRepository<T, String>) : AuditedNodeService<T>(repository)