package gropius.repository.template

import gropius.model.template.BaseTemplate
import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository
import org.springframework.stereotype.Repository

/**
 * Repository for [BaseTemplate]
 */
@Repository
interface BaseTemplateRepository : ReactiveNeo4jRepository<BaseTemplate<*, *>, String>