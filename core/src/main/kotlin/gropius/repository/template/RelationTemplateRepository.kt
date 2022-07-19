package gropius.repository.template

import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository
import org.springframework.stereotype.Repository
import gropius.model.template.RelationTemplate

/**
 * Repository for [RelationTemplate]
 */
@Repository
interface RelationTemplateRepository : ReactiveNeo4jRepository<RelationTemplate, String>