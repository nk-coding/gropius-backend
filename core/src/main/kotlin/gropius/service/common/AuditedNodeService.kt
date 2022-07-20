package gropius.service.common

import gropius.model.common.AuditedNode
import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository

/**
 * Base class for services for subclasses of [AuditedNode]
 *
 * @param repository the associated repository used for CRUD functionality
 */
abstract class AuditedNodeService<T : AuditedNode, R : ReactiveNeo4jRepository<T, String>>(repository: R) :
    AbstractExtensibleNodeService<T, R>(repository)