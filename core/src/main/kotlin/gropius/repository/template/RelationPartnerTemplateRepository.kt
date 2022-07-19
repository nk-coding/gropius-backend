package gropius.repository.template

import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository
import org.springframework.stereotype.Repository
import gropius.model.template.RelationPartnerTemplate

/**
 * Repository for [RelationPartnerTemplate]
 */
@Repository
interface RelationPartnerTemplateRepository : ReactiveNeo4jRepository<RelationPartnerTemplate<*, *>, String>