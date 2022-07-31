package gropius.repository.template

import gropius.model.template.RelationPartnerTemplate
import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository
import org.springframework.stereotype.Repository

/**
 * Repository for [RelationPartnerTemplate]
 */
@Repository
interface RelationPartnerTemplateRepository : ReactiveNeo4jRepository<RelationPartnerTemplate<*, *>, String>