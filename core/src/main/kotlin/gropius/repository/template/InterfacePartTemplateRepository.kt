package gropius.repository.template

import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository
import org.springframework.stereotype.Repository
import gropius.model.template.InterfacePartTemplate

/**
 * Repository for [InterfacePartTemplate]
 */
@Repository
interface InterfacePartTemplateRepository : ReactiveNeo4jRepository<InterfacePartTemplate, String>