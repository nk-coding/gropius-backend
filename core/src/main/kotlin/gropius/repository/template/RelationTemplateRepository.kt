package gropius.repository.template

import gropius.model.template.RelationTemplate
import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository
import org.springframework.stereotype.Repository

/**
 * Repository for [RelationTemplate]
 */
@Repository
interface RelationTemplateRepository : ReactiveNeo4jRepository<RelationTemplate, String>