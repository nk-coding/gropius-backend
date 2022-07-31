package gropius.repository.template

import gropius.model.template.InterfaceSpecificationTemplate
import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository
import org.springframework.stereotype.Repository

/**
 * Repository for [InterfaceSpecificationTemplate]
 */
@Repository
interface InterfaceSpecificationTemplateRepository : ReactiveNeo4jRepository<InterfaceSpecificationTemplate, String>