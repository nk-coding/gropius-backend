package gropius.repository.template

import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository
import org.springframework.stereotype.Repository
import gropius.model.template.ComponentTemplate

/**
 * Repository for [ComponentTemplate]
 */
@Repository
interface ComponentTemplateRepository : ReactiveNeo4jRepository<ComponentTemplate, String>