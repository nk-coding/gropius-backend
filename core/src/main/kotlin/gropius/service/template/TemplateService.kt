package gropius.service.template

import gropius.model.template.Template
import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository

/**
 * Base class for services for subclasses of [Template]
 *
 * @param repository the associated repository used for CRUD functionality
 */
abstract class TemplateService<T : Template<*, *>>(repository: ReactiveNeo4jRepository<T, String>) : BaseTemplateService<T>(repository)