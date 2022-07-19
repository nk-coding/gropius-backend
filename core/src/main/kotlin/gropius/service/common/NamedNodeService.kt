package gropius.service.common

import gropius.model.common.NamedNode
import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository

/**
 * Base class for services for subclasses of [NamedNode]
 *
 * @param repository the associated repository used for CRUD functionality
 */
abstract class NamedNodeService<T : NamedNode>(repository: ReactiveNeo4jRepository<T, String>) : ExtensibleNodeService<T>(repository)