package gropius.service.common

import gropius.model.common.ExtensibleNode
import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository

/**
 * Base class for services for subclasses of [ExtensibleNode]
 *
 * @param repository the associated repository used for CRUD functionality
 */
abstract class ExtensibleNodeService<T : ExtensibleNode>(repository: ReactiveNeo4jRepository<T, String>) : NodeService<T>(repository)