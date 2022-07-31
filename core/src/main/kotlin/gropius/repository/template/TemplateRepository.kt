package gropius.repository.template

import gropius.model.template.Template
import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository
import org.springframework.stereotype.Repository

/**
 * Repository for [Template]
 */
@Repository
interface TemplateRepository : ReactiveNeo4jRepository<Template<*, *>, String>