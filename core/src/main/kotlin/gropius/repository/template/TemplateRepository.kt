package gropius.repository.template

import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository
import org.springframework.stereotype.Repository
import gropius.model.template.Template

/**
 * Repository for [Template]
 */
@Repository
interface TemplateRepository : ReactiveNeo4jRepository<Template<*, *>, String>