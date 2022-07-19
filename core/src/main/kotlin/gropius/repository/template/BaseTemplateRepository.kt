package gropius.repository.template

import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository
import org.springframework.stereotype.Repository
import gropius.model.template.BaseTemplate

/**
 * Repository for [BaseTemplate]
 */
@Repository
interface BaseTemplateRepository : ReactiveNeo4jRepository<BaseTemplate<*, *>, String>