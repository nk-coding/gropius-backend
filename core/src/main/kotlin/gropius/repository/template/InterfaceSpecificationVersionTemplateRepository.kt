package gropius.repository.template

import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository
import org.springframework.stereotype.Repository
import gropius.model.template.InterfaceSpecificationVersionTemplate

/**
 * Repository for [InterfaceSpecificationVersionTemplate]
 */
@Repository
interface InterfaceSpecificationVersionTemplateRepository : ReactiveNeo4jRepository<InterfaceSpecificationVersionTemplate, String>