package gropius.service.common

import gropius.model.common.NamedAuditedNode
import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository

/**
 * Base class for services for subclasses of [NamedAuditedNode]
 *
 * @param repository the associated repository used for CRUD functionality
 * @param T the type of Node this service is used for
 * @param R Repository type associated with [T]
 */
abstract class NamedAuditedNodeService<T : NamedAuditedNode, R : ReactiveNeo4jRepository<T, String>>(repository: R) :
    AuditedNodeService<T, R>(repository)