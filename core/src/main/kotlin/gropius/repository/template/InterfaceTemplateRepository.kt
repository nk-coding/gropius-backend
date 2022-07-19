package gropius.repository.template

import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository
import org.springframework.stereotype.Repository
import gropius.model.template.InterfaceTemplate

/**
 * Repository for [InterfaceTemplate]
 */
@Repository
interface InterfaceTemplateRepository : ReactiveNeo4jRepository<InterfaceTemplate, String>