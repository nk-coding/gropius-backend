package gropius.repository.template

import gropius.model.template.ComponentTemplate
import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository
import org.springframework.stereotype.Repository

/**
 * Repository for [ComponentTemplate]
 */
@Repository
interface ComponentTemplateRepository : ReactiveNeo4jRepository<ComponentTemplate, String>