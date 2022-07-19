package gropius.service.common

import io.github.graphglue.model.Node
import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository

/**
 * Base class for services for subclasses of [Node]
 *
 * @param repository the associated repository used for CRUD functionality
 */
abstract class NodeService<T : Node>(val repository: ReactiveNeo4jRepository<T, String>)