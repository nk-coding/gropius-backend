package gropius.service.common

import gropius.model.common.NamedNode
import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository

/**
 * Base class for services for subclasses of [NamedNode]
 *
 * @param repository the associated repository used for CRUD functionality
 * @param T the type of Node this service is used for
 * @param R Repository type associated with [T]
 */
abstract class NamedNodeService<T : NamedNode, R : ReactiveNeo4jRepository<T, String>>(repository: R) :
    AbstractExtensibleNodeService<T, R>(repository)