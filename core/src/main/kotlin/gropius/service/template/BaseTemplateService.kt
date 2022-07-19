package gropius.service.template

import gropius.model.template.BaseTemplate
import gropius.service.common.NamedNodeService
import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository

/**
 * Base class for services for subclasses of [BaseTemplate]
 *
 * @param repository the associated repository used for CRUD functionality
 */
abstract class BaseTemplateService<T : BaseTemplate<*, *>>(repository: ReactiveNeo4jRepository<T, String>) : NamedNodeService<T>(repository)